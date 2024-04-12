package com.nancal.service.factory;

import com.nancal.service.bo.WPSExcel;
import org.springframework.stereotype.Component;

@Component
public class WPSExcelFactory {
    public WPSExcel create(){
        return new WPSExcel();
    }
}