package com.nancal.document.service.impl;


import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.CoderSetUtil;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.common.constants.DictConstant;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.utils.BeanUtil;
import com.nancal.model.entity.BOMNodeEntity;
import com.nancal.model.entity.Gte4TechDocumentEntity;
import com.nancal.model.entity.WorkspaceObjectEntity;
import com.nancal.remote.vo.DictItemVo;
import com.nancal.service.service.IBOMNodeDomainService;
import com.nancal.service.service.IGte4MfgOperationDomainService;
import com.nancal.service.service.IGte4TechDocumentDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class Gte4TechDocumentDomainServiceImpl implements IGte4TechDocumentDomainService {

    @Autowired
    private IBOMNodeDomainService ibomNodeDomainService;
    @Autowired
    private DictUtil dictUtil;

    /**
     * 创建设计零件对象，并创建关联的bom节点数据
     * 1、先将工序对象保存到数据库中 gte4part（工序表） master_r_l (关系表) gte4part_revision（工序版本表）
     * 2、再调用新增bom节点方法，新增bom节点  bomnode（bom节点表）
     *
     * @param req
     * @author: 拓凯
     * @time: 2022/7/5
     * @return: {@link BusinessObjectResp}
     */
    @Override
    @Transactional
    public WorkspaceObjectResp saveBomReq(BusinessObjectReq req) {
        Gte4TechDocumentBomReq documentBomReq = (Gte4TechDocumentBomReq) req;
        BOMNodeReq nodeReq = documentBomReq.getBomNodeReq();
//        1、单独创建设计零件对象
        WorkspaceObjectResp save = IGte4TechDocumentDomainService.super.save(documentBomReq.getTechDocumentRevisionReq());
//        2、在父级底下添加设计零件对象
        if (!Objects.isNull(nodeReq)) {
            //判断是否可以添加，取决于父级的是否具有编辑权限
            IGte4TechDocumentDomainService.super.checkParentRevEidt(nodeReq);
            nodeReq.setChildItem(save.getUid());
            nodeReq.setChildItemType(new Gte4TechDocumentEntity().getObjectType());
            ibomNodeDomainService.createNode(nodeReq, AppNameEnum.DOCUMENTBOM);
        }
        return save;
    }


    /**
     * 克隆文档，以及克隆文档底下所有的对象
     *
     * @param req
     * @author: 拓凯
     * @time: 2022/7/21
     * @return: {@link BusinessObjectResp}
     */
    @Override
    @Transactional
    public BusinessObjectResp cloneTechDocument(Gte4TechDocumentCloneReq req) {
        //1.先根据入参新建一个新的文档，拿到文档的uid
        WorkspaceObjectResp techDocument = IGte4TechDocumentDomainService.super.save(req.getDocumentRevisionReq());
        //2.再调用克隆BOMNode方法
        ibomNodeDomainService.cloneBomNode(req.getCloneType(),req.getCloneId(),techDocument.getObjectType(),
                techDocument.getUid(),techDocument.getRightObject(),AppNameEnum.DOCUMENTBOM,new HashMap<>());
        return techDocument;
    }

}
