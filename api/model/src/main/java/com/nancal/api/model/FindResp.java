package com.nancal.api.model;

import com.nancal.model.entity.BOMNodeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName FindResp
 * @Description TODO
 * @Author 拓凯
 * @Date 2022/4/25
 * @Version 1.0
 **/
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "FindResp BOM首页查询结果的响应")
public class FindResp {


    @ApiModelProperty(value = "BOM节点信息")
    private BOMNodeEntity bomNodeResp;

}
