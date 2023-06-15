use crate::trait_pixel::Pixel;
use png::ColorType;
use std::fs::File;
use std::{
    fmt,
    io::{self, ErrorKind},
};

pub struct ImageStruct<T: Pixel + Clone + Copy + Copy> {
    pub width: u32,
    pub height: u32,
    pub color_type: png::ColorType,
    pub pixels: Vec<T>,
    pub plte: Option<Vec<u8>>,
    pub trns: Option<Vec<u8>>,
}

//Debug Implementation
impl<T: Pixel + Clone + Copy + Copy> fmt::Debug for ImageStruct<T> {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        write!(
            f,
            "ImageStruct {{ width: {}, height: {}, color_type: {:?}, pixels: length {} plte:  {} trns: {} }}",
            self.width,
            self.height,
            self.color_type,
            self.pixels.len(),
            if self.plte.is_some() {"Some"} else {"None"},
            if self.trns.is_some() {"Some"} else {"None"},
        )
    }
}

impl<T: Pixel + Clone + Copy + Copy> ImageStruct<T> {
    pub fn clone(&self) -> Self {
        Self {
            width: self.width,
            height: self.height,
            color_type: self.color_type,
            pixels: self.pixels.clone(),
            plte: self.plte.clone(),
            trns: self.trns.clone(),
        }
    }

    pub fn pixels_to_u8(&self) -> ImageStruct<u8> {
        let pixels: Vec<u8> = self.pixels.iter().map(|pixel| pixel.to_u8()).collect();
        ImageStruct {
            width: self.width,
            height: self.height,
            color_type: self.color_type,
            pixels,
            plte: self.plte.clone(),
            trns: self.trns.clone(),
        }
    }

    pub fn pixels_to_f32(&self) -> ImageStruct<f32> {
        let pixels: Vec<f32> = self.pixels.iter().map(|pixel| pixel.to_f32()).collect();
        ImageStruct {
            width: self.width,
            height: self.height,
            color_type: self.color_type,
            pixels,
            plte: self.plte.clone(),
            trns: self.trns.clone(),
        }
    }

    pub fn to_rgb(&self) -> io::Result<Self> {
        let rgb_data: Vec<T> = match self.color_type {
            ColorType::Rgb => self.pixels.clone(),
            ColorType::Rgba => self
                .pixels
                .chunks(4)
                .flat_map(|rgba| {
                    let r = rgba[0].clone();
                    let g = rgba[1].clone();
                    let b = rgba[2].clone();
                    vec![r, g, b]
                })
                .collect(),
            ColorType::GrayscaleAlpha => self
                .pixels
                .chunks(2)
                .flat_map(|ga| {
                    let r = ga[0].clone();
                    let g = ga[0].clone();
                    let b = ga[0].clone();
                    vec![r, g, b]
                })
                .collect(),
            ColorType::Grayscale => self
                .pixels
                .iter()
                .flat_map(|g| {
                    let r = g.clone();
                    let g = g.clone();
                    let b = g.clone();
                    vec![r, g, b]
                })
                .collect(),
            ColorType::Indexed => {
                let converted = self.convert_from_indexed()?;
                let result = converted.to_rgb()?;
                result.pixels
            }
        };

        Ok(ImageStruct {
            width: self.width,
            height: self.height,
            color_type: ColorType::Rgb,
            pixels: rgb_data,
            trns: None,
            plte: None,
        })
    }

