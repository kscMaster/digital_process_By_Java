package com.nancal.inspectionlibrary.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.nancal.api.model.LibraryFolderReq;
import com.nancal.api.model.LibraryFolderResp;
import com.nancal.api.model.LibraryResp;
import com.nancal.api.model.BusinessObjectReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.annotation.TimeLog;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.enums.LibraryEnum;
import com.nancal.common.exception.ServiceException;
import com.nancal.model.entity.*;
import com.nancal.service.service.ILibraryFolderDomainService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.*;

@Service
public class InspectionLibraryFolderDomainServiceImpl implements ILibraryFolderDomainService {

    @Autowired
    private UserUtils userUtils;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private DictUtil dictUtil;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;


    /**
     *  创建库
     * @param req
     * @author: 薛锦龙
     * @time: 2022/4/26
     * @return: {@link WorkspaceObjectResp}
     */
    @Override
    @Transactional
    public WorkspaceObjectResp save(BusinessObjectReq req) {
        LibraryFolderReq libraryFolderReq = (LibraryFolderReq) req;
        if (!LibraryEnum.isContains(libraryFolderReq.getLibraryType())) {
            throw new ServiceException(ErrorCode.E_12, "库类型输入有误，请校验库的类型");
        }
        //创建子类与库的关系
        String relation = dictUtil.getRelation(EntityUtil.getObjectType(), EntityUtil.getObjectType());
        String classPath = EntityUtil.getEntityPackage(relation);
        RelationEntity relationEntity = ReflectUtil.newInstance(classPath);
        if (!libraryFolderReq.getLeftObject().equals(LibraryEnum.ZERO.getName())) {
            //校验文件夹类型
            QLibraryFolderEntity qLibraryFolderEntity = QLibraryFolderEntity.libraryFolderEntity;
            BooleanBuilder where = new BooleanBuilder();
            where.and(qLibraryFolderEntity.delFlag.isFalse());
            where.and(qLibraryFolderEntity.uid.eq(libraryFolderReq.getLeftObject()));
            LibraryFolderEntity libraryFolderEntity = jpaQueryFactory.selectFrom(qLibraryFolderEntity).where(where).fetchFirst();
            if (ObjectUtil.isEmpty(libraryFolderEntity)){
                throw new ServiceException(ErrorCode.E_12,"库文件夹数据不存在");
            }
            if (libraryFolderEntity.getObjectName().contains(LibraryEnum.MODEL.getName())){
                if (!libraryFolderReq.getObjectName().contains(LibraryEnum.BATCH.getName())){
                    throw new ServiceException(ErrorCode.E_10,"库文件夹数据层级错误");
                }
            }else if(libraryFolderEntity.getObjectName().contains(LibraryEnum.BATCH.getName())){
                if (!libraryFolderReq.getObjectName().contains(LibraryEnum.SPECIALIZED.getName())){
                    throw new ServiceException(ErrorCode.E_10,"库文件夹数据层级错误");
                }
            }else {
                throw new ServiceException(ErrorCode.E_10,"库文件夹数据层级错误");
            }
            //创建左对象属性
            relationEntity.setLeftObject(libraryFolderReq.getLeftObject());
            relationEntity.setLeftObjectType(EntityUtil.getObjectType());
        }else {
            if (!libraryFolderReq.getObjectName().contains(LibraryEnum.MODEL.getName())){
                throw new ServiceException(ErrorCode.E_10,"库文件夹数据层级错误");
            }
        }
        //创建库文件夹
        WorkspaceObjectResp save = ILibraryFolderDomainService.super.save(libraryFolderReq);
        return save;
    }

    /**
     * 获取基础库树结构
     * @param
     * @author: 薛锦龙
     * @time: 2022/4/26
     * @return: {@link List< LibraryFolderResp>}
     */
    @Override
    @TimeLog
    public List<LibraryResp> treeLibrary(){
        List type = new ArrayList();
        type.add(LibraryEnum.GTE4INSPECTION.getCode());
        List tree = ILibraryFolderDomainService.super.tree(type);
        return tree;
    }



}
