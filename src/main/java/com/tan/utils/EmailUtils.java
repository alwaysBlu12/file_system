package com.tan.utils;

import java.util.regex.Pattern;

/**
 * 邮箱工具类
 */

public class EmailUtils {

    /**
     * 校验是否为有效的QQ邮箱格式
     *
     * @param email 邮箱地址字符串
     * @return 如果是有效的QQ邮箱格式返回true，否则返回false
     */
    public static boolean isValidQQEmail(String email) {
        // QQ邮箱正则表达式
        String qqEmailRegex = "^[1-9]\\d{4,10}@qq\\.com$";
        // 使用Pattern进行编译
        Pattern pattern = Pattern.compile(qqEmailRegex);
        // 使用matcher进行匹配
        return pattern.matcher(email).matches();
    }

    public static void main(String[] args) {
        // 测试邮箱地址
        String email1 = "123456789@qq.com";
        String email2 = "test.email@qq.com";
        String email3 = "test_email@qq.com";
        String email4 = "testemail@qq.com";
        String email5 = "testemail@163.com";

        System.out.println(email1 + " is a valid QQ email? " + isValidQQEmail(email1)); // true
        System.out.println(email2 + " is a valid QQ email? " + isValidQQEmail(email2)); // false
        System.out.println(email3 + " is a valid QQ email? " + isValidQQEmail(email3)); // false
        System.out.println(email4 + " is a valid QQ email? " + isValidQQEmail(email4)); // true
        System.out.println(email5 + " is a valid QQ email? " + isValidQQEmail(email5)); // false
    }
}
