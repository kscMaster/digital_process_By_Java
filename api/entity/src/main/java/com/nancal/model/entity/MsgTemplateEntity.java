package com.nancal.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@SuperBuilder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "msg_template")
@ApiModel(value = "MsgTemplate 消息模板 的Entity")
public class MsgTemplateEntity extends WorkspaceObjectEntity implements Serializable{

    public static final String TITLE = "title";

    public static final String BODY = "body";

    public static final String MSG_TYPE = "msgType";

    public static final String MSG_CODE = "msgCode";

    @ApiModelProperty(value="标题",required=false)
    @Column(name = "title", length = 128)
    private String title;

    @ApiModelProperty(value="主题",required=false)
    @Column(name = "body", length = 1024)
    private String body;

    @ApiModelProperty(value="消息类型",required=false)
    @Column(name = "msg_type", length = 0)
    private Integer msgType;

    @ApiModelProperty(value="消息code码",required=false)
    @Column(name = "msg_code", length = 128)
    private String msgCode;

    public String getObjectType(){
        return "MsgTemplate";
    }

}