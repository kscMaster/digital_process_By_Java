package com.nancal.api.utils;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.AbstractVerticalCellStyleStrategy;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.HashMap;
import java.util.Map;

public class ExcelUtils extends AbstractVerticalCellStyleStrategy {
    //key为ExcelProperty value为颜色
    public Map<Integer,Short> colorMap=new HashMap<>();

    public ExcelUtils(Map<Integer,Short> colorMap){
        this.colorMap=colorMap;
    }

    //表格头样式
    @Override
    protected WriteCellStyle headCellStyle(Head head) {
        WriteCellStyle writeCellStyle=new WriteCellStyle();
        WriteFont font=new WriteFont();

        //自定义字体颜色
        for(Integer key:colorMap.keySet()){
            if(head.getColumnIndex()==key){
                font.setColor(colorMap.get(key));
                break;
            }else {
                font.setColor(IndexedColors.BLACK.index);//默认颜色
            }
        }

        //有兴趣可以自己定义别的这里只定义了color

        writeCellStyle.setWriteFont(font);
        return writeCellStyle;
    }


    //单元格格内样式 写法与上面类似
    @Override
    protected WriteCellStyle contentCellStyle(Head head) {
        return null;
    }

}