    pub fn convert_to_grayscale(&self) -> io::Result<ImageStruct<u8>> {
        let converted = self.pixels_to_u8();

        let (gray_data, color_type) = match self.color_type {
            ColorType::Rgb => (
                converted
                    .pixels
                    .chunks(3)
                    .map(|rgb| {
                        let r = rgb[0] as f32;
                        let g = rgb[1] as f32;
                        let b = rgb[2] as f32;
                        (0.3 * r + 0.59 * g + 0.11 * b) as u8
                    })
                    .collect(),
                ColorType::Grayscale,
            ),
            ColorType::Rgba => (
                converted
                    .pixels
                    .chunks(4)
                    .flat_map(|rgba| {
                        let r = rgba[0] as f32;
                        let g = rgba[1] as f32;
                        let b = rgba[2] as f32;
                        let a = rgba[3];
                        vec![(0.3 * r + 0.59 * g + 0.11 * b) as u8, a]
                    })
                    .collect(),
                ColorType::GrayscaleAlpha,
            ),
            ColorType::Grayscale => (converted.pixels, ColorType::Grayscale),
            ColorType::GrayscaleAlpha => (converted.pixels, ColorType::GrayscaleAlpha),
            ColorType::Indexed => {
                let converted = self.convert_from_indexed()?;
                let result = converted.convert_to_grayscale()?;
                (result.pixels, result.color_type)
            }
        };
        Ok(ImageStruct {
            width: self.width,
            height: self.height,
            color_type: color_type,
            pixels: gray_data,
            trns: None,
            plte: None,
        })
    }
    // function that converts indexed "image_data" to RGB or RGBA...
    pub fn convert_from_indexed(&self) -> io::Result<Self> {
        let converted = self.pixels_to_u8();

        match converted.color_type {
            ColorType::Indexed => ColorType::Indexed,
            _ => return Err(io::Error::new(
                ErrorKind::InvalidData,
                "Function convert_from_indexed only accepts ImageStructs with ColorType Indexed",
            )),
        };

        let color_type;
        let expected_length: usize;

        //Find out if the png has tRNS chunk (Alpha values for colors in palette) //
        if converted.trns.clone().is_some() {
            color_type = ColorType::Rgba;
            expected_length = converted.pixels.len() * 4;
        } else {
            color_type = ColorType::Rgb;
            expected_length = converted.pixels.len() * 3;
        }

        // Initialize a new vector to store the resulting RGB data (or RGBA if transparency exists)
        let mut rgb_data: Vec<T> = Vec::with_capacity(expected_length);

        // Extract and store the RGB palette from the PLTE chunk into the "palette" variable
        let palette = match converted.clone().plte {
            Some(data) => data,
            None => return Err(io::Error::new(ErrorKind::InvalidData, "No palette found")),
        };
        // Calculate the number of colors in the palette (each color is represented by three elements: RGB values)
        let colors_in_palette = palette.len() / 3;
        // Determine how many pixels are encoded by each byte (8-bit number) from the "image_data" vector
        let pixels_per_byte: usize;
        match colors_in_palette {
            1 | 2 => pixels_per_byte = 8,    // 1-2 colors: 8 pixels per byte
            3..=4 => pixels_per_byte = 4,    // 3-4 colors: 4 pixels per byte
            5..=16 => pixels_per_byte = 2,   // 5-16 colors: 2 pixels per byte
            17..=256 => pixels_per_byte = 1, // 17-256 colors: 1 pixel per byte
            _ => {
                return Err(io::Error::new(
                    ErrorKind::InvalidData,
                    "Invalid number of colors in the palette! (minimum: 1, maximum: 256",
                ))
            }
        }
        //Initialize byte_index to iterate through image_data
        let mut byte_index: usize = 0;
        let mut skip_counter: usize = 0;
        // Loop over the height of the image (each row of pixels)
        for _ in 0..converted.height as usize {
            // Loop over the width of the image (each column of pixels)
            for _ in 0..converted.width as usize {
                let skip_size = match converted.width % pixels_per_byte as u32 {
                    0 => 0,
                    _ => {
                        (pixels_per_byte as u32 - (converted.width % pixels_per_byte as u32))
                            as usize
                    }
                };

                if (byte_index - (skip_counter * skip_size)) as u32 % converted.width == 0
                    && byte_index != 0
                {
                    byte_index += skip_size;
                    skip_counter += 1;
                }
                // Retrieve the byte from the image_data. The index is calculated by integer division of
                // byte_index by pixels_per_byte. This means that each byte may contain data for multiple pixels.
                let byte = converted.pixels[byte_index / pixels_per_byte];
                // Calculate the index of the pixel within the byte by using modulus of byte_index with pixels_per_byte.
                // This allows us to determine which pixel's color we're currently processing.
                let pixel_index = byte_index % pixels_per_byte;
                // Determine the index of the color in the palette that should be used for this pixel.
                // The actual calculation depends on how many pixels each byte represents.
                let color_index = match pixels_per_byte {
                    // The match arm calculations will be continued below this point
                    // When a byte represents 8 pixels (palette contains 2 colors),
                    // the byte is right-shifted by (7 - pixel_index) positions, and
                    // then bitwise AND operation with 1 is performed to keep only the last bit.
                    // (The result is either 0 or 1, which is the index of color in the palette.)
                    8 => (byte >> (7 - pixel_index)) & 1,
                    // When a byte represents 4 pixels (palette contains 4 colors),
                    // the byte is right-shifted by (2 * (3 - pixel_index)) positions, and
                    // then bitwise AND operation with 3 (0b11 in binary) is performed to keep only the last two bits.
                    // (The result is a number between 0 and 3, which is the index of color in the palette.)
                    4 => (byte >> (2 * (3 - pixel_index))) & 3,
                    // When a byte represents 2 pixels (palette contains 16 colors),
                    // the byte is right-shifted by (4 * (1 - pixel_index)) positions, and
                    // then bitwise AND operation with 15 (0b1111 in binary) is performed to keep only the last four bits.
                    // (The result is a number between 0 and 15, which is the index of color in the palette.)
                    2 => (byte >> (4 * (1 - pixel_index))) & 15,
                    // When a byte represents 1 pixel (palette contains 256 colors),
                    // the byte is directly used (bitwise AND operation with 255 to keep all 8 bits).
                    // (The result is a number between 0 and 255, which is the index of color in the palette.)
                    1 => byte & 255,
                    _ => {
                        return Err(io::Error::new(
                            ErrorKind::InvalidData,
                            "Invalid number of pixels per byte / colors in  palette",
                        ))
                    }
                };

                let palette_t: Vec<T> = palette.iter().map(|b| T::from_u8(&b)).collect();
                // Get the RGB values from the palette based on the color index
                let rgb = &palette_t[(color_index as usize) * 3..(color_index as usize) * 3 + 3];
                rgb_data.extend_from_slice(rgb);
                // If a transparency chunk exists, push alpha value
                if converted.trns.clone().is_some() {
                    let alpha_data: Vec<u8> = converted.trns.clone().unwrap();
                    // Check if the transparency chunk matches the palette in length
                    // if it does, get transparency value of the color based on the color index
                    if alpha_data.len() * 3 == palette.len() {
                        let alpha = alpha_data[color_index as usize];
                        rgb_data.push(T::from_u8(&alpha));
                    //Otherwise error
                    } else {
                        return Err(io::Error::new(
                            ErrorKind::InvalidData,
                            "something is wrong with the tRNS chunk",
                        ));
                    }
                }
                //Increment byte_index

                byte_index += 1;
            }
        }

        Ok(ImageStruct {
            width: converted.width,
            height: converted.height,
            color_type: color_type,
            pixels: rgb_data,
            plte: None,
            trns: None,
        })
    }
}

