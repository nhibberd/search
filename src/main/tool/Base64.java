package main.tool;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Base64 {
    public static byte[] base64ToByte(String data) {
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            return decoder.decodeBuffer(data);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String byteToBase64(byte[] data){
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }
    
    public static String stringToBase64(String data) {
        return byteToBase64(data.getBytes());  
    }

    public static byte[] stringToBase64Bytes(String data) {
        try {
            return stringToBase64(data).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String base64BytesToString(byte[] b64) {
        try {
            return base64ToString(new String(b64, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
    
    public static String base64ToString(String b64) {
        byte[] bytes = base64ToByte(b64);
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
}

