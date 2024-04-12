package com.nancal.remote.to;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author： Wang Hui
 * @date： 2022/5/5 13:42
 * @description： 流程分页查询
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessPageQueryTo implements Serializable {

    private static final long serialVersionUID = -8579723433771616455L;

    @NotEmpty(message = "分页页码不能为空")
    @ApiModelProperty(value = "分页页码")
    private Integer pageNo;

    @NotEmpty(message = "分页大小不能为空")
    @ApiModelProperty(value = "分页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "查询条件")
    private QueryTo query;


}
