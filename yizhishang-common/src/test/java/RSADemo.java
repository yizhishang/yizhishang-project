import com.yizhishang.common.util.RSAUtil;

/**
 * @author 袁永君
 * @date 2019/7/18 13:56
 */
public class RSADemo {

    static {
        System.out.println("RSA公钥Base64编码: " + RSAUtil.PUBLIC_KEY);
        System.out.println("RSA私钥Base64编码: " + RSAUtil.PRIVATE_KEY);
        System.out.println();
    }

    /**
     * 客户端发服务端
     */
    public void client2Server() {
        try {
            String message = "yizhishang";
            System.out.println("加密+解密---------------------------------------------------- 明文：" + message);

            String byte2Base64 = RSAUtil.publicEncrypt(message);
            System.out.println("客户端公钥加密并Base64编码的结果：" + byte2Base64);

            String content = RSAUtil.privateDecrypt(byte2Base64);
            System.out.println("服务端私钥解密: " + content);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务端回客户端
     */
    public void server2Client() {
        try {
            String message = "50.36";
            System.out.println("签名+验签---------------------------------------------------- 明文：" + message);

            String byte2Base64 = RSAUtil.privateEncrypt(message);
            System.out.println("服务端私钥签名：" + byte2Base64);

            String content = RSAUtil.publicDecrypt(byte2Base64);
            System.out.println("客户端公钥验签：" + content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RSADemo demo = new RSADemo();
        demo.client2Server();
        demo.server2Client();
    }
}
