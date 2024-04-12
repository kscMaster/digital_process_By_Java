package com.nancal.api.model;

import com.nancal.api.utils.FieldAlias;
import com.nancal.common.annotation.QueryField;
import com.nancal.common.base.AddGroup;
import com.nancal.common.base.FidHistoryGroup;
import com.nancal.common.base.LikeGroup;
import com.nancal.common.base.UpdateGroup;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;


import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@ApiModel(value = "MfgStep 制造工步 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MfgStepReq extends MsgbImportReq implements Serializable{

    @QueryField(Ops.LIKE)
    @NotBlank(message = "工步内容不能为空",groups = {AddGroup.class,UpdateGroup.class})
    @ApiModelProperty(value="工步内容",required=true)
    @Length(max = 1024,message = "工步内容长度超过了最大限制",groups = {AddGroup.class, UpdateGroup.class, LikeGroup.class})
    @FieldAlias(name = "工步内容",required = true,max = 1024)
    private String stepContent;
}