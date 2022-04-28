package com.yizhishang.common.pdf.read;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yizhishang
 */
public class AnalysisUtil {

    private static final String selectBox = "☑";
    private static final String emptyBox = "□";

    /**
     * 提取固定字符之间的字符串
     *
     * @param source  源字符串
     * @param prefix  第一个固定字符串
     * @param postfix 第二个固定字符串,为null是提取到最后字符
     * @return
     */
    public static String getInnerString(String source, String prefix, String postfix) {
        if (StringUtils.isBlank(source)) {
            return source;
        }
        int startIdx = source.indexOf(prefix);
        if (startIdx < 0) {
            return "";
        }
        source = source.substring(startIdx + prefix.length()).trim();
        if (postfix == null) {
            return source;
        }
        int endIdx = source.indexOf(postfix);
        if (endIdx < 0) {
            return "";
        }
        return source.substring(0, endIdx);
    }

    public static Integer getInnerInteger(String source, String prefix, String postfix) {
        String result = getInnerString(source, prefix, postfix);
        if (StringUtils.isBlank(result)) {
            return null;
        }
        return Integer.valueOf(result);
    }

    public static Long getInnerLong(String source, String prefix, String postfix) {
        String result = getInnerString(source, prefix, postfix);
        if (StringUtils.isBlank(result)) {
            return null;
        }
        return Long.valueOf(result);
    }

    public static BigDecimal getInnerBigDecimal(String source, String prefix, String postfix) {
        String result = getInnerString(source, prefix, postfix);
        if (StringUtils.isBlank(result)) {
            return null;
        }
        return new BigDecimal(result);
    }

    public static Long getLong(String result) {
        if (result == null) {
            return null;
        }
        return Long.valueOf(result);
    }

    public static BigDecimal getBigDecimal(String result) {
        if (result == null) {
            return null;
        }
        return new BigDecimal(result);
    }

