use png::ColorType;

use crate::{struct_image::*};
use std::borrow::Cow;
use std::{fs::File, io::BufWriter, path::Path};



pub fn encode_and_write(destination: &str, image: &ImageStruct<u8>, bit_depth: png::BitDepth) {
    let path = Path::new(&destination);
    let file = File::create(path).unwrap();
    let ref mut w = BufWriter::new(file);
    let mut encoder = png::Encoder::new(w, image.width, image.height);
    encoder.set_color(image.color_type);
    encoder.set_depth(bit_depth);
    if image.color_type == ColorType::Indexed {
        let palette: Cow<'_, [u8]> = match image.plte {
            Some(ref data) => Cow::Borrowed(data),
            None => Cow::Borrowed(&[]), // oder Cow::Borrowed(&plte[..]) falls `plte` ein Vec<u8> ist
        };
        encoder.set_palette(palette);
        if image.trns.is_some() {
            let trns_palette: Cow<'_, [u8]> = match image.trns {
                Some(ref data) => Cow::Borrowed(data),
                None => Cow::Borrowed(&[]), // oder Cow::Borrowed(&plte[..]) falls `plte` ein Vec<u8> ist
            };
            encoder.set_trns(trns_palette);
        }
    }
    let mut writer = encoder.write_header().unwrap();
    let data: Vec<u8> = image.pixels.clone();
    writer.write_image_data(&data).unwrap(); // Save
}

pub fn clone_to_rgb(source_path: &str) {
    let image = read_png(source_path).unwrap();
    let clone = image.to_rgb().unwrap();
    let mut destination = String::from("visual_tests\\rgb_clones\\rgb_clone_");
    let file_path = Path::new(source_path);
    let name = file_path
        .file_name()
        .and_then(|name| name.to_str())
        .unwrap();
    destination.push_str(name);
    encode_and_write(&destination, &clone, png::BitDepth::Eight)
}

pub fn clone_to_grayscale(source_path: &str) {
    let image = read_png(source_path).unwrap();
    let clone = image.convert_to_grayscale().unwrap().pixels_to_u8(); //convert functions are tested here
    let mut destination = String::from("visual_tests\\gray_clones\\gray_clone_");
    let file_path = Path::new(source_path);
    let name = file_path
        .file_name()
        .and_then(|name| name.to_str())
        .unwrap();
    destination.push_str(name);
    encode_and_write(&destination, &clone, png::BitDepth::Eight)
}

pub fn test_all(source_path: &str) {
    clone_to_rgb(source_path);
    clone_to_grayscale(source_path);
}

#[cfg(test)]
mod image_tests {
    use super::*;

    #[test]
    fn convert_and_write_clone_test() {
        test_all("visual_tests/test_images/indexed_rgba_image.png");
        test_all("visual_tests/test_images/indexed_rgb_image.png");
        test_all("visual_tests/test_images/gray_alpha_image.png");
        test_all("visual_tests/test_images/gray_image.png");
        test_all("visual_tests/test_images/rgba_image.png");
        test_all("visual_tests/test_images/rgba_image.png");
    }
}
