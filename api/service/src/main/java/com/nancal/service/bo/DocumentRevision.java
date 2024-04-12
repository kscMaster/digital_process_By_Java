package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "DocumentRevision 文档版本 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DocumentRevision extends ItemRevision  implements Serializable{

    @ApiModelProperty(value="文档标题",required=false)
    private String documentTitle;

    @ApiModelProperty(value="文档作者",required=false)
    private String documentAuthor;

    @ApiModelProperty(value="文档主题",required=false)
    private String documentSubject;

}