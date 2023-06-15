use crate::{struct_image::*, trait_pixel::*};
use png::ColorType;
use std::convert::Into;
use std::io::Write;
use std::io::{self, ErrorKind};

pub fn medium_brightness<T: Pixel + Clone + Copy>(image: &ImageStruct<T>) -> io::Result<f32> {
    return match image.color_type {
        ColorType::Grayscale => {
            let sum = image.pixels.iter().map(|pixel| pixel.to_u8()).sum::<u8>();
            return Ok((sum as f32 / (image.height * image.width) as f32) / 255.0);
        }
        ColorType::Indexed => {
            let converted = image.convert_from_indexed()?;
            let result = medium_brightness(&converted.convert_to_grayscale()?)?;
            Ok(result)
        }
        _ => {
            let image_rgb = image.to_rgb()?;
            let image_gray = image_rgb.convert_to_grayscale()?;
            let result = medium_brightness(&image_gray)?;
            Ok(result)
        }
    };
}

pub fn color_histogram<T: Pixel + Clone + Copy>(image: &ImageStruct<T>) -> io::Result<Vec<u32>> {
    let converted_f32 = image.pixels_to_f32();
    let mut histogram: Vec<u32> = vec![0; 15];
    match image.color_type {
        ColorType::Rgb => {
            for rgb_chunk in converted_f32.pixels.chunks(3) {
                let red_index = if rgb_chunk[0] == 1.0 {
                    4
                } else {
                    (rgb_chunk[0] * 5.0).floor() as usize
                };
                let green_index = if rgb_chunk[1] == 1.0 {
                    9
                } else {
                    5 + (rgb_chunk[1] * 5.0).floor() as usize
                };
                let blue_index = if rgb_chunk[2] == 1.0 {
                    14
                } else {
                    10 + (rgb_chunk[2] * 5.0).floor() as usize
                };
                histogram[red_index] += 1;
                histogram[green_index] += 1;
                histogram[blue_index] += 1;
            }
            Ok(histogram)
        }
        ColorType::Indexed => {
            let converted = image.convert_from_indexed()?;
            let result = color_histogram(&converted)?;
            Ok(result)
        }
        _ => {
            let image_rgb = image.to_rgb()?;
            let result = color_histogram(&image_rgb)?;
            Ok(result)
        }
    }
}

fn cosine_similarity<A, B>(vec1: &[A], vec2: &[B]) -> io::Result<f32>
where
    A: Into<f64> + Copy,
    B: Into<f64> + Copy,
{
    if vec1.len() != vec2.len() {
        return Err(io::Error::new(
            ErrorKind::InvalidData,
            "Vectors have to be of same length",
        ));
    }
    let vec1 = vec1.iter().map(|&x| x.into()).collect::<Vec<f64>>();
    let vec2 = vec2.iter().map(|&x| x.into()).collect::<Vec<f64>>();

    let dot_product = vec1
        .iter()
        .zip(vec2.clone())
        .map(|(x, y)| x * y)
        .sum::<f64>();
    let magnitude1 = (vec1.iter().map(|&x| x * x).sum::<f64>()).sqrt();
    let magnitude2 = (vec2.iter().map(|&x| x * x).sum::<f64>()).sqrt();

    if magnitude1 == 0.0 || magnitude2 == 0.0 {
        return Ok(0.0); // Handle division by zero
    }

    Ok((dot_product / (magnitude1 * magnitude2)) as f32)
}

pub fn display_histogram(histogram: &[u32]) {
    if 1 == 1 {
        histogram_to_writer(histogram, &mut io::stdout());
    }
}

fn histogram_to_writer<W: Write>(histogram: &[u32], writer: &mut W) -> io::Result<()> {
    let max_count = *histogram.iter().max().unwrap_or(&0);
    let max_height = 10; // Determine the maximum height of the columns

    for i in (0..=max_height).rev() {
        for (index, bin) in histogram.iter().enumerate() {
            let color = match index {
                0..=4 => "\x1B[31m", // red
                5..=9 => "\x1B[32m", // green
                _ => "\x1B[34m",     // blue
            };

            let bin_height = (*bin as f32 / max_count as f32 * max_height as f32).round() as u32;

            if bin_height >= i {
                write!(writer, "{}{} \x1B[0m\t", color, '\u{2588}')?;
            } else {
                write!(writer, "\t")?; // A tab for the space
            }
        }
        writeln!(writer)?;
    }

    // Print the values under the bars
    for bin in histogram {
        write!(writer, "{:3}\t", bin)?;
    }
    writeln!(writer)?;

    Ok(())
}

#[cfg(test)]
mod feature_tests {
    use super::*;
    use png::ColorType;

