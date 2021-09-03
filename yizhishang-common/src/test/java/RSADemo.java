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
            /**
             * ===============生成公钥和私钥，公钥传给客户端，私钥服务端保留==================
             */
            System.out.println("客户端（公钥加密）发送给服务端（私钥解密）----------------------------------------------------");

            String message = "yizhishang";
            // 加密后的内容Base64编码
            String byte2Base64 = RSAUtil.publicEncrypt(message);
            System.out.println("公钥加密并Base64编码的结果：" + byte2Base64);
            // ############## 网络上传输的内容有Base64编码后的公钥 和 Base64编码后的公钥加密的内容 #################

            // ===================服务端================
            // 用私钥解密
            String content = RSAUtil.privateDecrypt(byte2Base64);
            // 解密后的明文
            System.out.println("解密后的明文: " + content);
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
            // ===============生成公钥和私钥，公钥传给客户端，私钥服务端保留==================
            System.out.println("服务端（私钥签名）发给客户端（公钥验签）----------------------------------------------------");

            // =================服务端=================
            String message = "50.36";
            // 将Base64编码后的私钥转换成PrivateKey对象
            // 用私钥签名
            // 加密后的内容Base64编码
            String byte2Base64 = RSAUtil.privateEncrypt(message);
            System.out.println("私钥签名：" + byte2Base64);

            // ############## 网络上传输的内容有 Base64编码后的私钥签名的内容 #################

            // ===================客户端================
            // 用公钥验签
            String content = RSAUtil.publicDecrypt(byte2Base64);
            System.out.println("公钥验签：" + content);
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
