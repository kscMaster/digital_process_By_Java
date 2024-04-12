package com.nancal.inspectionlibrary.service.impl;

import cn.hutool.core.util.StrUtil;
import com.nancal.api.model.*;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import com.nancal.inspectionlibrary.service.IItemRevisionDomainServiceAdaptor;
import com.nancal.model.entity.Gte4InspectionRevisionEntity;
import com.nancal.model.entity.QGte4InspectionRevisionEntity;
import com.nancal.service.service.IGte4InspectionRevisionDomainService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class Gte4InspectionRevisionDomainServiceImpl implements IGte4InspectionRevisionDomainService, IItemRevisionDomainServiceAdaptor {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;


    @Override
    public BusinessObjectResp updatePlus(BusinessObjectReq req) {
        Gte4InspectionRevisionReq gte4InspectionRevisionReq = (Gte4InspectionRevisionReq)req;
        this.getGte4IDCheck(gte4InspectionRevisionReq.getGte4ID(),gte4InspectionRevisionReq.getItemId());
        return IItemRevisionDomainServiceAdaptor.super.updatePlus(req);
    }

    /**
     * getGte4ID为空校验
     * @param getGte4ID
     * @param itemId
     * @author: 薛锦龙
     * @time: 2022/6/9
     * @return: {@link }
     */
    public void getGte4IDCheck(String getGte4ID,String itemId){
        if (StrUtil.isNotBlank(getGte4ID)){
            QGte4InspectionRevisionEntity qGte4InspectionRevisionEntity = QGte4InspectionRevisionEntity.gte4InspectionRevisionEntity;
            BooleanBuilder where = new BooleanBuilder();
            where.and(qGte4InspectionRevisionEntity.gte4ID.eq(getGte4ID));
            where.and(qGte4InspectionRevisionEntity.delFlag.isFalse());
            List<Gte4InspectionRevisionEntity> fetch = jpaQueryFactory.selectFrom(qGte4InspectionRevisionEntity).where(where).fetch();
            fetch.forEach(data->{
                if (!data.getItemId().equals(itemId)){
                    throw new ServiceException(ErrorCode.E_10,"代号已存在" );
                }
            });
        }
    }

    @Override
    public WorkspaceObjectResp upgradeAndDataSet(WorkspaceObjectReq req) {
        Gte4InspectionRevisionReq gte4InspectionRevisionReq = (Gte4InspectionRevisionReq)req;
        //校验编码
        this.getGte4IDCheck(gte4InspectionRevisionReq.getGte4ID(),gte4InspectionRevisionReq.getItemId());
        return IGte4InspectionRevisionDomainService.super.upgradeAndDataSet(req);
    }
}
