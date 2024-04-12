package com.nancal.api.model;

import com.nancal.api.model.common.RelationReq;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;


@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@ToString
@NoArgsConstructor

@ApiModel(value = "DatasetFileRL 数据集文件关系 的请求")
public class DatasetFileRLReq extends RelationReq {


    @Override
    public String getRelationType() {
        return "IncludeRL";
    }
}
