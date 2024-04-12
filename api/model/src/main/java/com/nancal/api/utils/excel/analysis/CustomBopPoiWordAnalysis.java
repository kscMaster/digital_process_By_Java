package com.nancal.api.utils.excel.analysis;

import com.nancal.api.model.*;
import com.nancal.common.constants.BopConstant;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author hewei
 * @date 2022/9/27 15:51
 * @Description
 */
public class CustomBopPoiWordAnalysis<T extends WorkspaceObjectReq> extends ExcelPoiWordAnalysis<T>{

    private Map<String,MsgbImportReq> objectMap;

    @Override
    protected T getClassObject(Class<?> aClass, Map<String, Object> fieldMap, int currentIndex) {
        String dataType = (String)fieldMap.get(BopConstant.DATA_TYPE);
        if(StringUtils.isNotBlank(dataType)) {
            WorkspaceObjectReq workspaceObjectReq = objectMap.get(dataType);
            return super.getClassObject(workspaceObjectReq.getClass(), fieldMap, currentIndex);
        }else {
            return super.getClassObject(null, fieldMap, currentIndex);
        }
    }


    public Map<String, MsgbImportReq> getObjectMap() {
        return objectMap;
    }

    public void setObjectMap(Map<String, MsgbImportReq> objectMap) {
        this.objectMap = objectMap;
    }
}
