package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel(value = "DocumentRevision 文档版本 的响应")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRevisionResp extends ItemRevisionResp  implements Serializable{
    @ApiModelProperty(value="文档标题",required=false)
    private String documentTitle;
    @ApiModelProperty(value="文档作者",required=false)
    private String documentAuthor;
    @ApiModelProperty(value="文档主题",required=false)
    private String documentSubject;
    @ApiModelProperty(value="文档内容",required=false)
    private String documentContent;
}