pub fn read_png(file_path: &str) -> io::Result<ImageStruct<u8>> {
    let file = match File::open(file_path) {
        Ok(file) => file,
        Err(_) => {
            let mut error = String::from(file_path);
            error.push_str(&String::from(" could not be found / read"));
            return Err(io::Error::new(ErrorKind::NotFound, error));
        }
    };
    // The decoder is a build for reader and can be used to set various decoding options
    let decoder = png::Decoder::new(file);
    let mut reader = match decoder.read_info() {
        Ok(reader) => reader,
        Err(_) => {
            return Err(io::Error::new(
                ErrorKind::InvalidData,
                "Could not decode PNG data",
            ))
        }
    };
    // Allocate the output buffer.
    let mut image_data = vec![0; reader.output_buffer_size()];
    // Read the next frame. An APNG might contain multiple frames. In our program we only consider the first.
    let info = match reader.next_frame(&mut image_data) {
        Ok(info) => info,
        Err(_) => {
            return Err(io::Error::new(
                ErrorKind::InvalidData,
                "Could not decode PNG data",
            ))
        }
    };
    let plte = match reader.info().palette.as_ref() {
        Some(data) => Some(data.to_vec()),
        None => None,
    };

    let trns = match reader.info().trns.as_ref() {
        Some(data) => Some(data.to_vec()),
        None => None,
    };
    Ok(ImageStruct {
        width: info.width,
        height: info.height,
        color_type: info.color_type,
        pixels: image_data,
        plte: plte,
        trns: trns,
    })
}

#[cfg(test)]
mod image_tests {
    use super::*;
    use png::ColorType;

    #[test]
    fn test_pixels_to_u8() {
        let image = ImageStruct {
            width: 1,
            height: 1,
            color_type: ColorType::Rgb,
            pixels: vec![0.5f32],
            plte: None,
            trns: None,
        };

        let converted = image.pixels_to_u8();
        assert_eq!(converted.pixels, vec![127]);
    }

    #[test]
    fn test_pixels_to_f32() {
        let image = ImageStruct {
            width: 1,
            height: 1,
            color_type: ColorType::Rgb,
            pixels: vec![255u8],
            plte: None,
            trns: None,
        };

        let converted = image.pixels_to_f32();
        assert_eq!(converted.pixels, vec![1.0f32]);
    }