    private static SettlementRailwayBill.BaseInfo buildBaseInfoList(String pageText) {
        SettlementRailwayBill.BaseInfo baseInfo = new SettlementRailwayBill.BaseInfo();
        SettlementRailwayBill.UserInfo shipper = new SettlementRailwayBill.UserInfo();
        SettlementRailwayBill.UserInfo consignee = new SettlementRailwayBill.UserInfo();
        List<SettlementRailwayBill.CargoInfo> cargoInfoList = Lists.newArrayList();
        List<SettlementRailwayBill.CostInfo> costInfoList = Lists.newArrayList();


        // ”选择服务“ 选择的行数
        int costLineMark = 0;
        // 受票方地址电话
        String billReceiverAddressPhone = "";
        int addressPhoneLineMark = 0;

        // 箱货行标记
        int cargoLineMark = 0;
        // 合计行标记
        int totalLineMark = 0;

        List<String> costStr = Lists.newArrayList();

        String[] data;

        String[] lines = pageText.split("\\n");
        int i = 1;
        for (String line : lines) {
            // 0： 中国铁路成都局集团有限公司
            System.out.println(i + " " + line);
            switch (i) {
                case 1:
                case 20:
                case 19:
                case 5:
                case 9:
                case 12:
                case 16:
                    break;
                case 2:
                    // 运单号
                    String code = line.replace(" ", "").substring(4);
                    baseInfo.setCode(code);

                    break;
                case 3:
                    // 需求号 + 整车
                    line = line.substring(4);
                    data = line.split(" ");
                    String demandNumber = data[0];
                    baseInfo.setDemandNumber(demandNumber);
                    String transportType = data[1];
                    baseInfo.setTransportType(transportType);

                    break;
                case 4:
                    // 发站(公司) 攀枝花（成） 专用线 [47999002]攀枝花市大西南实业有限公司专用线 货区
                    line = line.replace(" ", "");
                    shipper.setNode(getInnerString(line, "发站(公司)", "专用线"));
                    shipper.setSpecialLine(getInnerString(line, "专用线", "货区"));
                    baseInfo.setCargoArea(getInnerString(line, "货区", null));

                    break;
                case 6:
                    // 经办人 陈华 货位
                    line = line.replace(" ", "");
                    shipper.setAgent(getInnerString(line, "经办人", "货位"));
                    baseInfo.setCargoLocation(getInnerString(line, "货位", null));

                    break;
                case 7:
                    // 托运人名称
                    data = line.split(" ");
                    shipper.setName(data[2]);
                    break;
                case 8:
                    // 手机号码 + 车种车号
                    shipper.setPhoneNumber(getInnerString(line, "手机号码 ", "车种车号"));
                    baseInfo.setCarSizeAndNumber(getInnerString(line, "车种车号 ", null));
                    break;
                case 10:
                    // □上门取货 取货地址 联系电话 取货里程(km)
                    line = line.replace(" ", "");
                    if (line.contains(selectBox)) {
                        shipper.setDoorToDoor("上门取货");
                    }
                    shipper.setAddress(getInnerString(line, "取货地址 ", "联系电话"));
                    shipper.setConcatNumber(getInnerString(line, "联系电话 ", "取货里程"));
                    shipper.setMileage(getInnerBigDecimal(line, "取货里程(km) ", null));

                    break;
                case 11:
                    // 到站(公司) 城厢（成） 专用线 运到期限 5 标重 70
                    line = line.replace(" ", "");
                    consignee.setNode(getInnerString(line, "到站(公司)", "专用线"));
                    consignee.setSpecialLine(getInnerString(line, "专用线", "运到期限"));
                    baseInfo.setTransportationTerm(getInnerInteger(line, "运到期限", "标重"));
                    baseInfo.setMarkWeight(getInnerBigDecimal(line, "标重", null));

                    break;
                case 13:
                    // 经办人 谢超 施封号
                    line = line.replace(" ", "");
                    consignee.setAgent(getInnerString(line, "经办人", "施封号"));
                    baseInfo.setSealNumber(getInnerString(line, "施封号", null));

                    break;
                case 14:
                    // 收货人名称
                    data = line.split(" ");
                    consignee.setName(data[1]);
                    break;
                case 15:
                    // 手机号码 篷布号
                    line = line.replace(" ", "");
                    consignee.setPhoneNumber(getInnerString(line, "手机号码", "篷布号"));
                    baseInfo.setTarpaulinNumber(getInnerString(line, "篷布号", null));

                    break;
                case 17:
                    // □上门送货 送货地址 联系电话 送货里程(km)
                    line = line.replace(" ", "");
                    if (line.contains(selectBox)) {
                        consignee.setDoorToDoor("上门送货");
                    }
                    consignee.setAddress(getInnerString(line, "送货地址 ", "联系电话"));
                    consignee.setConcatNumber(getInnerString(line, "联系电话 ", "送货里程"));
                    consignee.setMileage(getInnerBigDecimal(line, "送货里程(km) ", null));

                    break;
                case 18:
                    // 付费方式 □电子 □现金 □支票 □银行卡 ☑预付款 □汇总支付 领货方式 ☑电子领货 □纸质领货 装车方 货主 施封方 无
                    line = line.replace(" ", "");

                    // 付费方式
                    String paymentWay = getInnerString(line, "付费方式", "领货方式");
                    baseInfo.setPaymentWay(getInnerString(paymentWay, selectBox, emptyBox));

                    // 领货方式
                    String receiveCargoWay = getInnerString(line, "领货方式", "装车方");
                    baseInfo.setReceiveCargoWay(getInnerString(receiveCargoWay, selectBox, emptyBox));

                    // 装车方 施封方
                    baseInfo.setLoader(getInnerString(line, "装车方", "施封方"));
                    baseInfo.setSealer(getInnerString(line, "施封方", null));

                    break;
                case 21:
                    // 下一行可能是箱货做个标记
                    cargoLineMark = 22;
                    break;
                default:
                    if (i == cargoLineMark && !line.startsWith("合计")) {
                        data = line.split(" ");

                        // 货物 + 箱号
                        SettlementRailwayBill.CargoInfo cargoInfo = new SettlementRailwayBill.CargoInfo();
                        cargoInfo.setName(data[0]);

                        // 有箱号
                        if (line.contains("TB")) {
                            String boxNumber = line.substring(line.indexOf("TB"));
                            boxNumber = boxNumber.split(" ")[0];
                            cargoInfo.setBoxNumber(boxNumber);
                        }
                        cargoInfo.setWaybillCode(baseInfo.getCode());
                        cargoInfoList.add(cargoInfo);
                        cargoLineMark++;
                        break;
                    }
                    // 可能是箱货，可能不是
                    if (line.startsWith("合计")) {
                        totalLineMark = i;
                        data = line.split(" ");
                        // 件数合计 货物价格合计 重量合计 承运人确定重量合计
                        baseInfo.setCargoQuantity(getLong(data[1]));
                        baseInfo.setCargoPrice(getBigDecimal(data[2]));
                        baseInfo.setCargoWeight(getBigDecimal(data[3]));
                        baseInfo.setCargoDeterminedWeight(getBigDecimal(data[4]));

                        break;
                    }
                    // 解析费用信息
                    if (line.contains("上门卸车") && line.length() > 6) {
                        line = line.substring(6);

                        costStr.add(line);
                        costLineMark = i + 2;
                        break;
                    }

                    // 费用项
                    if (costLineMark == i && !line.contains("保价运输") && !line.startsWith("其他服务")) {
                        costStr.add(line);
                        break;
                    }

                    if (line.contains("装载加固材料")) {
                        costLineMark = i + 1;
                        break;
                    }

                    // 受票方名称
                    if (line.startsWith("受票方名称")) {
                        baseInfo.setBillReceiverName(getInnerString(line, "受票方名称：", null));
                    }
                    // 受票方识别号
                    if (line.startsWith("识别号")) {
                        baseInfo.setBillReceiverId(line.substring(4));
                    }
                    // 受票方地址电话
                    if (line.startsWith("地址电话")) {
                        billReceiverAddressPhone = line.substring(5);
                        addressPhoneLineMark = i;
                    }
                    if (i == addressPhoneLineMark + 2) {
                        billReceiverAddressPhone += line;
                        baseInfo.setBillReceiverName(billReceiverAddressPhone);
                    }
                    // 受票方开户行及账号
                    if (line.startsWith("开户行及账号")) {
                        String billReceiverBankAccount = getInnerString(line, "开户行及账号：", null);
                        baseInfo.setBillReceiverBankAccount(billReceiverBankAccount);
                    }
                    // 费用合计 大写
                    if (line.contains("大写")) {
                        data = line.split(" ");
                        baseInfo.setTotalCost(getBigDecimal(data[2]));
                        baseInfo.setTotalCostCn(data[4].substring(2));
                    }
                    // 发票类型
                    if (line.contains("普通票") && line.contains(selectBox)) {
                        baseInfo.setVatInvoiceType("普通票");
                    }
                    if (line.contains("专用票") && line.contains(selectBox)) {
                        baseInfo.setVatInvoiceType("专用票");
                    }
                    // 托运人记事+承运人记事  内容模糊，不解析

                    if (line.contains("制单日期")) {
                        String date = getInnerString(line, "制单日期：", null);
                        baseInfo.setBillCreatedDate(DateUtil.parseDate(date));
                    }
                    break;
            }
            i++;
        }

        shipper.setWaybillCode(baseInfo.getCode());
        consignee.setWaybillCode(baseInfo.getCode());
        // 解析费用项
        analysisCost(costInfoList, costStr, baseInfo.getCode());


        System.out.println("***********************************************************************");
        System.out.println("***********************************************************************");
        System.out.println("***********************************************************************");
        System.out.println("##################### 运单号 " + baseInfo.getCode());

        System.out.println("##################### 需求号 " + baseInfo.getDemandNumber());
        System.out.println("##################### 整车 " + baseInfo.getTransportType());
        System.out.println("##################### 发站(公司) " + shipper.getNode());
        System.out.println("##################### 专用线 " + shipper.getSpecialLine());
        System.out.println("##################### 货区 " + baseInfo.getCargoArea());
        System.out.println("##################### 经办人: " + shipper.getAgent());
        System.out.println("##################### 货位: " + baseInfo.getCargoLocation());
        System.out.println("##################### 托运人名称 " + shipper.getName());

        System.out.println("##################### 发货人手机号码 " + shipper.getPhoneNumber());
        System.out.println("##################### 车种车号 " + baseInfo.getCarSizeAndNumber());

        System.out.println("##################### 发货人 上门取货 " + shipper.getDoorToDoor());
        System.out.println("##################### 发货人 取货地址 " + shipper.getAddress());
        System.out.println("##################### 发货人 联系电话 " + shipper.getConcatNumber());
        System.out.println("##################### 发货人 取货里程 " + shipper.getMileage());

        System.out.println("##################### 到站(公司)" + consignee.getNode());
        System.out.println("##################### 专用线" + consignee.getSpecialLine());
        System.out.println("##################### 运到期限" + baseInfo.getTransportationTerm());
        System.out.println("##################### 标重" + baseInfo.getMarkWeight());
        System.out.println("##################### 收货经办人 " + consignee.getAgent());
        System.out.println("##################### 施封号 " + baseInfo.getSealNumber());
        System.out.println("##################### 收货人名称 " + consignee.getName());

        System.out.println("##################### 收货手机号码 " + consignee.getPhoneNumber());
        System.out.println("##################### 篷布号 " + baseInfo.getTarpaulinNumber());
        System.out.println("##################### 上门送货: " + consignee.getDoorToDoor());
        System.out.println("##################### 送货地址: " + consignee.getAddress());
        System.out.println("##################### 联系电话: " + consignee.getConcatNumber());
        System.out.println("##################### 送货里程: " + consignee.getMileage());
        System.out.println("##################### 付费方式 " + baseInfo.getPaymentWay());
        System.out.println("##################### 领货方式 " + baseInfo.getReceiveCargoWay());
        System.out.println("##################### 装车方 " + baseInfo.getLoader());
        System.out.println("##################### 施封方 " + baseInfo.getSealer());

        cargoInfoList.forEach(cargoInfo -> {
            System.out.println("##############货物名称: " + cargoInfo.getName());
            System.out.println("##############箱号: " + cargoInfo.getBoxNumber());
        });

        System.out.println("############## 件数合计: " + baseInfo.getCargoQuantity());
        System.out.println("############## 货物价格合计: " + baseInfo.getCargoPrice());
        System.out.println("############## 重量合计: " + baseInfo.getCargoWeight());
        System.out.println("############## 承运人确定重量合计: " + baseInfo.getCargoDeterminedWeight());

        System.out.println("############# 费用合计 " + baseInfo.getTotalCost());
        System.out.println("############# 大写 " + baseInfo.getTotalCostCn());
        System.out.println("##################### 制单日期 " + baseInfo.getBillCreatedDate());
        System.out.println("费用项信息############");
        costInfoList.forEach(System.out::println);
        System.out.println("费用项信息############");
        return baseInfo;
    }

