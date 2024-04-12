package com.nancal.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;


@ApiModel(value = "LibraryFolderRL 库文件夹关系 的响应")
@Data

@NoArgsConstructor
public class LibraryFolderRLResp extends RelationResp  implements Serializable{
}