package com.nancal.api.utils;


import com.nancal.common.enums.BopExportEnum;
import com.nancal.remote.vo.MoreFieldEntryVo;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author hewei
 * @date 2022/4/25 15:24
 * @Description
 */
public class ExportExcelUtil {

    /**
     * ebom导出
     * @param response
     * @param mapList
     * @param titles
     * @param fileName
     */
    @SneakyThrows
    public static void ExportExcels(HttpServletResponse response, List<Map<String,Object>> mapList, List<String> titles, String fileName) {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet();
        CellStyle cellStyle1 = excel(sheet,wb,titles);
        AtomicInteger rowNumber = new AtomicInteger(1);
        AtomicInteger number = new AtomicInteger(1);
        mapList.forEach(map ->{
            final Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            int cellNumber = 1;
            Row rowData = sheet.createRow(rowNumber.get());
            Cell name;
            name = rowData.createCell(0);
            name.setCellValue(number.toString());
            while (iterator.hasNext()){
                final Map.Entry<String, Object> next = iterator.next();
                name = rowData.createCell(cellNumber);
                if (null != next.getValue()){
                    name.setCellValue(next.getValue().toString());
                }else {
                    name.setCellValue("");
                }
                cellNumber=cellNumber+1;
            }
            number.addAndGet(1);
            name.setCellStyle(cellStyle1);

            rowNumber.addAndGet(1);
        });
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        fileName +="-"+LocalDateTime.now().format(dateTimeFormatter)+".xlsx";
        fileName = fileName.trim();
        //导出word
        OutputStream ous = null;
        InputStream ins = null;
        String path = System.getProperty("user.dir")+fileName;
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        wb.write(fileOutputStream);
        File file = new File(path);
        try {
            ins = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[ins.available()];
            ins.read(buffer);
            response.reset();
            response.setHeader("fileName", URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition,fileName");
            response.setHeader("Content-Disposition",  "attachment;filename="+URLEncoder.encode(fileName, "UTF-8"));
            response.addHeader("Content-Length", "" + file.length());
            ous = new BufferedOutputStream(response.getOutputStream());

            ous.write(buffer);
            ous.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                wb.close();
                fileOutputStream.close();
                ous.close();
                ins.close();
                file.delete();

                //删除文件
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 工艺规划导出显示列
     * @param response
     * @param lists
     * @param titles
     * @param fileName
     */
    @SneakyThrows
    public static void BopFieldExcels(HttpServletResponse response,List<List<String>> lists, List<String> titles, String fileName) {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet();
        CellStyle cellStyle1 = excel(sheet,wb,titles);
        AtomicInteger rowNumber = new AtomicInteger(1);
        lists.forEach(list ->{
            AtomicInteger cellNumber = new AtomicInteger(0);
            Row rowData = sheet.createRow(rowNumber.get());
            AtomicReference<Cell> name = new AtomicReference<>();
            name.set(rowData.createCell(0));
            list.forEach(l ->{
                name.set(rowData.createCell(cellNumber.get()));
                if (null != l){
                    name.get().setCellValue(l);
                }else {
                    name.get().setCellValue("");
                }
                cellNumber.set(cellNumber.get() + 1);

            });
            name.get().setCellStyle(cellStyle1);

            rowNumber.addAndGet(1);
        });
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        fileName +="-"+LocalDateTime.now().format(dateTimeFormatter)+".xlsx";
        fileName = fileName.trim();
        //导出word
        OutputStream ous = null;
        InputStream ins = null;
        String path = System.getProperty("user.dir")+fileName;
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        wb.write(fileOutputStream);
        File file = new File(path);
        try {
            ins = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[ins.available()];
            ins.read(buffer);
            response.reset();
            response.setHeader("fileName", URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition,fileName");
            response.setHeader("Content-Disposition",  "attachment;filename="+URLEncoder.encode(fileName, "UTF-8"));
            response.addHeader("Content-Length", "" + file.length());
            ous = new BufferedOutputStream(response.getOutputStream());

            ous.write(buffer);
            ous.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                wb.close();
                fileOutputStream.close();
                ous.close();
                ins.close();
                file.delete();

                //删除文件
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static CellStyle excel(Sheet sheet,Workbook wb, List<String> tableHost ){

        Row row = sheet.createRow(0);
        CellRangeAddress cellAddresses = CellRangeAddress.valueOf("A:N");
        sheet.setAutoFilter(cellAddresses);
        //给单元格设置样式
        CellStyle cellStyle = wb.createCellStyle();
        Font font = wb.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short) 11);
        //设置字体加粗
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setFontName("宋体");
        //给字体设置样式
        cellStyle.setFont(font);

        //设置单元格背景颜色
        cellStyle.setFillForegroundColor((short)23);
        //设置单元格填充样式(使用纯色背景颜色填充)
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        for (int i = 0; i < tableHost.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(tableHost.get(i));
            cell.setCellStyle(cellStyle);
            //设置列的宽度
            switch (i){
                case 0:
                case 1:
                case 5:
                case 6:
                case 10:
                case 11:
                default:
                    sheet.setColumnWidth(i, 120*50);
            }

        }
        CellStyle cellStyle1 = wb.createCellStyle();
        Font font1 = wb.createFont();
        //设置字体大小
        font1.setFontHeightInPoints((short) 9);
        //设置字体加粗
        font1.setFontName("宋体");
        font1.setColor(IndexedColors.BLACK.getIndex());
        cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
        //给字体设置样式
        cellStyle1.setFont(font1);
        return cellStyle1;
    }

    /**
     * bop导出模板数据
     * @param response
     * @param required
     * @param titles
     * @param fileName
     */
    @SneakyThrows
    public static void customExportExcels(HttpServletResponse response,List<String> required, List<String> titles, String fileName) {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet();
        customExportHead(0,sheet,fileName,wb,required,titles);
        fileName +=".xlsx";
        fileName = fileName.trim();
        //导出word
        OutputStream ous = null;
        InputStream ins = null;
        String path = System.getProperty("user.dir")+fileName;
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        wb.write(fileOutputStream);
        File file = new File(path);
        try {
            ins = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[ins.available()];
            ins.read(buffer);
            response.reset();
            response.setHeader("fileName", URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition,fileName");
            response.setHeader("Content-Disposition",  "attachment;filename="+URLEncoder.encode(fileName, "UTF-8"));
            response.addHeader("Content-Length", "" + file.length());
            ous = new BufferedOutputStream(response.getOutputStream());

            ous.write(buffer);
            ous.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                wb.close();
                fileOutputStream.close();
                ous.close();
                ins.close();
                file.delete();

                //删除文件
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static CellStyle customExportHead(int size, Sheet sheet,String headName, Workbook wb,List<String> required, List<String> titles){
        CellStyle cellStyle1 = wb.createCellStyle();
        do {
            Row row = sheet.createRow(size);
            //给单元格设置样式
            CellStyle cellStyle = wb.createCellStyle();
            // 顶边栏
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
            // 右边栏
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
            // 底边栏
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            // 左边栏
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());

            cellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
            //设置单元格背景颜色
            cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_GREEN.getIndex());
            // 填充样式
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = wb.createFont();
            //设置字体大小
            font.setFontHeightInPoints((short) 11);
            //设置字体加粗
            font.setBold(true);
            font.setColor(IndexedColors.BLACK.getIndex());
            font.setFontName("宋体");
            //给字体设置样式
            cellStyle.setFont(font);

            switch (size){
                case  0:
                    CellRangeAddress cra = new CellRangeAddress(0, 2, 0, 0);
                    sheet.addMergedRegion(cra);
                    Cell cell2 = row.createCell(0);
                    cell2.setCellValue("序号");
                    cell2.setCellStyle(cellStyle);
                    Cell cell1 = row.createCell(1);
                    cell1.setCellValue(headName);
                    cell1.setCellStyle(cellStyle);
                    CellRangeAddress cra1 = new CellRangeAddress(0, 0, 1, required.size());
                    sheet.addMergedRegion(cra1);
                    break;
                case 1:
                    for (int i = 0; i < required.size(); i++) {
                        Cell cell = row.createCell(i+1);
                        cell.setCellValue(required.get(i));
                        cell.setCellStyle(cellStyle);
                        sheet.setColumnWidth(i+1, 90*60);
                    }
                    break;
                case 2:

                    for (int i = 0; i < titles.size()+1; i++) {
                        if (i == 0){
                            Cell cell3 = row.createCell(0);
                            cell3.setCellValue("序号");
                            cell3.setCellStyle(cellStyle);
                        }else {
                            Cell cell = row.createCell(i );
                            cell.setCellValue(titles.get(i-1));
                            cell.setCellStyle(cellStyle);
                            sheet.setColumnWidth(i, 90 * 60);
                        }
                    }
                    break;
                default:
                    break;
            }
            size++;
        }while (size != 3);
        return cellStyle1;
    }

    /**
     * bop导出模板
     * @param response
     * @param fileName
     */
    @SneakyThrows
    public static void customBopExportExcelsTemplate(HttpServletResponse response,Map<String,List<MoreFieldEntryVo>> map, String fileName,int rowNumber) {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet();
        customBopExportHead(0,sheet,"工艺规划导出(*为下拉选项,#为数字选项,&为单选选项)",wb,map, rowNumber);
        //导出word
        fileName +=".xlsx";
        String path = System.getProperty("user.dir")+File.separator+fileName;
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        wb.write(fileOutputStream);
        //导出word
        OutputStream ous = null;
        InputStream ins = null;

        File file = new File(path);
        try {
            ins = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[ins.available()];
            ins.read(buffer);
            response.reset();
            response.setHeader("fileName", URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition,fileName");
            response.setHeader("Content-Disposition",  "attachment;filename="+URLEncoder.encode(fileName, "UTF-8"));
            response.addHeader("Content-Length", "" + file.length());
            ous = new BufferedOutputStream(response.getOutputStream());

            ous.write(buffer);
            ous.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                wb.close();
                ous.close();
                ins.close();
                fileOutputStream.flush();
                fileOutputStream.close();
                file.delete();
                //删除文件
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @SneakyThrows
    public static File customBopExportExcels(Map<String,List<MoreFieldEntryVo>> map, String fileName,int rowNumber,List<List<String>> lists) {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet();
        customBopExportHead(0,sheet,fileName,wb,map, rowNumber);
        for (int i = 0; i < lists.size(); i++){
            Row row = sheet.createRow(i + 4);
             List<String> values = lists.get(i);
             for (int j = 0; j < values.size(); j++){
                 Cell cell = row.createCell(j);
                 cell.setCellValue(values.get(j));
             }
        }
        //导出word
        String path = System.getProperty("user.dir")+File.separator+"工艺规划.xlsx";
        FileOutputStream fileOutputStream = new FileOutputStream(path);
        wb.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        return new File(path);

    }





    public static void customBopExportHead(int size, Sheet sheet,String headName, Workbook wb,Map<String,List<MoreFieldEntryVo>> map,int rowNumber){
        do {
            DataValidationHelper helper = sheet.getDataValidationHelper();
            //设置下拉框数据
            DataValidationConstraint constraint = helper.createExplicitListConstraint(new String[]{"工厂工艺", "线体工艺", "工位工艺", "工序", "工步", "辅材", "设备", "量具", "工夹具", "设计零件", "工位", "虚拟零件"});
            //设置生效的起始行、终止行、起始列、终止列
            CellRangeAddressList addressList = new CellRangeAddressList(4,10000,3,3);
            DataValidation validation = helper.createValidation(constraint,addressList);
            if(validation instanceof HSSFDataValidation){
                validation.setSuppressDropDownArrow(false);
            }else{
                validation.setSuppressDropDownArrow(true);
                validation.setShowErrorBox(true);
            }
            sheet.addValidationData(validation);
            // 设置单元格样式
            Row row = sheet.createRow(size);
            //给单元格设置统一样式
            CellStyle unifyCellStyle = wb.createCellStyle();

            /**
             * 设置单元格字体样式
             */
            Font font = wb.createFont();
            //设置字体大小
            font.setFontHeightInPoints((short) 11);
            //设置字体加粗
            font.setBold(true);
            font.setColor(IndexedColors.BLACK.getIndex());
            font.setFontName("宋体");
            //给字体设置样式
            unifyCellStyle.setFont(font);
            // 顶边栏
            unifyCellStyle.setBorderTop(BorderStyle.THIN);
            unifyCellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
            // 右边栏
            unifyCellStyle.setBorderRight(BorderStyle.THIN);
            unifyCellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
            // 底边栏
            unifyCellStyle.setBorderBottom(BorderStyle.THIN);
            unifyCellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            // 左边栏
            unifyCellStyle.setBorderLeft(BorderStyle.THIN);
            unifyCellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            // 居中展示
            unifyCellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);

            //设置单元格背景颜色
            unifyCellStyle.setFillForegroundColor(BopExportEnum.GREEN.getName());
            // 填充样式
            unifyCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            switch (size){
                case  0:
                    // 第一行设置 层级表格 和  顶层表格
                    CellRangeAddress hierarchyCra = new CellRangeAddress(0, 3, 0, 0);
                    sheet.addMergedRegion(hierarchyCra);
                    // 最左侧设置层级表格
                    Cell hierarchy = row.createCell(0);
                    hierarchy.setCellValue("层级");
                    hierarchy.setCellStyle(unifyCellStyle);
                    // 做顶层设置表格
                    Cell topFloor = row.createCell(1);
                    topFloor.setCellValue(headName);
                    topFloor.setCellStyle(unifyCellStyle);
                    CellRangeAddress topFloorCra = new CellRangeAddress(0, 0, 1, rowNumber);
                    RegionUtil.setBorderBottom(BorderStyle.THIN, topFloorCra, sheet);
                    sheet.addMergedRegion(topFloorCra);
                    break;
                case 1:
                    // 设置第二层类型
                    Iterator<Map.Entry<String, List<MoreFieldEntryVo>>> typeIterator = map.entrySet().iterator();
                    AtomicInteger atomicInteger = new AtomicInteger();
                    AtomicInteger atomicType  = new AtomicInteger(1);
                    while (typeIterator.hasNext()){
                        Map.Entry<String, List<MoreFieldEntryVo>> next = typeIterator.next();
                        String key = next.getKey();
                        List<MoreFieldEntryVo> moreFieldEntryVos = next.getValue();
                        if (moreFieldEntryVos.size() > 1){
                            CellStyle cellStyle = setCustomCellStyle(wb, font, moreFieldEntryVos,atomicType);
                            CellRangeAddress typeCra = new CellRangeAddress(1, 1, atomicInteger.get()+1, atomicInteger.get()+moreFieldEntryVos.size());
                            sheet.addMergedRegion(typeCra);
                            Cell typeCell = row.createCell(atomicInteger.get()+1);
                            typeCell.setCellValue(key);
                            typeCell.setCellStyle(cellStyle);
                            //合并后设置下边框
                            RegionUtil.setBorderBottom(BorderStyle.THIN, typeCra, sheet);
                            atomicInteger.addAndGet(moreFieldEntryVos.size());
                            continue;
                        }
                        atomicInteger.addAndGet(moreFieldEntryVos.size());
                        Cell typeCell = row.createCell(atomicInteger.get());
                        typeCell.setCellStyle(unifyCellStyle);
                    }
                     break;
                case 2:
                    // 设置第三层类型
                    Iterator<Map.Entry<String, List<MoreFieldEntryVo>>> iteratorRequired = map.entrySet().iterator();
                    AtomicInteger numberRequired = new AtomicInteger();
                    AtomicInteger atomicRequired = new AtomicInteger(1);
                    while (iteratorRequired.hasNext()){
                        List<MoreFieldEntryVo> moreFieldEntryVos = iteratorRequired.next().getValue();
                        CellStyle cellStyle = setCustomCellStyle(wb, font, moreFieldEntryVos,atomicRequired);
                        moreFieldEntryVos.forEach(moreFieldEntryVo -> {
                            Cell cell = row.createCell(numberRequired.get()+1);
                            cell.setCellValue(moreFieldEntryVo.getRequired());
                            cell.setCellStyle(cellStyle);
                            sheet.setColumnWidth(numberRequired.get(), 90 * 60);
                            numberRequired.getAndIncrement();
                        });

                    }
                    break;
                case 3:
                    // 设置第死层类型
                    Iterator<Map.Entry<String, List<MoreFieldEntryVo>>> iteratorFieldName = map.entrySet().iterator();
                    AtomicInteger numberFieldName = new AtomicInteger();
                    AtomicInteger atomicFieldName  = new AtomicInteger(1);
                    while (iteratorFieldName.hasNext()){
                        List<MoreFieldEntryVo> moreFieldEntryVos = iteratorFieldName.next().getValue();
                        CellStyle cellStyle = setCustomCellStyle(wb, font, moreFieldEntryVos,atomicFieldName);
                        moreFieldEntryVos.forEach(moreFieldEntryVo -> {
                            Cell cell = row.createCell(numberFieldName.get()+1);
                            cell.setCellValue(moreFieldEntryVo.getFieldName());
                            cell.setCellStyle(cellStyle);
                            sheet.setColumnWidth(numberFieldName.get(), 90 * 60);
                            numberFieldName.getAndIncrement();
                        });

                    }
                    break;
                default:
                    break;
            }
            size++;
        }while (size != 4);
    }


    public static CellStyle setCustomCellStyle(Workbook wb, Font font, List<MoreFieldEntryVo> moreFieldEntryVos,AtomicInteger atomicInteger){
        CellStyle disunityCellStyles = wb.createCellStyle();
        disunityCellStyles.setFont(font);

        if (moreFieldEntryVos.size() > 1){
            //设置单元格背景颜色
            disunityCellStyles.setFillForegroundColor(BopExportEnum.getBopExportEnum(atomicInteger.get()+"").getName());
            atomicInteger.incrementAndGet();
        }else {
            //设置单元格背景颜色
            disunityCellStyles.setFillForegroundColor(BopExportEnum.GREEN.getName());
        }
        disunityCellStyles.setBorderTop(BorderStyle.THIN);
        disunityCellStyles.setTopBorderColor(IndexedColors.BLACK.getIndex());
        // 右边栏
        disunityCellStyles.setBorderRight(BorderStyle.THIN);
        disunityCellStyles.setRightBorderColor(IndexedColors.BLACK.getIndex());
        // 底边栏
        disunityCellStyles.setBorderBottom(BorderStyle.THIN);
        disunityCellStyles.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        // 左边栏
        disunityCellStyles.setBorderLeft(BorderStyle.THIN);
        disunityCellStyles.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        // 填充样式
        disunityCellStyles.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        disunityCellStyles.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        return disunityCellStyles;
    }
}
