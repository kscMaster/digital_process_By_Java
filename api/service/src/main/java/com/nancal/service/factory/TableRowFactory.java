package com.nancal.service.factory;

import com.nancal.service.bo.TableRow;
import org.springframework.stereotype.Component;

@Component
public class TableRowFactory {
    public TableRow create(){
        return new TableRow();
    }
}