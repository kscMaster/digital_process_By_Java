package com.nancal.api.utils;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.nancal.common.constants.Constant;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.remote.service.RemoteLezaoCodeSetService;
import com.nancal.remote.to.LezaoCodingTo;
import com.nancal.remote.vo.DictItemVo;
import com.nancal.remote.vo.LezaoResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CoderSetUtil {

    @Autowired
    private RemoteLezaoCodeSetService remoteLezaoCodeSetService;

    private List<String> getCoderListByType(String objectType, Integer count, String referenceValue, Map<Integer,String> variableValueMap){
        String ruleCoding = objectType+"_itemId";
        LezaoCodingTo lezaoCodingTo = LezaoCodingTo.builder().ruleCoding(ruleCoding).counts(count).
                referenceValue(referenceValue).variableValueMap(variableValueMap).build();
        List<String> result = getCoderListByType(lezaoCodingTo, objectType);
        if(CollUtil.isEmpty(result)){
            throw new ServiceException(ErrorCode.REMOTE_FAIL, "未获取到对应的code编码");
        }
        return result;
    }

    public String getOneCoderByObjectType(String objectType){
        if(StrUtil.isBlank(objectType)){
            return null;
        }
        return getCoderListByType(objectType,1,null,null).get(0);
    }

    public List<String> getCoderListByObjectType(String objectType, Integer count, String referenceValue, Map<Integer,String> variableValueMap){

        return getCoderListByType(objectType,count,referenceValue,variableValueMap);
    }

    /***
     * 根据对像类型和字典类型，获取其下的字典明细
     *
     * @param objectType 对像类型
     * @param lezaoCodingTo 数据参数
     * @author 徐鹏军
     * @date 2022/4/12 12:29
     * @return {@link List<DictItemVo>}
     */
    private List<String> getCoderListByType(LezaoCodingTo lezaoCodingTo,String objectType) {
        LezaoResult<List<String>> result = remoteLezaoCodeSetService.getSpecificationCodeList(lezaoCodingTo);
        if (ObjectUtil.isNull(result)) {
            throw new ServiceException(ErrorCode.REMOTE_FAIL, "远程调用编码规则服务异常");
        }
        if (CollUtil.isNotEmpty(result.getData())) {
            return result.getData();
        }
        Class<?> clazz = EntityUtil.getEntityClass(objectType);
        String simpleName = clazz.getSuperclass().getSimpleName();
        // 如果都找到了Object了，则直接放弃
        if (Object.class.getSimpleName().equals(simpleName)) {
            return Collections.emptyList();
        }
        // 将Entity字符串替换并递归调用本方法
        String newObjectType = simpleName.replaceAll(Constant.ENTITY, StrUtil.EMPTY);
        String newRuleCoding = newObjectType + StrUtil.UNDERLINE + StrUtil.subAfter(lezaoCodingTo.getRuleCoding(), StrUtil.UNDERLINE, false);
        lezaoCodingTo.setRuleCoding(newRuleCoding);
        return getCoderListByType(lezaoCodingTo, newObjectType);
    }

}