    #[test]
    fn test_clone() {
        let image = ImageStruct {
            width: 1,
            height: 1,
            color_type: ColorType::Rgb,
            pixels: vec![1],
            plte: None,
            trns: None,
        };

        let clone = image.clone();
        assert_eq!(clone.width, image.width);
        assert_eq!(clone.height, image.height);
        assert_eq!(clone.color_type, image.color_type);
        assert_eq!(clone.pixels, image.pixels);
        assert_eq!(clone.plte, image.plte);
        assert_eq!(clone.trns, image.trns);
    }
    #[test]
    fn test_functions() {
        use crate::visual_tests::encode_and_write;

        //initialize test images of all color types
        let rgba_image = ImageStruct {
            width: 6,
            height: 2,
            color_type: ColorType::Rgba,
            pixels: vec![
                137, 42, 201, 255, 19, 174, 83, 200, 96, 211, 47, 150, 224, 89, 54, 100, 72, 120,
                209, 50, 186, 31, 143, 0, 137, 42, 201, 255, 19, 174, 83, 200, 96, 211, 47, 150,
                224, 89, 54, 100, 72, 120, 209, 50, 186, 31, 143, 0,
            ],
            plte: None,
            trns: None,
        };
        encode_and_write(
            "visual_tests/test_images/rgba_image.png",
            &rgba_image,
            png::BitDepth::Eight,
        );

        let rgb_image = ImageStruct {
            width: 6,
            height: 2,
            color_type: ColorType::Rgb,
            pixels: vec![
                137, 42, 201, 19, 174, 83, 96, 211, 47, 224, 89, 54, 72, 120, 209, 186, 31, 143,
                137, 42, 201, 19, 174, 83, 96, 211, 47, 224, 89, 54, 72, 120, 209, 186, 31, 143,
            ],
            plte: None,
            trns: None,
        };
        encode_and_write(
            "visual_tests/test_images/rgb_image.png",
            &rgb_image,
            png::BitDepth::Eight,
        );
        let gray_alpha_image = ImageStruct {
            width: 6,
            height: 2,
            color_type: ColorType::GrayscaleAlpha,
            pixels: vec![
                87, 255, 117, 200, 158, 150, 125, 100, 115, 50, 89, 0, 87, 255, 117, 200, 158, 150,
                125, 100, 115, 50, 89, 0,
            ],
            plte: None,
            trns: None,
        };
        encode_and_write(
            "visual_tests/test_images/gray_alpha_image.png",
            &gray_alpha_image,
            png::BitDepth::Eight,
        );
        let gray_image = ImageStruct {
            width: 6,
            height: 2,
            color_type: ColorType::Grayscale,
            pixels: vec![87, 117, 158, 125, 115, 89, 87, 117, 158, 125, 115, 89],
            plte: None,
            trns: None,
        };
        encode_and_write(
            "visual_tests/test_images/gray_image.png",
            &gray_image,
            png::BitDepth::Eight,
        );
        let indexed_rgba_image = ImageStruct {
            width: 6,
            height: 2,
            color_type: ColorType::Indexed,
            pixels: vec![1, 35, 69, 1, 35, 69],
            plte: Some(vec![
                137, 42, 201, 19, 174, 83, 96, 211, 47, 224, 89, 54, 72, 120, 209, 186, 31, 143,
            ]),
            trns: Some(vec![255, 200, 150, 100, 50, 0]),
        };
        encode_and_write(
            "visual_tests/test_images/indexed_rgba_image.png",
            &indexed_rgba_image,
            png::BitDepth::Four,
        );
        let indexed_rgb_image = ImageStruct {
            width: 6,
            height: 2,
            color_type: ColorType::Indexed,
            pixels: vec![1, 35, 69, 1, 35, 69],
            plte: Some(vec![
                137, 42, 201, 19, 174, 83, 96, 211, 47, 224, 89, 54, 72, 120, 209, 186, 31, 143,
            ]),
            trns: None,
        };
        encode_and_write(
            "visual_tests/test_images/indexed_rgb_image.png",
            &indexed_rgb_image,
            png::BitDepth::Four,
        );

        // tests
        let gray_png = read_png("visual_tests/test_images/gray_image.png").unwrap();
        assert_eq!(
            gray_png.pixels,
            gray_image
                .to_rgb()
                .unwrap()
                .convert_to_grayscale()
                .unwrap()
                .pixels
        );
        assert_eq!(
            gray_png.pixels,
            rgb_image
                .to_rgb()
                .unwrap()
                .convert_to_grayscale()
                .unwrap()
                .pixels
        );
        assert_eq!(
            gray_png.pixels,
            rgba_image
                .to_rgb()
                .unwrap()
                .convert_to_grayscale()
                .unwrap()
                .pixels
        );
        assert_eq!(
            gray_png.pixels,
            gray_alpha_image
                .to_rgb()
                .unwrap()
                .convert_to_grayscale()
                .unwrap()
                .pixels
        );
        assert_eq!(
            gray_png.pixels,
            indexed_rgba_image
                .to_rgb()
                .unwrap()
                .convert_to_grayscale()
                .unwrap()
                .pixels
        );
        assert_eq!(
            gray_png.pixels,
            indexed_rgba_image
                .to_rgb()
                .unwrap()
                .convert_to_grayscale()
                .unwrap()
                .pixels
        );
    }
}
