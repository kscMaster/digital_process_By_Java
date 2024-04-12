package com.nancal.remote.to;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class FlowableQueryTo implements Serializable {

    @ApiModelProperty(value = "业务标识")
    private String businessKey;

    @ApiModelProperty(value = "应用类别")
    private String category;

    @NotNull(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id")
    private String userId;

}