    #[test]
    fn test_medium_brightness() {
        let gray_image = ImageStruct {
            width: 1,
            height: 2,
            color_type: ColorType::Grayscale,
            pixels: vec![255, 0],
            plte: None,
            trns: None,
        };
        assert_eq!(medium_brightness(&gray_image).unwrap(), 0.5);
    }
    //Hallo
    #[test]
    fn test_color_histogram() {
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

        let histogram1 = color_histogram(&indexed_rgba_image).unwrap();
        let histogram2 = color_histogram(&rgb_image).unwrap();
        assert_eq!(
            vec![2, 4, 2, 2, 2, 4, 2, 2, 2, 2, 2, 4, 2, 2, 2],
            histogram1
        );
        assert_eq!(
            vec![2, 4, 2, 2, 2, 4, 2, 2, 2, 2, 2, 4, 2, 2, 2],
            histogram2
        );
    }

    #[test]
    fn test_cosine_similarity() {
        let vec1 = &[1.0, 2.0, 3.0];
        let vec2 = &[1.0, 2.0, 3.0];
        let vec3 = &[4.0, 5.0];
        let vec4 = &[0.0, 0.0, 0.0];

        // Test: if vectors have equal values, cosine similarity should be 1
        let result = cosine_similarity(vec1, vec2).unwrap();
        assert_eq!(result, 1.0);

        // Test: If vectors have different lengths, an error should be triggered
        let result = cosine_similarity(vec1, vec3);
        assert!(matches!(result, Err(ref e) if e.kind() == ErrorKind::InvalidData));

        // Test: If one of the vectors is zero, the cosine similarity should be 0
        let result = cosine_similarity(vec1, vec4).unwrap();
        assert_eq!(result, 0.0);
    }

    #[test]
    fn test_display_histogram() {
        let histogram = &[2, 4, 2, 2, 2, 4, 2, 2, 2, 2, 2, 4, 2, 2, 2];
        let mut output = Vec::new();
        histogram_to_writer(histogram, &mut output).unwrap();
        let output_str = String::from_utf8(output).expect("No valid UTF8-Output");

        let expected_str = "\t\u{1b}[31m█ \u{1b}[0m\t\t\t\t\u{1b}[32m█ \u{1b}[0m\t\t\t\t\t\t\u{1b}[34m█ \u{1b}[0m\t\t\t\t\n\t\u{1b}[31m█ \u{1b}[0m\t\t\t\t\u{1b}[32m█ \u{1b}[0m\t\t\t\t\t\t\u{1b}[34m█ \u{1b}[0m\t\t\t\t\n\t\u{1b}[31m█ \u{1b}[0m\t\t\t\t\u{1b}[32m█ \u{1b}[0m\t\t\t\t\t\t\u{1b}[34m█ \u{1b}[0m\t\t\t\t\n\t\u{1b}[31m█ \u{1b}[0m\t\t\t\t\u{1b}[32m█ \u{1b}[0m\t\t\t\t\t\t\u{1b}[34m█ \u{1b}[0m\t\t\t\t\n\t\u{1b}[31m█ \u{1b}[0m\t\t\t\t\u{1b}[32m█ \u{1b}[0m\t\t\t\t\t\t\u{1b}[34m█ \u{1b}[0m\t\t\t\t\n\u{1b}[31m█ \u{1b}[0m\t\u{1b}[31m█ \u{1b}[0m\t\u{1b}[31m█ \u{1b}[0m\t\u{1b}[31m█ \u{1b}[0m\t\u{1b}[31m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\n\u{1b}[31m█ \u{1b}[0m\t\u{1b}[31m█ \u{1b}[0m\t\u{1b}[31m█ \u{1b}[0m\t\u{1b}[31m█ \u{1b}[0m\t\u{1b}[31m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\n\u{1b}[31m█ \u{1b}[0m\t\u{1b}[31m█ \u{1b}[0m\t\u{1b}[31m█ \u{1b}[0m\t\u{1b}[31m█ \u{1b}[0m\t\u{1b}[31m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\n\u{1b}[31m█ \u{1b}[0m\t\u{1b}[31m█ \u{1b}[0m\t\u{1b}[31m█ \u{1b}[0m\t\u{1b}[31m█ \u{1b}[0m\t\u{1b}[31m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\n\u{1b}[31m█ \u{1b}[0m\t\u{1b}[31m█ \u{1b}[0m\t\u{1b}[31m█ \u{1b}[0m\t\u{1b}[31m█ \u{1b}[0m\t\u{1b}[31m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\n\u{1b}[31m█ \u{1b}[0m\t\u{1b}[31m█ \u{1b}[0m\t\u{1b}[31m█ \u{1b}[0m\t\u{1b}[31m█ \u{1b}[0m\t\u{1b}[31m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[32m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\u{1b}[34m█ \u{1b}[0m\t\n  2\t  4\t  2\t  2\t  2\t  4\t  2\t  2\t  2\t  2\t  2\t  4\t  2\t  2\t  2\t\n"; // Setzen Sie Ihren erwarteten String hier ein.
        assert_eq!(output_str, expected_str);
    }
}
