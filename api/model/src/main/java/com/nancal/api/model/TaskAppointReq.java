package com.nancal.api.model;

import com.nancal.api.model.common.ValidList;
import com.nancal.common.base.IdRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import java.io.Serializable;


@ApiModel(value = "章节任务指派 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TaskAppointReq  implements Serializable{

    @ApiModelProperty(value="被指派者用户ID",required=true)
    @Length(max = 64)
    private String ownerId;

    @ApiModelProperty(value="被指派者用户名",required=true)
    @Length(max = 128)
    private String ownerName;

    @Valid
    @ApiModelProperty(value="代图号",required=true)
    ValidList<IdRequest> ids;

    @ApiModelProperty(value="根的childItem",required=false)
    @Length(max = 64)
    private String rootUid;

    @ApiModelProperty(value="根的childItem的Name",required=false)
    @Length(max = 64)
    private String rootObjectName;

    @ApiModelProperty(value="根的childItemType",required=false)
    @Length(max = 64)
    private String rootObjectType;

}