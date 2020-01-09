package com.project.rine;

import android.util.Log;

import java.math.BigInteger;
import java.util.Collections;

public class Encrypter {
    /*encrypt and decrypt C = p**e mod n*/
    public static BigInteger encryptBigInteger(BigInteger src, BigInteger n, BigInteger ed) {
        /*
        * @par src: Source Bigint waiting to be encrypted or decrypted
        * @par n: from private and public key
        * @par ed: e from public key or d from private key
        *
        * @result dst: BigInteger, encrypted or decryped from src, using key n and e or d
        * return src**ed mod n;
        * */
        BigInteger dst, tmp;
        BigInteger zero = new BigInteger("0");
        BigInteger one = new BigInteger("1");
        BigInteger two = new BigInteger("2");
        if (ed.equals(zero)) {
            return one;
        }
        if (ed.equals(one)) {
            return src.mod(n);
        }
        dst = encryptBigInteger(src, n, ed.divide(two));
        tmp = dst.multiply(dst);
        dst = tmp.mod(n);
        if (ed.and(one).equals(one)) {
            tmp = dst.multiply(src);
            dst = tmp.mod(n);
        }
        return dst;
    }
    /*plainText -> cipherText*/
    public static String encrypt(String plainText, BigInteger n, BigInteger e) {
        /*
        * @par plainText: String, text waiting to be encrypted
        * @par n: BigInteger, from public and private key
        * @par e: BigInteger, from public key
        *
        * @result: String, encrypted from plainText.
        * The result is a suits of Strings concat with each other, each should
        * be long as (bitLen * 4), where bitLen = n.bitLength() / 10 - 1;
        *   When decrypting, first split the whole text as length (bitLen * 4),
        * then use encryptBigInteger to decrypt to a String-Byte, finally change
        * them as String.
        * */
        int bitLen = n.bitLength() / 10 - 1;
        gV.xLog("RINE_debug", "Encrypting begin.");
        gV.xLog("RINE_debug", "bitLen ="+bitLen);
        gV.xLog("RINE_debug", "n.toString().length() ="+
                n.toString().length());
        gV.xLog("RINE_debug", "n.toString() ="+
                n.toString());
        int start = -bitLen, end = 0;
        /* Ex:plainText="abc", plainByte="097098099" */
        String plainByte = StringByte.stringToNumbers(plainText, "utf-8");
        gV.xLog("RINE_debug", "plainByte ="+plainByte);
        int len = plainByte.length();
        String subStr = null;
        BigInteger plainNum = null;
        BigInteger cipherNum = null;
        String cipherString = null;
        StringBuilder builder = new StringBuilder();
        while (end < len) {
            start += bitLen;
            end += bitLen;
            if (end > len) {
                end = len;
            }
            subStr = plainByte.substring(start, end);
            plainNum = new BigInteger(subStr);
            cipherNum = encryptBigInteger(plainNum, n, e);
            cipherString = cipherNum.toString();
            gV.xLog("RINE_debug", "cipherString.length(ini) ="+
                    cipherString.length());
            int outerZeros = bitLen * 4 - cipherString.length();
            cipherString = String.join("",
                    Collections.nCopies(outerZeros, "0")) + cipherString;
            gV.xLog("RINE_debug", "cipherString.length(fin) ="+
                    cipherString.length());
            builder.append(cipherString);
        }
        gV.xLog("RINE_debug", "cipherNum ="+builder.toString());
        gV.xLog("RINE_debug", "Encrypting end.");
        return builder.toString();
    }

    public static String decrypt(String cipherText, BigInteger n, BigInteger d) {
        /*
        *@par cipherText: String, text waiting to be decrypted.
        *@par n: BigInteger, assume m is cipherText, then m**d mod n = c, c is plainText
        *@par d: As above
        *
        *@result: String, decrypted plain message.
        * Note: This algorithm should be used with function "encrypt", not other
        * encrypting functions.
        * */
        int bitLen = n.bitLength() / 10 - 1;
        gV.xLog("RINE_debug", "Decrypting begin.");
        gV.xLog("RINE_debug", "cipherText.length ="+
                cipherText.length());
        int strLen = bitLen;
        int subLen = bitLen * 4;
        int totalNum = cipherText.length() / subLen;
        gV.xLog("RINE_debug", "totalNum ="+totalNum);
        String subStr, plainNum, plainString, tmp;
        BigInteger cipherInt, plainInt;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < totalNum; i++) {
            subStr = cipherText.substring(i*subLen, (i+1)*subLen);
            cipherInt = new BigInteger(subStr);
            plainInt = encryptBigInteger(cipherInt, n, d);
            plainNum = plainInt.toString();
            int outerZeros = strLen - plainNum.length();
            plainNum = String.join("",
                    Collections.nCopies(outerZeros, "0")) + plainNum;
            gV.xLog("RINE_debug", "plainNum ="+plainNum);
            if (i == totalNum-1) {
                outerZeros = 3 - plainNum.length() % 3;
                plainNum = String.join("",
                        Collections.nCopies(outerZeros, "0")) + plainNum;
                while (StringByte.begin3zeros(plainNum)) {
                    gV.xLog("RINE_debug", "delete first 3 zeros");
                    tmp = plainNum.substring(3);
                    plainNum = tmp;
                }
                gV.xLog("RINE_debug", "plainNum(fin) ="+plainNum);
            }
            builder.append(plainNum);
        }
        plainString = StringByte.numbersToString(builder.toString());
        return plainString;
    }
}
