package util;

import com.yizhishang.common.util.AESUtil;

/**
 * @author: yizhishang
 * @date: 2021/9/6 9:56
 */
public class AESDemo {
    public static void main(String[] args) {
        String content = "hello,您好";
        String key = "今天是个好日子";
        System.out.println("content:" + content);
        String s1 = AESUtil.encrypt(content, key);
        System.out.println("s1: " + s1);
        System.out.println("s2: " + AESUtil.decrypt(s1, key));

    }

}