    public static void analysis(List<String> page) {
        List<SettlementRailwayBill.BaseInfo> baseInfoList = Lists.newArrayList();
        for (String pageText : page) {
            baseInfoList.add(buildBaseInfoList(pageText));
        }
    }

    /**
     * 解析费用项信息
     *
     * @param costInfoList 费用项list
     * @param costStr      费用项拼接字符串
     * @param waybillCode  运单号
     */
    private static void analysisCost(List<SettlementRailwayBill.CostInfo> costInfoList, List<String> costStr, String waybillCode) {
        String[] data;
        for (String line : costStr) {
            data = line.split(" ");
            // 运单号 费目 金额 税额
            SettlementRailwayBill.CostInfo costInfo = new SettlementRailwayBill.CostInfo();
            costInfo.setWaybillCode(waybillCode);
            costInfo.setCostItem(data[0]);
            costInfo.setPrice(getBigDecimal(data[1]));
            costInfo.setTaxAmount(getBigDecimal(data[2]));
            costInfoList.add(costInfo);
            if (data.length > 4) {
                costInfo = new SettlementRailwayBill.CostInfo();
                costInfo.setWaybillCode(waybillCode);
                costInfo.setCostItem(data[3]);
                costInfo.setPrice(getBigDecimal(data[4]));
                costInfo.setTaxAmount(getBigDecimal(data[5]));
                costInfoList.add(costInfo);
            }
        }
    }

}
