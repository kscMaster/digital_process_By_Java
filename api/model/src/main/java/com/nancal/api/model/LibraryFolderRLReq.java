package com.nancal.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;


@ApiModel(value = "LibraryFolderRL 库文件夹关系 的请求")
@Data

@NoArgsConstructor
public class LibraryFolderRLReq extends RelationReq  implements Serializable{
}