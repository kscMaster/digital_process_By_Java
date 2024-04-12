package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


/**
 * @ClassName CompareResp
 * @Description TODO
 * @Author 拓凯
 * @Date 2022/4/25
 * @Version 1.0
 **/
@Data
@ToString
@NoArgsConstructor
@ApiModel(value = "CompareResp BOM对比结果的响应")
public class CompareResp {

    @ApiModelProperty(value = "左对象集合")
    private List<BOMNodeResp> leftObjects;

    @ApiModelProperty(value = "右对象集合")
    private List<BOMNodeResp> rightObjects;
}
