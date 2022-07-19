package com.exp.oa.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Utils {
    public static String md5Digest(String source) {
        return DigestUtils.md5Hex(source);
    }

    /**
     * 对源数据简单的加盐混淆后生成MD5摘要
     * @param source 源数据
     * @param salt 盐值
     * @return MD5摘要
     */
    public static String md5Digest(String source, Integer salt) {
        char[] ca = source.toCharArray();
        for (int i = 0; i < ca.length; i++) {
            ca[i] = (char) (ca[i] + salt);
        }
        String target = new String(ca);
//        System.out.println(target);
        String md5 = DigestUtils.md5Hex(target);
        return md5;
    }

    public static void main(String[] args) {
        System.out.println(MD5Utils.md5Digest("test", 180));
        System.out.println(MD5Utils.md5Digest("test", 181));
        System.out.println(MD5Utils.md5Digest("test", 182));
        System.out.println(MD5Utils.md5Digest("test", 183));
        System.out.println(MD5Utils.md5Digest("test", 184));
        System.out.println(MD5Utils.md5Digest("test", 185));
        System.out.println(MD5Utils.md5Digest("test", 186));
        System.out.println(MD5Utils.md5Digest("test", 187));
        System.out.println(MD5Utils.md5Digest("test", 188));
        System.out.println(MD5Utils.md5Digest("test", 189));
    }
}
