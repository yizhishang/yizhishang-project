import com.yizhishang.common.util.AESUtil;

/**
 * @author 袁永君
 * @date 2019/7/18 13:56
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
