package com.nancal.imexport.service;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.nancal.api.model.AssociatedBoardsReq;
import com.nancal.api.model.AuxiliaryMaterialRevisionImportReq;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.model.entity.BOMNodeEntity;
import com.nancal.model.entity.Gte4PartRevisionEntity;
import com.nancal.model.entity.LibraryFolderEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.remote.vo.MoreDictEntryGroupVo;
import com.nancal.remote.vo.MoreDictEntryVo;
import com.nancal.service.bo.Item;
import com.nancal.service.factory.ItemFactory;
import com.nancal.service.service.IBOMNodeDomainService;
import com.nancal.service.service.IItemDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Objects;
@Service
public interface IImexportServiceAdaptor extends IItemDomainService {

    /**
     * 统计资源库菜单计数
     * @param objectType 左侧菜单类型
     * @param leftObject 左侧菜单uid
     * @param statisticsNumber 总计数
     */
    @Transactional(rollbackFor = Exception.class)
    default void statistics(String objectType,String leftObject,Integer statisticsNumber){
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        LibraryFolderEntity libraryFolderEntity = EntityUtil.getById(objectType, leftObject);
        if (Objects.isNull(libraryFolderEntity)) {
            throw new ServiceException(ErrorCode.E_12);
        }
        libraryFolderEntity.setQuantity(libraryFolderEntity.getQuantity()+statisticsNumber);
        entityManager.merge(libraryFolderEntity);
    }

    /**
     *  解析获取自定义存储
     * @param codeValueList
     * @param auxiliaryMaterialRevisionImportReqHashMap
     * @param moreDictEntryVoMap
     */
    default void customField(List<MoreDictEntryGroupVo> codeValueList, Map<String, AuxiliaryMaterialRevisionImportReq> auxiliaryMaterialRevisionImportReqHashMap,Map<String, MoreDictEntryVo> moreDictEntryVoMap){
        if (!codeValueList.isEmpty()){
            MoreDictEntryGroupVo moreDictEntryGroupVo = codeValueList.get(0);
            moreDictEntryGroupVo.getDictList().forEach(moreDictEntryVo -> {
                if (StringUtils.isNotBlank(moreDictEntryVo.getParentId())) {
                    AuxiliaryMaterialRevisionImportReq parseObject = JSON.parseObject(moreDictEntryVo.getRemark(), AuxiliaryMaterialRevisionImportReq.class);
                    if (parseObject.getType().equals("input") && parseObject.getDataType().equals("int")){
                        moreDictEntryVoMap.put(moreDictEntryVo.getValue()+"#",moreDictEntryVo);
                        auxiliaryMaterialRevisionImportReqHashMap.put(moreDictEntryVo.getValue()+"#",parseObject);
                    }else if (parseObject.getType().equals("radio")) {
                        moreDictEntryVoMap.put(moreDictEntryVo.getValue()+"&",moreDictEntryVo);
                        auxiliaryMaterialRevisionImportReqHashMap.put(moreDictEntryVo.getValue() + "&",parseObject);
                    }else if (parseObject.getType().equals("select")){
                        moreDictEntryVoMap.put(moreDictEntryVo.getValue()+"*",moreDictEntryVo);
                        auxiliaryMaterialRevisionImportReqHashMap.put(moreDictEntryVo.getValue() + "*",parseObject);
                    }else {
                        moreDictEntryVoMap.put(moreDictEntryVo.getValue(),moreDictEntryVo);
                        auxiliaryMaterialRevisionImportReqHashMap.put(moreDictEntryVo.getValue(),parseObject);
                    }
                }
            });
        }
    }

    /**
     * 关联板子
     * @param req
     * @author: 薛锦龙
     * @time: 2022/8/30
     * @return: {@link boolean}
     */
    @Transactional(rollbackFor = Exception.class)
    default boolean associatedBoards(AssociatedBoardsReq req){
        String objectType = new BOMNodeEntity().getObjectType();
        Item item = SpringUtil.getBean(ItemFactory.class).create();
        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
        IBOMNodeDomainService ibomNodeDomainService = SpringUtil.getBean(IBOMNodeDomainService.class);
        ibomNodeDomainService.existParent(req.getUid(), objectType,true);
        WorkspaceObjectEntity activeSequence = item.getLastVersion(req.getUid(), req.getObjectType());
        if (ObjectUtil.isEmpty(activeSequence)){
            throw new ServiceException(ErrorCode.E_12);
        }
        Gte4PartRevisionEntity partRevisionEntity = (Gte4PartRevisionEntity)activeSequence;
        partRevisionEntity.setBoardKey(req.getBoardKey());
        entityManager.merge(partRevisionEntity);
        return true;
    }
}
