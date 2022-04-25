package com.yizhishang.common.pdf.read;

import com.google.common.collect.Lists;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ReadPdf {
    public static void main(String[] args) {
        String inputPath = "F:\\gitee\\yizhishang-project\\docs\\铁路大票.pdf";
        String outputPath = "F:\\gitee\\yizhishang-project\\docs\\铁路大票.txt";
        readPdf(inputPath, outputPath);

//        readPdfByPage(inputPath);
//        String result = readPdfByPage(inputPath);

    }

    public void analysis(String result) {
        String[] pages = result.split("\nPDF解析分页\n");

        SettlementRailwayBill.BaseInfo baseInfo = new SettlementRailwayBill.BaseInfo();
        SettlementRailwayBill.UserInfo shipper = new SettlementRailwayBill.UserInfo();
        SettlementRailwayBill.UserInfo consignee = new SettlementRailwayBill.UserInfo();
        List<SettlementRailwayBill.CargoInfo> cargoInfoList = Lists.newArrayList();

        String end = "@yizhishang";

        String selectBox = "☑";

        String[] data;
        for (String page : pages) {
//            String[] lines = page.split("\\r?\\n");
            String[] lines = page.split("\\n");
            int i = 0;
            for (String line : lines) {
                // 0： 中国铁路成都局集团有限公司
                System.out.println(i + " " + line);
                switch (i) {
                    case 1:
                        // 运单号
                        String code = line.replace(" ", "").substring(4);
                        baseInfo.setCode(code);
                        break;
                    case 2:
                        // 需求号 + 整车
                        line = line.substring(4);
                        data = line.split(" ");
                        String demandNumber = data[0];
                        baseInfo.setDemandNumber(demandNumber);
                        String transportType = data[1];
                        baseInfo.setTransportType(transportType);
//                        System.out.println("#####################" + demandNumber);
//                        System.out.println("#####################" + transportType);
                        break;
                    case 6:
                        // 发站(公司) 攀枝花（成） 专用线 [47999002]攀枝花市大西南实业有限公司专用线 货区
                        line = line.replace(" ", "") + end;
                        shipper.setNode(getInnerString(line, "发站(公司)", "专用线"));
                        shipper.setSpecialLine(getInnerString(line, "专用线", "货区"));
                        baseInfo.setCargoArea(getInnerString(line, "货区", end));

//                        System.out.println("#####################" + shipper.getNode());
//                        System.out.println("#####################" + shipper.getSpecialLine());
//                        System.out.println("#####################" + baseInfo.getCargoArea());
                        break;
                    case 7:
                        // 托运人名称
                        data = line.split(" ");
                        shipper.setName(data[1]);
//                        System.out.println("#####################" + shipper.getName());
                        break;
                    case 8:
                        // 经办人 陈华 货位
                        line = line.replace(" ", "") + end;
                        shipper.setAgent(getInnerString(line, "经办人", "货位"));
                        baseInfo.setCargoLocation(getInnerString(line, "货位", end));

//                        System.out.println("#####################" + shipper.getAgent());
//                        System.out.println("#####################" + baseInfo.getCargoLocation());
                        break;
                    case 9:
                        // 手机号码 + 车种车号
                        line += end;
                        shipper.setPhoneNumber(getInnerString(line, "手机号码 ", "车种车号"));
                        baseInfo.setCarSizeAndNumber(getInnerString(line, "车种车号 ", end));

//                        System.out.println("#####################" + shipper.getPhoneNumber());
//                        System.out.println("#####################" + baseInfo.getCarSizeAndNumber());
                        break;
                    case 10:
                        // □上门取货 取货地址 联系电话 取货里程(km)
                        line = line.replace(" ", "") + end;
                        if (line.contains(selectBox)) {
                            shipper.setDoorToDoor("上门取货");
                        }
                        shipper.setAddress(getInnerString(line, "取货地址 ", "联系电话"));
                        shipper.setConcatNumber(getInnerString(line, "联系电话 ", "取货里程"));
                        shipper.setMileage(getInnerBigDecimal(line, "取货里程(km) ", end));

//                        System.out.println("#####################" + shipper.getDoorToDoor());
//                        System.out.println("#####################" + shipper.getAddress());
//                        System.out.println("#####################" + shipper.getConcatNumber());
//                        System.out.println("#####################" + shipper.getMileage());
                        break;
                    case 14:
                        // 到站(公司) 城厢（成） 专用线 运到期限 5 标重 70
                        line = line.replace(" ", "") + end;
                        consignee.setNode(getInnerString(line, "到站(公司)", "专用线"));
                        consignee.setSpecialLine(getInnerString(line, "专用线", "运到期限"));
                        baseInfo.setTransportationTerm(getInnerInteger(line, "货区", end));
                        baseInfo.setMarkWeight(getInnerBigDecimal(line, "货区", end));

//                        System.out.println("#####################" + consignee.getNode());
//                        System.out.println("#####################" + consignee.getSpecialLine());
//                        System.out.println("#####################" + baseInfo.getTransportationTerm());
//                        System.out.println("#####################" + baseInfo.getMarkWeight());
                        break;
                    case 15:
                        // 收货人名称
                        data = line.split(" ");
                        consignee.setName(data[1]);
//                        System.out.println("#####################" + consignee.getName());
                        break;
                    case 16:
                        // 经办人 谢超 施封号
                        line = line.replace(" ", "") + end;
                        consignee.setAgent(getInnerString(line, "经办人", "施封号"));
                        baseInfo.setSealNumber(getInnerString(line, "施封号", end));

//                        System.out.println("#####################" + consignee.getAgent());
//                        System.out.println("#####################" + baseInfo.getSealNumber());
                        break;
                    case 17:
                        // 手机号码 篷布号
                        line = line.replace(" ", "") + end;
                        consignee.setPhoneNumber(getInnerString(line, "手机号码", "篷布号"));
                        baseInfo.setTarpaulinNumber(getInnerString(line, "篷布号", end));

//                        System.out.println("#####################" + consignee.getPhoneNumber());
//                        System.out.println("#####################" + baseInfo.getTarpaulinNumber());
                        break;
                    case 18:
                        // □上门送货 送货地址 联系电话 送货里程(km)
                        line = line.replace(" ", "") + end;
                        if (line.contains(selectBox)) {
                            consignee.setDoorToDoor("上门送货");
                        }
                        consignee.setAddress(getInnerString(line, "送货地址 ", "联系电话"));
                        consignee.setConcatNumber(getInnerString(line, "联系电话 ", "送货里程"));
                        consignee.setMileage(getInnerBigDecimal(line, "送货里程(km) ", end));

//                        System.out.println("#####################" + consignee.getDoorToDoor());
//                        System.out.println("#####################" + consignee.getAddress());
//                        System.out.println("#####################" + consignee.getConcatNumber());
//                        System.out.println("#####################" + consignee.getMileage());
                        break;
                    case 19:
                        // 付费方式 □电子 □现金 □支票 □银行卡 ☑预付款 □汇总支付 领货方式 ☑电子领货 □纸质领货 装车方 货主 施封方 无
                        line = line.replace(" ", "") + end;
                        // TODO 付费方式
                        baseInfo.setPaymentWay(getInnerString(line, "付费方式", "领货方式"));
                        // TODO 领货方式
                        baseInfo.setReceiveCargoWay(getInnerString(line, "领货方式", "装车方"));
                        baseInfo.setLoader(getInnerString(line, "装车方", "施封方"));
                        baseInfo.setSealer(getInnerString(line, "施封方", end));

//                        System.out.println("#####################" + baseInfo.getPaymentWay());
//                        System.out.println("#####################" + baseInfo.getReceiveCargoWay());
//                        System.out.println("#####################" + baseInfo.getLoader());
//                        System.out.println("#####################" + baseInfo.getSealer());
                        break;
                    case 33:

                        break;
                    default:
                        break;
                }
                i++;
            }

        }
    }

    public static void readPdf(String inputPath, String outputPath) {
        try (PDDocument document = PDDocument.load(new File(inputPath))) {

            if (!document.isEncrypted()) {

                PDFTextStripperByArea stripper = new PDFTextStripperByArea();

                stripper.setSortByPosition(true);

                PDFTextStripper tStripper = new PDFTextStripper();

                String pdfFileInText = tStripper.getText(document);

//                String[] lines = pdfFileInText.split("\\r?\\n");
                String[] lines = pdfFileInText.split("\\n");

                FileWriter fw = new FileWriter(outputPath);

                for (String line : lines) {
                    System.out.println(line);
                    fw.write(line + "\n");
                }

                fw.close();

                document.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * PDF路径
     * 每页之间分割字符串：“PDF解析第\d+页”
     *
     * @param fileName
     * @return
     */
    public static String readPdfByPage(String fileName) {
        String result = "";
        File file = new File(fileName);
        try (FileInputStream in = new FileInputStream(fileName)) {
            // 新建一个PDF解析器对象
            PDFParser parser = new PDFParser(new RandomAccessFile(file, "rw"));
            // 对PDF文件进行解析
            parser.parse();
            // 获取解析后得到的PDF文档对象
            PDDocument document = parser.getPDDocument();
            int size = document.getNumberOfPages();
            // 新建一个PDF文本剥离器
            PDFTextStripper stripper = new PDFTextStripper();
            //sort:设置为true则按照行进行读取，默认是false
            stripper.setSortByPosition(false);
            //一页一页读取
            for (int i = 1; i <= 1; i++) {
                // 设置起始页
                stripper.setStartPage(i);
                // 设置结束页
                stripper.setEndPage(i);
                // 从PDF文档对象中剥离文本
                String pageStr = stripper.getText(document).trim();
                result = result + pageStr + "\nPDF解析分页\n";
            }

        } catch (Exception e) {
            System.out.println("读取PDF文件" + file.getAbsolutePath() + "生失败！" + e);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 提取固定字符之间的字符串
     *
     * @param source
     * @param prefix
     * @param postfix
     * @return
     */
    public static String getInnerString(String source, String prefix, String postfix) {
        if (isBlank(source)) {
            return source;
        }
        int startIdx = source.indexOf(prefix);
        int endIdx = source.indexOf(postfix);
        if (startIdx < 0) {
            return "";
        }
        if (endIdx < 0) {
            return "";
        }
        return source.substring(startIdx, endIdx).substring(prefix.length()).trim();
    }

    public static Integer getInnerInteger(String source, String prefix, String postfix) {
        String result = getInnerString(source, prefix, postfix);
        if (isBlank(result)) {
            return null;
        }
        return Integer.valueOf(result);
    }

    public static BigDecimal getInnerBigDecimal(String source, String prefix, String postfix) {
        String result = getInnerString(source, prefix, postfix);
        if (isBlank(result)) {
            return null;
        }
        return new BigDecimal(result);
    }
}