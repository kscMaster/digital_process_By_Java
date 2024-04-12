package com.nancal.api.model.common;

import com.nancal.api.model.WorkspaceObjectReq;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkBaseReq extends WorkspaceObjectReq {

    @ApiModelProperty(value = "主键id")
    @Length(max = 64, message = "主键id超过了最大限制", groups = {AddGroup.class, UpdateGroup.class})
    private String uid;

    @ApiModelProperty(value = "对象类型")
    @Length(max = 64, message = "对象类型超过了最大限制", groups = {AddGroup.class, UpdateGroup.class})
    private String objectType;

}
