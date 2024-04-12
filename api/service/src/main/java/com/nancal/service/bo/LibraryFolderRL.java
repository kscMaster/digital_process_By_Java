package com.nancal.service.bo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ApiModel(value = "LibraryFolderRL 库文件夹关系 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LibraryFolderRL extends Relation  implements Serializable{

}