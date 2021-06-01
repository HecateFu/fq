package org.fcx.fq.util;

import lombok.extern.slf4j.Slf4j;
import org.fcx.fq.exception.MyToolException;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@Slf4j
public class MyToolUtil {
    /**
     * Base64 Url 解码，UTF-8 字符集
     * @param raw 密文
     * @return 明文
     */
    public static String base64UrlDecode(String raw) {
        byte[] bs = Base64.getUrlDecoder().decode(raw.trim());
        try {
            String decodeStr = new String(bs, "UTF-8");
            return decodeStr;
        } catch (UnsupportedEncodingException e){
            MyToolException mye = new MyToolException("base64解码生成String，字符集异常："+e.getMessage());
            mye.setStackTrace(e.getStackTrace());
            throw mye;
        }
    }

    /**
     * Base64 Basic 解码，UTF-8 字符集
     * @param raw 密文
     * @return 明文
     */
    public static String base64Decode(String raw) {
        byte[] bs = Base64.getDecoder().decode(raw.trim());
        try {
            String decodeStr = new String(bs, "UTF-8");
            return decodeStr;
        } catch (UnsupportedEncodingException e){
            MyToolException mye = new MyToolException("base64解码生成String，字符集异常："+e.getMessage());
            mye.setStackTrace(e.getStackTrace());
            throw mye;
        }
    }

}