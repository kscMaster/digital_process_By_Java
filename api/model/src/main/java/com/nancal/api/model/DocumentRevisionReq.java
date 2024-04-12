package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;


@ApiModel(value = "DocumentRevision 文档版本 的请求")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRevisionReq extends ItemRevisionReq  implements Serializable{

    @ApiModelProperty(value="文档标题",required=false)
    @Length(max = 128)
    private String documentTitle;

    @ApiModelProperty(value="文档作者",required=false)
    @Length(max = 64)
    private String documentAuthor;

    @ApiModelProperty(value="文档主题",required=false)
    @Length(max = 128)
    private String documentSubject;
}