package com.nancal.service.bo;


import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nancal.model.entity.Gte4PlantEntity;
import com.nancal.api.model.Gte4PlantReq;
import com.nancal.api.model.Gte4PlantResp;






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
@ApiModel(value = "Gte4Plant 工厂 的BO")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Gte4Plant extends Item  implements Serializable{



    @Override
    public String getObjectType(){
        return "Gte4Plant";
    }

}