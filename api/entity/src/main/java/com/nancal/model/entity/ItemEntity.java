package com.nancal.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.io.Serializable;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "item")
@ApiModel(value = "Item 零组件 的Entity")
public  class ItemEntity  extends WorkspaceObjectEntity  implements Serializable{

    public static final String ITEM_ID = "itemId";


    @ApiModelProperty(value="零组件号",required=false)
    @Column(name = "item_id", length = 64)
    private String itemId;

//    @ApiModelProperty(value="代图号",required=false)
//    @Column(name = "part_no", length = 64)
//    private String partNo;

    @Override
    public String getObjectType(){
        return "ITEM";
    }

}