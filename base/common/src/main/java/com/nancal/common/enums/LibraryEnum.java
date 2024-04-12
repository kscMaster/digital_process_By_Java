package com.nancal.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ZeroCopyHttpOutputMessage;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum LibraryEnum {
    GTE4INSPECTION("Gte4Inspection", "检验项目库"),
    MODEL("Model", "型号"),
    SPECIALIZED("Specialized", "专业"),
    BATCH("Batch", "批次"),
    TOOL("Tool", "资源库工夹具"),
    MEASURE("Measure", "资源库量具"),
    EQUIPMENT("Equipment", "资源库设备"),
    AUXILIARYMATERIAL("AuxiliaryMaterial", "资源库辅料"),
    ZERO("zero","0");

    private String code;
    private String name;

    public static Boolean isContains(String code) {
        return Arrays.stream(LibraryEnum.values()).map(enums -> enums.getCode()).collect(Collectors.toSet()).contains(code);
    }
}
