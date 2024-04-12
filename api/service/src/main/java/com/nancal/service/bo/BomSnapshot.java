package com.nancal.service.bo;


import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;

@Data
@ApiModel(value = "BomSnapshot bom快照 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BomSnapshot extends WorkspaceObject  implements Serializable{

    public static final String BOM_VIEW = "bomView";
    public static final String REV_UID = "revUid";
    public static final String REV_OBJECT_TYPE = "revObjectType";
    public static final String REVISION_ID = "revisionId";
    public static final String ITEM_ID = "itemId";


    @ApiModelProperty(value="BOM视图",required=false)
    private String bomView;

    @ApiModelProperty(value="查询的版本uid",required=false)
    private String revUid;

    @ApiModelProperty(value="查询的对象类型",required=false)
    private String revObjectType;

    @ApiModelProperty(value="版本号",required=false)
    private String revisionId;

    @ApiModelProperty(value="零件id",required=false)
    private String itemId;

    public String getObjectType(){
        return "BomSnapshot";
    }

}