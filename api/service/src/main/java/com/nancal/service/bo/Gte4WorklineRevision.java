package com.nancal.service.bo;


import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nancal.model.entity.Gte4WorklineRevisionEntity;
import com.nancal.api.model.Gte4WorklineRevisionReq;
import com.nancal.api.model.Gte4WorklineRevisionResp;






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
@ApiModel(value = "Gte4WorklineRevision 线体版本 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Gte4WorklineRevision extends ItemRevision  implements Serializable{

    public static final String GTE4ORDER_NO = "gte4orderNo";
    public static final String GTE4WORKSHOP = "gte4Workshop";
    public static final String GTE4ASSET_NUM = "gte4AssetNum";
    public static final String GTE4WORKAREA = "gte4Workarea";
    public static final String GTE4DEPARTMENT_NAME = "gte4DepartmentName";
    public static final String GTE4LINE_CAPACITY = "gte4LineCapacity";


    @ApiModelProperty(value="序号",required=false)
    private String gte4orderNo;

    @ApiModelProperty(value="所属车间",required=false)
    private String gte4Workshop;

    @ApiModelProperty(value="资产编号",required=false)
    private String gte4AssetNum;

    @ApiModelProperty(value="区域",required=false)
    private String gte4Workarea;

    @ApiModelProperty(value="所属部门",required=false)
    private String gte4DepartmentName;

    @ApiModelProperty(value="线体能力",required=false)
    private String gte4LineCapacity;

    @Override
    public String getObjectType(){
        return "Gte4WorklineRevision";
    }

}