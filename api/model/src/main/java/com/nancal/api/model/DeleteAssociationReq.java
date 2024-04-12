package com.nancal.api.model;

import com.nancal.api.model.common.ValidList;
import com.nancal.common.base.IdRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
@ApiModel(value = "删除关联关系的请求")
@Data
@NoArgsConstructor
public class DeleteAssociationReq {
    @ApiModelProperty(value = "左对象类型",required = true)
    @NotBlank(message = "左对象类型不能为空")
    @Length(max = 64,message = "左对象类型超过了最大长度限制")
    private String leftObject;
    @ApiModelProperty(value = "左对象类型",required = true)
    @NotBlank(message = "左对象类型不能为空")
    @Length(max = 64,message = "左对象类型超过了最大长度限制")
    private String leftObjectType;
    @ApiModelProperty(value = "当前选中行数据",required = true)
    private ValidList<IdRequest> uids;

}
