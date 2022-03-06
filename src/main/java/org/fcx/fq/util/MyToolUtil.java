package org.fcx.fq.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.fcx.fq.exception.MyToolException;

public class MyToolUtil {
    /**
     * Base64 Url 解码，UTF-8 字符集
     * @param raw 密文
     * @return 明文
     */
    private static String base64UrlDecode(String raw) {
        byte[] bs = Base64.getUrlDecoder().decode(raw.trim());
        return new String(bs, StandardCharsets.UTF_8);
    }

    /**
     * Base64 Basic 解码，UTF-8 字符集
     * @param cipher 密文
     * @return 明文
     */
    public static String base64Decode(String cipher) {
        String raw = cipher.trim();
        if(raw.contains("+")|| raw.contains("/") ||raw.contains("=")) {
            byte[] bs = Base64.getDecoder().decode(raw);
            return new String(bs, StandardCharsets.UTF_8);
        } else {
            byte[] bs = Base64.getUrlDecoder().decode(raw.trim());
            return new String(bs, StandardCharsets.UTF_8);
        }
    }

}
