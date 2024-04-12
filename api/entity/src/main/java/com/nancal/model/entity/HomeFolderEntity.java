package com.nancal.model.entity;

import io.swagger.annotations.ApiModel;
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
@Entity(name = "home_folder")
@ApiModel(value = "HomeFolder Home文件夹 的Entity")
public  class HomeFolderEntity  extends FolderEntity implements Serializable{
    @Override
    public String getObjectType() {
        return "HomeFolder";
    }
}