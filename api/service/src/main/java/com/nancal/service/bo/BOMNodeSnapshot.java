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
@ApiModel(value = "BOMNodeSnapshot bom行快照 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BOMNodeSnapshot extends BOMNode  implements Serializable{

    public static final String SNAPSHOT_ID = "snapshotId";
    public static final String HIS_BOM_ID = "hisBomId";


    @ApiModelProperty(value="快照id",required=false)
    private String snapshotId;

    @ApiModelProperty(value="历史bom行id",required=false)
    private String hisBomId;

    public String getObjectType(){
        return "BOMNodeSnapshot";
    }

}