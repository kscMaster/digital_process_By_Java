package com.nancal.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "library_folder")
@ApiModel(value = "LibraryFolder 库文件夹 的Entity")
public  class LibraryFolderEntity  extends FolderEntity implements Serializable{
    @ApiModelProperty(value="包含类型",required=false)
    @Column(name = "library_type", length = 64)
    private String libraryType;
    @ApiModelProperty(value="显示列Key",required=false)
    @Column(name = "column_key", length = 64)
    private String columnKey;
    @ApiModelProperty(value="数量",required=false)
    @Column(name = "quantity", length = 0)
    private Integer quantity;
    @ApiModelProperty(value="资源库icon",required=false)
    @Column(name = "icon", length = 0)
    private String icon;

    public String getObjectType() {
        return "LibraryFolder";
    }

}