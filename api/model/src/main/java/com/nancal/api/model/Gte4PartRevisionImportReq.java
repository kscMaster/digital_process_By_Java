package com.nancal.api.model;

import com.nancal.api.utils.FieldAlias;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;


@ApiModel(value = "Gte4PartRevision 设计零件版本导入请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4PartRevisionImportReq extends Gte4PartRevisionReq  implements Serializable{


    @ApiModelProperty(value="存储子级")
    private List<Gte4PartRevisionImportReq> gte4PartRevisionImportReqList;

}