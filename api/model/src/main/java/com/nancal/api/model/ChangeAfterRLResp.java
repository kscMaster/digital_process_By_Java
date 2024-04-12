package com.nancal.api.model;


import com.nancal.common.constants.DictConstant;
import com.nancal.framework.common.annotations.JsonDict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ApiModel(value = "ChangeAfterRL 更改后 的响应")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChangeAfterRLResp extends RelationResp  implements Serializable{

    @JsonDict(DictConstant.CHANGE_TYPE)
    @ApiModelProperty(value="更改类型",required=false)
    private String changeType;
    @ApiModelProperty(value="更改内容",required=false)
    private String changeReason;
    @ApiModelProperty(value="在制品处理意见",required=false)
    private String wipSuggestion;
    @ApiModelProperty(value="已制品处理意见",required=false)
    private String processedSuggestion;
    @ApiModelProperty(value="更改意见",required=false)
    private String changeOpinion;
    @ApiModelProperty(value="生效时间",required=false,example="1994-03-07")
    private LocalDateTime effectiveDate;
    @ApiModelProperty(value="同时更改的文件和资料",required=false)
    private String changeAtOnce;

    @Override
    public String getObjectType(){
       return "ChangeAfterRL";
    }

    public String getRelationType(){
       return "ChangeAfterRL";
    }
}