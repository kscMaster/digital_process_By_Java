package com.nancal.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@Entity(name = "folder")
@ApiModel(value = "Folder 文件夹 的Entity")
public  class FolderEntity  extends WorkspaceObjectEntity implements Serializable{
    @ApiModelProperty(value="排序顺序",required=false)
    @Column(name = "sort", length = 128)
    private String sort;
    @ApiModelProperty(value="排序准则",required=false)
    @Column(name = "criteria", length = 128)
    private String criteria;
}