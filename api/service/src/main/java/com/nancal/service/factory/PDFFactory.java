package com.nancal.service.factory;

import com.nancal.service.bo.PDF;
import org.springframework.stereotype.Component;

@Component
public class PDFFactory {
    public PDF create(){
        return new PDF();
    }
}