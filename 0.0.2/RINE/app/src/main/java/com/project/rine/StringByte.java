package com.project.rine;

import java.io.UnsupportedEncodingException;

public class StringByte {
    public static byte[] stringToBytes(String str, String method) {
        /*
        * @par str: String, source String waiting to be changed into bytes
        * @par method: String, encoding method, "utf-8" is usually used
        *
        * @result: byte[], encoded from str
        * Note:(1) result's length may not match with str.length, because
        * Chinese characters may be encoded to two or more bytes
        * (2) result cannot be used directly to generate a BigInteger,
        * because byte may be under 0 (ex: -12). While using, bytes under 0
        * should be changed into Integer then +256.
        * */
        try {
            return str.getBytes(method);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String bytesToString(byte[] bs, String method) {
        /*
        * @par bs: byte[], byte presentation of dst String.
        * @par method: String, usually "utf-8"
        *
        * @result: String, encoded from bs using method.
        * */
        try {
            return new String(bs, method);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String stringToNumbers(String str, String method) {
        /*
        * @par str: String, raw String waiting to be changed into a
        *   String of numbers, which can be used to generate a BigInteger.
        * @par method: String, usually "utf-8"
        *
        * @result: String, a String of numbers that can be used to
        *   generate a BigInteger.
        * Example: str="123", method="utf-8", then
        *   result = "049050051"
        * */
        StringBuilder builder = new StringBuilder();
        byte[] bs = stringToBytes(str, method);
        int ib;
        String sib;
        for (int i = 0; i < bs.length; ++i) {
            ib = bs[i];
            if (ib < 0) {
                ib = ib + 256;
            }
            if (ib < 10) {
                sib = "00" + String.valueOf(ib);
            } else if (ib < 100) {
                sib = "0" + String.valueOf(ib);
            } else {
                sib = String.valueOf(ib);
            }
            builder.append(sib);
        }
        return builder.toString();
    }

    public static boolean begin3zeros(String str) {
        /*
        * @par str: String, source String
        *
        * @result: boolean, whether source String begins with three zeros("000")
        * */
        return (str.charAt(0) == '0' && str.charAt(1) == '0' && str.charAt(2) == '0');
    }

    public static String numbersToString(String numbers) {
        /*
        * @par numbers: String, a String of numbers, three of which makes a byte
        *   presentation of dst String.
        *
        * @result: String, using "utf-8" encoded from bytes.
        * Example: numbers="049050051", then result="123".
        * */
        int totalCount = numbers.length() / 3;
        String subStr;
        byte c;
        byte[] bytes = new byte[totalCount];
        for (int i = 0; i < totalCount; i++) {
            subStr = numbers.substring(i*3, (i+1)*3);
            c = (byte)Integer.parseInt(subStr, 10);
            bytes[i] = c;
        }
        return bytesToString(bytes, "utf-8");
    }
    public static String parsePostSendMsg(int srcId, int dstId, String msg) {
        return "{\"srcid\":\"" + srcId + "\",\"dstid\":\"" + dstId +
                "\",\"text\":\"" + msg + "\"}";
    }
    public static String parsePostGetMsg(int dstId) {
        return "{\"dstId\":\""+dstId+"\"}";
    }
    public static String parsePostSignUp(String name, String password, String keyN, String keyE) {
        return "{\"name\":\""+name+"\",\"password\":\""+password+"\",\"keyN\":\""+
                keyN+"\",\"keyE\":\""+keyE+"\"}";
    }
    public static String parsePostGetFriend(String name) {
        return "{\"name\":\"" + name + "\"}";
    }
    public static String parsePostLogIn(String name, String password) {
        return "{\"name\":\"" + name + "\",\"password\":\"" + password + "\"}";
    }
}
