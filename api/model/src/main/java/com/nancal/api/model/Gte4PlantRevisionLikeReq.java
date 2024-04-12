package com.nancal.api.model;

import com.nancal.common.annotation.QueryField;
import com.nancal.common.base.UpdateGroup;
import com.querydsl.core.types.Ops;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@ApiModel(value = "Gte4PlantRevision 工厂 的请求")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Gte4PlantRevisionLikeReq extends Gte4PlantRevisionReq{
    @QueryField(Ops.IN)
    @ApiModelProperty(value="uid",required=true)
    private List<String> uids;

    @ApiModelProperty(value="bom行uid",required=true)
    @Length(max = 128,groups = {UpdateGroup.class})
    @NotEmpty(message = "BOM行uid不能为空")
    private String bomUid;

}
