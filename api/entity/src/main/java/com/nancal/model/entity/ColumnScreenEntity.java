package com.nancal.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@ToString
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Entity(name = "column_screen")
@ApiModel(value = "ColumnScreen 列筛选 的Entity")
public class ColumnScreenEntity extends AdminEntity {

    @ApiModelProperty(value = "应用名称")
    @Column(name = "app_name", length = 128)
    private String appName;
    /**
        列筛选针对的是哪个对象存储在ObjectName上，如对BOMLine的列筛选,在ObjectName上存BOMLine
        不同用户使用OwnerId和OwnerName区分
     */
    @ApiModelProperty(value = "要显示的列名称,用逗号分割")
    @Column(name = "display_properties", length = 1024)
    private String displayProperties;

    @Override
    public String getObjectType() {
        return "ColumnScreen";
    }

    public static final String APP_NAME = "appName";
}
