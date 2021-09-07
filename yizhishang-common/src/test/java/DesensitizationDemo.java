import com.yizhishang.common.util.DesensitizationUtil;

/**
 * @author yizhishang
 * @Description : 加密字符串 替换* 号
 * @since 2020-07-03 15:31
 */
public class DesensitizationDemo {

    public static void main(String[] args) {
        String number = "13088840046";
        System.out.println(DesensitizationUtil.hideNumber(number));

        number = "420222199001058418";
        System.out.println(DesensitizationUtil.hideNumber(number));

        number = "6228480128149967373";
        System.out.println(DesensitizationUtil.hideNumber(number));

        number = "285206405@qq.com";
        System.out.println(DesensitizationUtil.hideMailbox(number));

        number = "袁永君";
        System.out.println(DesensitizationUtil.hideUserName(number));

        number = "湖北省黄石市阳新县发送到发斯蒂芬";
        System.out.println(DesensitizationUtil.hideAddress(number));

        number = "黄石市阳新县发送到发斯蒂芬";
        System.out.println(DesensitizationUtil.hideAddress(number));

        number = "浙江省新华书店有限公司";
        System.out.println(DesensitizationUtil.hideCompanyName(number));

        number = "顺丰控股集团有限公司";
        System.out.println(DesensitizationUtil.hideCompanyName(number));

        number = "深圳市顺丰控股集团有限公司";
        System.out.println(DesensitizationUtil.hideCompanyName(number));

    }


}
