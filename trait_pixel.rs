pub trait Pixel {
    fn to_f32(&self) -> f32;
    fn to_u8(&self) -> u8;
    fn from_u8(value: &u8) -> Self;
}

impl Pixel for u8 {
    fn to_f32(&self) -> f32 {
        *self as f32 / 255.0
    }
    fn to_u8(&self) -> Self {
        *self
    }
    fn from_u8(value: &u8) -> Self {
        *value
    }
}

impl Pixel for f32 {
    fn to_f32(&self) -> Self {
        *self
    }
    fn to_u8(&self) -> u8 {
        (*self * 255.0) as u8
    }
    fn from_u8(value: &u8) -> Self {
        *value as f32
    }
}

#[cfg(test)]
mod test {
    use super::*;

    #[test]
    fn test_u8_pixel() {
        let u: u8 = 127;
        assert_eq!(u.to_f32(), 127.0 / 255.0);
        assert_eq!(u.to_u8(), 127);
        let u_from_u8 = u8::from_u8(&255);
        assert_eq!(u_from_u8, 255);
    }

    #[test]
    fn test_f32_pixel() {
        let f: f32 = 0.5;
        assert_eq!(f.to_f32(), 0.5);
        assert_eq!(f.to_u8(), 127);  // 0.5 * 255 = 127.5, but due to conversion, it becomes 127.
        let f_from_u8 = f32::from_u8(&255);
        assert_eq!(f_from_u8, 255.0);
    }

    #[test]
    fn test_u8_to_f32_conversion() {
        let u: u8 = 255;
        let f: f32 = u.to_f32();
        assert_eq!(f, 1.0); // 255/255 = 1.0
    }

    #[test]
    fn test_f32_to_u8_conversion() {
        let f: f32 = 1.0;
        let u: u8 = f.to_u8();
        assert_eq!(u, 255);  // 1.0 * 255 = 255
    }
}