//package com.nancal.myworkspace.service.impl;
//
//import com.nancal.api.model.LikeReq;
//import com.nancal.api.model.LikeResp;
//import com.nancal.api.utils.DictUtil;
//import com.nancal.common.base.PageHelper;
//import com.nancal.common.base.TableRequest;
//import com.nancal.common.base.TableResponse;
//import com.nancal.service.service.IObjectIndexDomainService;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import org.apache.commons.lang3.StringUtils;
//import org.hibernate.query.internal.NativeQueryImpl;
//import org.hibernate.transform.Transformers;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Service;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//import java.util.ArrayList;
//import java.util.List;
//
//
//@Service
//public class ObjectIndexDomainServiceImpl implements IObjectIndexDomainService {
//    @Autowired
//    private JPAQueryFactory queryFactory;
//
//    @Autowired
//    private DictUtil dictUtil;
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    /**
//     * 根据对象名称和代号分页模糊查询
//     *
//     * @param req
//     * @author: 拓凯
//     * @time: 2022/4/16
//     * @return: {@link TableResponse<LikeReq>}
//     */
//    @Override
//    public TableResponse<LikeResp> getByLike(TableRequest<LikeReq> req) {
//        PageRequest page = PageHelper.ofReq(req);
//        //构建查询sql
//        StringBuilder dataSql = new StringBuilder();
//        dataSql.append("SELECT * FROM (" +
//                getAuxMaterialRevisionLinkIndexSql() +
//                " UNION ALL" +
//                getPDFLinkIndexSql() +
//                " UNION ALL" +
//                getR006PartRevisionLinkIndexSql() + " )a ");
//        //根据传入条件动态拼接模糊查询条件
//        StringBuilder whereSql = new StringBuilder(" where 1=1 and a.delFlag = 0");
//        if (!StringUtils.isEmpty(req.getData().getObjectName()) && !StringUtils.isEmpty(req.getData().getItemId())) {
//            whereSql.append(" and a.objectName LIKE concat('%',:objectName,'%') or a.itemId LIKE concat('%',:itemId,'%')");
//        } else {
//            if (!StringUtils.isEmpty(req.getData().getObjectName())) {
//                whereSql.append(" and a.objectName like concat('%',:objectName,'%') ");
//            }
//            if (!StringUtils.isEmpty(req.getData().getItemId())) {
//                whereSql.append(" and a.itemId like concat('%',:itemId,'%') ");
//            }
//        }
//        dataSql.append(whereSql);
//        //按照更新时间降序排列
//        dataSql.append(" order by a.lastUpdateDate desc");
//        //获取总条数
//        String countSql = dataSql.toString().replace("*", "count(*)");
//        Query countQuery = entityManager.createNativeQuery(countSql);
//        setProperty(countQuery, req);
//        //判断查询到的数量
//        if (Long.parseLong(countQuery.getSingleResult().toString()) <= 0) {
//            return TableResponse.<LikeResp>builder()
//                    .total(Long.parseLong(countQuery.getSingleResult().toString()))
//                    .data(new ArrayList<>())
//                    .build();
//        }
//        //分页
//        StringBuilder limitSql = new StringBuilder(" limit :offset,:pageSize");
//        dataSql.append(limitSql);
//        //创建SQL并指定结果封装到指定对象中
//        Query dataQuery = entityManager.createNativeQuery(dataSql.toString());
//        dataQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(LikeResp.class));
//        setProperty(dataQuery, req);
//        dataQuery.setParameter("offset", page.getOffset());
//        dataQuery.setParameter("pageSize", page.getPageSize());
//        //查询并返回
//        List<LikeResp> resultList = dataQuery.getResultList();
//        //拼接展示名称:对象名称/版本
////        resultList.stream().forEach(data -> data.setDisplayName(data.getObjectName() + StrUtil.SLASH + data.getRevisionId()));
//        dictUtil.translate(resultList);
//        return TableResponse.<LikeResp>builder()
//                .total(Long.parseLong(countQuery.getSingleResult().toString()))
//                .data(resultList)
//                .build();
//    }
//
//    public String getAuxMaterialRevisionLinkIndexSql() {
//        return " SELECT" +
//                " mr.uid," +
//                " mr.revision_id revisionId," +
//                " mr.sequence," +
//                " mr.item_id itemId," +
//                " mr.object_name objectName," +
//                " NULL r006Phase," +
//                " NULL r006Key," +
//                " NULL r006Route," +
//                " NULL r006MaterialDensity," +
//                " NULL r006StandardNo," +
//                " mr.object_type objectType," +
//                " mr.lifecycle_state lifeCycleState," +
//                " mr.state_change_date changeDate," +
//                " oi.del_flag delFlag," +
//                " oi.last_update lastUpdateDate, " +
//                " rl.left_object leftObject," +
//                " rl.left_object_type leftObjectType " +
//                " FROM" +
//                " object_index oi" +
//                " JOIN r006_part_aux_material_revision mr ON oi.object_uid = mr.uid " +
//                " JOIN  master_r_l rl on mr.uid = rl.right_object " +
//                " WHERE" +
//                " 1 = 1 " +
//                " AND mr.uid IS NOT NULL " +
//                " AND rl.del_flag = 0 " +
//                " AND mr.active = 1 " +
//                " AND mr.del_flag = 0 ";
//    }
//
//    public String getPDFLinkIndexSql() {
//        return " SELECT" +
//                " f.uid," +
//                " NULL revisionId," +
//                " NULL sequence," +
//                " NULL itemId," +
//                " f.object_name objectName," +
//                " NULL r006Phase," +
//                " NULL r006Key," +
//                " NULL r006Route," +
//                " NULL r006MaterialDensity," +
//                " NULL r006StandardNo," +
//                " f.object_type objectType," +
//                " f.lifecycle_state lifeCycleState," +
//                " f.state_change_date changeDate," +
//                " oi.del_flag delFlag," +
//                " oi.last_update lastUpdateDate, " +
//                " f.uid leftObject," +
//                " f.object_type leftObjectType" +
//                " FROM" +
//                " object_index oi" +
//                " JOIN pdf f ON oi.object_uid = f.uid " +
//                " WHERE" +
//                " 1 = 1 " +
//                " AND f.uid IS NOT NULL " +
//                " AND f.del_flag = 0 ";
//    }
//
//    public String getR006PartRevisionLinkIndexSql() {
//        return " SELECT" +
//                " pr.uid," +
//                " pr.revision_id revisionId," +
//                " pr.sequence," +
//                " pr.item_id itemId," +
//                " pr.object_name objectName," +
//                " pr.r006_phase r006Phase," +
//                " pr.r006_key r006Key," +
//                " pr.r006_route r006Route," +
//                " pr.r006_material_density r006MaterialDensity," +
//                " pr.r006_standard_no r006StandardNo," +
//                " pr.object_type objectType," +
//                " pr.lifecycle_state lifeCycleState," +
//                " pr.state_change_date changeDate," +
//                " oi.del_flag delFlag," +
//                " oi.last_update lastUpdateDate, " +
//                " rl.left_object leftObject," +
//                " rl.left_object_type leftObjectType " +
//                " FROM" +
//                " object_index oi" +
//                " JOIN r006_part_revision pr ON oi.object_uid = pr.uid " +
//                " JOIN  master_r_l rl on pr.uid = rl.right_object " +
//                " WHERE" +
//                " 1 = 1 " +
//                " AND pr.uid IS NOT NULL " +
//                " AND rl.del_flag = 0 " +
//                " AND pr.active = 1 " +
//                " AND pr.del_flag = 0 ";
//    }
//
//    public void setProperty(Query query, TableRequest<LikeReq> req) {
//        //设置参数
//        if (StringUtils.isNotEmpty(req.getData().getObjectName())) {
//            query.setParameter("objectName", req.getData().getObjectName());
//        }
//        if (StringUtils.isNotEmpty(req.getData().getItemId())) {
//            query.setParameter("itemId", req.getData().getItemId());
//        }
//    }
//
//
//
//}