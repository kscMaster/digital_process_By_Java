package com.nancal.service.factory;

import com.nancal.service.bo.MSExcel;
import org.springframework.stereotype.Component;

@Component
public class MSExcelFactory {
    public MSExcel create(){
        return new MSExcel();
    }
}