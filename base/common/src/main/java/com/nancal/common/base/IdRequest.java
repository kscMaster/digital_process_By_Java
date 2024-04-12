package com.nancal.common.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class IdRequest implements Serializable {

    @NotBlank(message = "id不能为空")
    @ApiModelProperty(value = "ID")
    private String uid;

    @ApiModelProperty(value = "对象类型")
    private String objectType;

    public IdRequest(String uid) {
        this.uid = uid;
    }
}
