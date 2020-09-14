package com.yizhishang.common.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.*;

import static org.springframework.beans.BeanUtils.getPropertyDescriptor;

/**
 * @author YUANYONGJUN993
 */
@Slf4j
public class PDFUtils {

    /**
     * 有四舍五入
     */
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###.0000");

    private static final String PATTERN = "yyy-MM-dd";

    private PDFUtils() {

    }

    /**
     * @param title 表格标题名
     * @param cls   javaBean
     * @param list  列表数据
     * @param path  目标路径
     */
    public static void export(String title, Class<?> cls, List<?> list, String path) {
        export(title, cls, list, path, PATTERN);
    }

    /**
     * @param title   表格标题名
     * @param cls     javaBean
     * @param list    列表数据
     * @param path    目标路径
     * @param pattern 如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
     */
    public static void export(String title, Class<?> cls, List<?> list, String path, String pattern) {

        // 定义A4页面大小
        Rectangle rectPageSize = new Rectangle(PageSize.A4);
        // 加上这句可以实现A4页面的横置
        rectPageSize = rectPageSize.rotate();

        // 其余4个参数，设置了页面的4个边距
        Document document = new Document(rectPageSize);
        // 添加文档元数据信息

        document.addTitle(title);
        document.addSubject("export information");
        document.addAuthor("yizhishang");
        document.addCreator("yizhishang");
        document.addKeywords("pdf itext");

        BaseFont baseFont = null;
        try {
            baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (DocumentException | IOException e) {
            log.error(e.getMessage(), e);
        }

        Font titleFont = new Font(baseFont, 20, Font.BOLD);
        Font docFont = new Font(baseFont, 10, Font.NORMAL);
        File file = new File(path);
        try (FileOutputStream out = new FileOutputStream(file)) {
            PdfWriter.getInstance(document, out);

            document.open();

            //设置title
            Paragraph header = new Paragraph(title, titleFont);
            header.setAlignment(1);
            document.add(header);

            //设置报告出具时间
            Calendar calendar = Calendar.getInstance();
            String timeStr = FastDateFormat.getInstance("yyyy 年 MM 月 dd 日").format(calendar.getTime());
            Paragraph time = new Paragraph("【报告出具时间：" + timeStr + " 】", docFont);
            time.setIndentationLeft(500);
            //设置段落前后间距
            time.setSpacingAfter(15);
            time.setSpacingBefore(15);

            document.add(time);
            PdfPTable table = getTable(cls, list, pattern, docFont);
            document.add(table);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            document.close();
        }
    }

    /**
     * 获取PDF表格
     *
     * @param cls
     * @param list
     * @param pattern
     * @param docFont
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws IOException
     * @throws DocumentException
     */
    private static PdfPTable getTable(Class<?> cls, List<?> list, String pattern, Font docFont) throws IllegalAccessException, InvocationTargetException, IOException, DocumentException {
        List<Object[]> annotationList = new ArrayList<>();

        List<Field> fieldList = new ArrayList<>();
        Class tempClass = cls;
        while (tempClass != null) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));

            //得到父类,然后赋给自己
            tempClass = tempClass.getSuperclass();
        }
        for (Field field : fieldList) {
            PDFField pdfField = field.getAnnotation(PDFField.class);
            if (pdfField == null) {
                continue;
            }
            addAnnotation(annotationList, pdfField, field);
        }

        annotationList.sort(Comparator.comparingInt(annotation -> ((PDFField) annotation[0]).order()));

        int listSize = annotationList.size();
        PdfPTable table = new PdfPTable(listSize);
        table.setWidthPercentage(Float.parseFloat((8 * listSize) + ""));

        //设置文档行
        List<PdfPRow> listRow = table.getRows();
        int[] widths = new int[listSize];

        // 产生表格标题行
        PdfPCell[] cellsHeader = new PdfPCell[listSize];
        PdfPRow rowHeader = new PdfPRow(cellsHeader);
        int i = 0;
        for (Object[] annotation : annotationList) {
            PDFField pdfField = (PDFField) annotation[0];
            String cellTitle = pdfField.title();
            if (StringUtils.isEmpty(cellTitle)) {
                Field field = (Field) annotation[1];
                cellTitle = field.getName();
            }
            widths[i] = pdfField.width();

            PdfPCell cell = new PdfPCell(new Paragraph(cellTitle, docFont));

            // 水平居中
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);

            //垂直居中
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cellsHeader[i++] = cell;
        }

        listRow.add(rowHeader);

        // 遍历集合数据，产生数据行
        for (Object obj : list) {
            PdfPCell[] cells = new PdfPCell[listSize];
            PdfPRow row = new PdfPRow(cells);
            i = 0;
            for (Object[] annotation : annotationList) {
                //region 设置单元格
                Field field = (Field) annotation[1];
                PDFField pdfField = (PDFField) annotation[0];

                PdfPCell cell = null;

                PropertyDescriptor pd = getPropertyDescriptor(cls, field.getName());

                Method method = pd.getReadMethod();

                Object value = method.invoke(obj);

                // 判断值的类型后进行强制类型转换
                String textValue = null;
                if (value == null) {
                    textValue = "";
                } else if (pdfField.enumClass().isEnum() && PdfAnnotationEnum.class.isAssignableFrom(pdfField.enumClass())) {
                    if (StringUtils.isEmpty(value.toString().trim())) {
                        textValue = "";
                    } else {
                        for (PdfAnnotationEnum annotationEnum : pdfField.enumClass().getEnumConstants()) {
                            if (annotationEnum.getCode().equals(Integer.parseInt(value.toString()))) {
                                textValue = annotationEnum.getName();
                                break;
                            }
                        }
                    }

                } else if (value instanceof Boolean) {
                    textValue = (Boolean) value ? "是" : "否";
                } else if (value instanceof Date) {
                    Date date = (Date) value;
                    FastDateFormat fastDateFormat = FastDateFormat.getInstance(pattern);
                    textValue = fastDateFormat.format(date);
                } else if (value instanceof BigDecimal) {
                    BigDecimal bd = (BigDecimal) value;
                    textValue = DECIMAL_FORMAT.format(bd);
                } else if (value instanceof byte[]) {
                    byte[] bsValue = (byte[]) value;
                    Image img = Image.getInstance(bsValue);
                    cell = new PdfPCell(img);
                } else {
                    textValue = value.toString();
                }

                // 如果不是图片数据,就当做文本处理
                if (textValue != null) {
                    cell = new PdfPCell(new Paragraph(textValue, docFont));
                }

                if (cell == null) {
                    throw new NullPointerException("cell is null");
                }

                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                //endregion
                cells[i++] = cell;
            }
            listRow.add(row);
        }

        table.setWidths(widths);

        return table;
    }

    /**
     * 添加到 annotationList
     */
    private static void addAnnotation(List<Object[]> annotationList, PDFField pdfField, Field field) {
        if (pdfField != null) {
            annotationList.add(new Object[]{pdfField, field});
        }
    }

}