package com.nancal.service;

import cn.afterturn.easypoi.entity.vo.MapExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.view.PoiBaseView;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.huawei.innovation.rdm.coresdk.basic.dto.BasicObjectViewDTO;
import com.huawei.innovation.rdm.dto.entity.TenantViewDTO;
import com.huawei.innovation.rdm.hwkeymodeltest.bean.entity.WorkspaceObjectEntity;
import com.nancal.api.model.*;
import com.nancal.api.model.common.ValidList;
import com.nancal.api.utils.EntityUtil;
import com.nancal.auth.util.UserUtils;
import com.nancal.common.annotation.TimeLog;
import com.nancal.common.base.IdRequest;
import com.nancal.common.base.TableRequest;
import com.nancal.common.base.TableResponse;
import com.nancal.common.enums.*;
import com.nancal.common.exception.ServiceException;
import com.nancal.common.exception.TipServiceException;
import com.nancal.common.utils.BeanUtil;
import com.nancal.common.utils.IdGeneratorUtil;
import com.nancal.common.utils.MemoryUtil;
import com.nancal.common.utils.MessageSourceUtil;
import com.nancal.xdm.XdmEntityUtil;
import com.nancal.xdm.builder.XdmRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

public interface IWorkspaceObjectDomainService extends IBusinessObjectDomainService {


    /***
     * 创建
     *
     * @param req 请求实体
     * @author 徐鹏军
     * @date 2022/3/30 13:27
     * @return {@link WorkspaceObjectResp}
     */
    @Transactional
    default WorkspaceObjectResp save(BusinessObjectReq req) {
        String objectType = StrUtil.blankToDefault(req.getObjectType(), XdmEntityUtil.getObjectType());
        // 构建entity全包名
        String classPath = XdmEntityUtil.getEntityPackage(objectType);
        WorkspaceObjectEntity entity = ReflectUtil.newInstance(classPath);
        BeanUtil.copyPropertiesIgnoreNull(req, entity);
        entity.setObjectType(objectType);
        TenantViewDTO currentUser = XdmRequest.getCurrentUser();
        entity.setOwnerId(currentUser.getId().toString());
        entity.setOwnerName(currentUser.getCreator());
        entity.setUid(IdGeneratorUtil.generate());
        entity.setId(Long.parseLong(entity.getUid()));
        entity.setLifeCycleState(LifeCycleStateEnum.Working.name());
        entity.setDelFlag(false);
        // 保存当前对象
        XdmRequest.postForObject(objectType,ActionEnum.create,entity, BasicObjectViewDTO.class);
        // 构建响应对象的全包名
        String classRespPath = EntityUtil.getRespPackage(objectType);
        // 创建响应对象
        WorkspaceObjectResp workspaceObjectResp = ReflectUtil.newInstance(classRespPath);
        cn.hutool.core.bean.BeanUtil.copyProperties(entity, workspaceObjectResp);
        return workspaceObjectResp;
    }

    /***
     * 更新
     *
     * @param req 请求实体
     * @author 徐鹏军
     * @date 2022/3/30 13:27
     * @return {@link BusinessObjectResp}
     */
    @Transactional
    default BusinessObjectResp update(BusinessObjectReq req) {
//        WorkspaceObjectReq myReq = (WorkspaceObjectReq) req;
//        String objectType =Objects.isNull(myReq.getObjectType()) ? EntityUtil.getObjectType() : myReq.getObjectType();
//
//        List<BasicObjectViewDTO> post = XdmRequest.postForObject(objectType, ActionEnum.get, MapUtil.of("id", myReq.getUid()), BasicObjectViewDTO.class);
//        BasicObjectViewDTO entity = CollUtil.getFirst(post);
//        if (Objects.isNull(entity)) {
//            throw new ServiceException(ErrorCode.E_12);
//        }
//        // 创建响应对象
//        WorkspaceObjectResp instance = ReflectUtil.newInstance(EntityUtil.getRespPackage(objectType));
//        // 设置属性值
//        BeanUtil.copyPropertiesIgnoreNull(req, entity);
//        entity.setLastUpdateTime(new Timestamp(LocalDateTime.now().get(ChronoField.MILLI_OF_DAY)));
//        // 保存
//        XdmRequest.postForObject(objectType,ActionEnum.save,entity,BasicObjectViewDTO.class);
//        cn.hutool.core.bean.BeanUtil.copyProperties(entity, instance);
//        instance.setLeftObject(myReq.getLeftObject());
//        instance.setLeftObjectType(myReq.getLeftObjectType());
//        instance.setRightObject(myReq.getRelationId());
//        return instance;
        return null;
    }

    /***
     * 根据主键删除
     *
     * @param id 主键id
     * @author 徐鹏军
     * @date 2022/3/30 13:27
     * @return {@link BusinessObjectResp}
     */
    @Transactional
    default BusinessObjectResp deleteObject(IdRequest id) {
//        String objectType = StrUtil.blankToDefault(id.getObjectType(), EntityUtil.getObjectType());
//        WorkspaceObjectEntity entity = EntityUtil.getById(objectType,id.getUid());
//        if (Objects.isNull(entity)) {
//            throw new ServiceException(ErrorCode.E_12);
//        }
//        // 校验权限
//        this.verifyAuthority(entity,OperatorEnum.Delete,"deleteData",entity.getObjectName());
//        // 删除对象
//        entity.setDelFlag(true);
//        SpringUtil.getBean(EntityManager.class).merge(entity);
//        // 构建响应对象的全包名
//        String classPath = EntityUtil.getRespPackage(EntityUtil.getObjectType());
        // 创建响应对象
//        Object instance = ReflectUtil.newInstance(classPath);
//        cn.hutool.core.bean.BeanUtil.copyProperties(entity, instance);
//        return (BusinessObjectResp) instance;
        return null;
    }
    /***
     * 根据主键批量删除
     *
     * @param ids 主键ids
     * @author 徐鹏军
     * @date 2022/3/30 13:27
     * @return {@link BusinessObjectResp}
     */
    @Transactional
    default Boolean deleteObject(ValidList<IdRequest> ids) {
        deleteObject(ids);
        return true;
    }
    /***
     * 根据主键删除
     * <p>
     *     1：查询要删除的对象
     *     2：校验状态和权限
     *     3：校验我是否有对父级的操作权限
     *     4：根据字典查询当前类型被哪些关系引用，去这些引用表中查询左对象是否存在
     *     5：删除当前对象
     *     6：删除对象的上下关系以及版本
     *     7：创建响应返回
     * </p>
     *
     * @param id 主键id
     * @author 徐鹏军
     * @date 2022/3/30 13:27
     * @return {@link BusinessObjectResp}
     */
    @Transactional
    default BusinessObjectResp deleteObject(IdRequest id, AppNameEnum appName) {
//        String objectType = StrUtil.blankToDefault(id.getObjectType(),EntityUtil.getObjectType());
//        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
//        MasterRLFactory masterRLFactory = SpringUtil.getBean(MasterRLFactory.class);
//        IncludeRLFactory includeRLFactory = SpringUtil.getBean(IncludeRLFactory.class);
//        EntityManager entityManager = SpringUtil.getBean(EntityManager.class);
//        // 1：查询要删除的对象
//        WorkspaceObjectEntity workspaceObjectEntity = EntityUtil.getById(objectType, id.getUid());
//        if (Objects.isNull(workspaceObjectEntity)) {
//            throw new ServiceException(ErrorCode.E_12);
//        }
//        // 2：校验状态和权限
//        this.verifyAuthority(workspaceObjectEntity,OperatorEnum.Delete);
//        // 3：校验我是否有对父级的操作权限
//        masterRLFactory.create().getParents(id.getUid(),objectType,null).forEach(entity -> this.verifyAuthority(entity,OperatorEnum.Write));
//        // 4：根据字典查询当前类型被哪些关系引用，去这些引用表中查询左对象是否存在
//        List<String> checkRelations = dictUtil.getCheckAllRLs(objectType);
//        for (String relation : checkRelations) {
//            List<WorkspaceObjectEntity> relationList = EntityUtil.getDynamicEqQuery(relation, Pair.of(RelationEntity.RIGHT_OBJECT,workspaceObjectEntity.getUid())).fetch();
//            if (CollUtil.isEmpty(relationList)) {
//                continue;
//            }
//            relationList.forEach(data -> {
//                WorkspaceObjectEntity first = CollUtil.getFirst(relationList);
//                WorkspaceObjectEntity entity = EntityUtil.getById(first.getObjectType(),first.getUid());
//                if (Objects.nonNull(entity)) {
//                    String msg = "对象" + workspaceObjectEntity.getObjectName() + "被" + entity.getObjectName() + "引用，无法执行删除";
//                    throw new ServiceException(ErrorCode.FORBIDDEN, msg);
//                }
//            });
//        }
//        // 5：删除当前对象
//        workspaceObjectEntity.setDelFlag(Boolean.TRUE);
//        entityManager.merge(workspaceObjectEntity);
//
//        // 6：删除对象的上下关系以及版本
//        List<String> relations = dictUtil.getUpDownAllRLs(objectType);
//        includeRLFactory.create().deleteUpDownAllRls(id.getUid(), relations);

        // 7：创建响应返回
//        Object instance = ReflectUtil.newInstance(EntityUtil.getRespPackage(objectType));
//        workspaceObjectEntity.setDelFlag(Boolean.TRUE);
//        cn.hutool.core.bean.BeanUtil.copyProperties(workspaceObjectEntity, instance);
//        return (BusinessObjectResp) instance;
        return null;
    }

    /***
     * 根据主键查询
     *
     * @param id 主键id
     * @author 徐鹏军
     * @date 2022/3/30 13:27
     * @return {@link BusinessObjectResp}
     */
    default BusinessObjectResp getObject(IdRequest id) {
//        String objectType =Objects.isNull(id.getObjectType()) ? EntityUtil.getObjectType() : id.getObjectType();
////        WorkspaceObjectEntity entity = EntityUtil.getById(objectType,id.getUid());
////        if (Objects.isNull(entity)) {
////            throw new ServiceException(ErrorCode.E_12);
////        }
//        // 构建响应对象的全包名
//        String classPath = EntityUtil.getRespPackage(objectType);
//        // 创建响应对象
//        Object instance = ReflectUtil.newInstance(classPath);
////        cn.hutool.core.bean.BeanUtil.copyProperties(entity, instance);
////        SpringUtil.getBean(DictUtil.class).translate(instance);
//        return (BusinessObjectResp) instance;
        return null;
    }

    /***
     * 分页查询
     * 1：获取请求对象所有标注需要查询的属性，即属性中带有@QueryField注解的
     * 2：将属性值是空的过滤
     * 3：根据@QueryField的value组合查询条件
     * 4：带入分页查询
     *
     * @author 徐鹏军
     * @date 2022/5/12 13:32
     * @return {@link TableResponse< WorkspaceObjectResp>}
     */
    default TableResponse<WorkspaceObjectResp> page(TableRequest<? extends WorkspaceObjectReq> req) {
        // 获取查询条件
//        Pair<List<Triple<String, Ops, Object>>, OrderSpecifier[]> pair = EntityUtil.getAnnotationQueryParam(req.getData());
//        if (Objects.isNull(pair)) {
//            return TableResponse.<WorkspaceObjectResp>builder().total(0).data(Collections.emptyList()).build();
//        }
//        String objectType = StrUtil.blankToDefault(req.getData().getObjectType(), EntityUtil.getObjectType());
//        PageRequest request = PageHelper.ofReq(req);
//        // 1：带入分页查询
//        JPAQuery<WorkspaceObjectEntity> jpaQuery = EntityUtil.getDynamicQuery(objectType, pair.getKey())
//                .offset(request.getOffset())
//                .limit(request.getPageSize());
//        if (ArrayUtil.isNotEmpty(pair.getValue())) {
//            jpaQuery.orderBy(pair.getValue());
//        }
//        QueryResults<WorkspaceObjectEntity> queryResults = jpaQuery.fetchResults();
//        if (queryResults.getTotal() <= 0) {
//            return TableResponse.<WorkspaceObjectResp>builder().total(0).data(Collections.emptyList()).build();
//        }
//        // 如果当前对象是版本，则吧他对应的左对象查询出来
//        Map<String, WorkspaceObjectEntity> leftObjectMap = new HashMap<>();
//        Class<?> clazz = EntityUtil.getEntityClass(objectType);
//        if (ItemRevisionEntity.class.isAssignableFrom(clazz)) {
//            List<String> rightUids = queryResults.getResults().stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
//            ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
//            leftObjectMap.putAll(itemRevision.getLeftObjectMap(rightUids, objectType));
//        }
//        List<WorkspaceObjectResp> respList = fullLeftObjectResult(queryResults.getResults(),leftObjectMap);
//        return TableResponse.<WorkspaceObjectResp>builder().total(queryResults.getTotal()).data(respList).build();
        return null;
    }


    /***
     * 列表查询
     *
     * @author 徐鹏军
     * @date 2022/5/12 13:32
     * @return {@link List<BusinessObjectResp>}
     */
    default <T extends WorkspaceObjectResp> List<WorkspaceObjectResp> list(WorkspaceObjectReq req) {
        // 获取查询条件
//        Pair<List<Triple<String, Ops, Object>>, OrderSpecifier[]> pair = EntityUtil.getAnnotationQueryParam(req);
//        if (Objects.isNull(pair)) {
//            return Collections.emptyList();
//        }
//        // 1：带入分页查询
//        JPAQuery<WorkspaceObjectEntity> jpaQuery = EntityUtil.getDynamicQuery(EntityUtil.getObjectType(), pair.getKey());
//        if (ArrayUtil.isNotEmpty(pair.getValue())) {
//            jpaQuery.orderBy(pair.getValue());
//        }
//        List<WorkspaceObjectEntity> fetch = jpaQuery.fetch();
//        if (CollUtil.isEmpty(fetch)) {
//            return Collections.emptyList();
//        }
//        // 如果当前对象是版本，则吧他对应的左对象查询出来
//        Map<String, WorkspaceObjectEntity> leftObjectMap = new HashMap<>();
//        Class<?> clazz = EntityUtil.getEntityClass(EntityUtil.getObjectType());
//        if (ItemRevisionEntity.class.isAssignableFrom(clazz)) {
//            List<String> rightUids = fetch.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
//            ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
//            leftObjectMap.putAll(itemRevision.getLeftObjectMap(rightUids, EntityUtil.getObjectType()));
//        }
//        List<WorkspaceObjectResp> respList = fullLeftObjectResult(fetch,leftObjectMap);
//        return respList;
        return null;
    }

    default List<WorkspaceObjectResp> fullLeftObjectResult(Collection<WorkspaceObjectEntity> results,Map<String, WorkspaceObjectEntity> leftObjectMap){
//        if(CollUtil.isEmpty(results)){
//            return null;
//        }
//        // 转换响应对象
//        List<WorkspaceObjectResp> respList = results.stream().map(obj -> {
//            WorkspaceObjectResp instance = ReflectUtil.newInstance(EntityUtil.getRespPackage(EntityUtil.getObjectType()));
//            BeanUtil.copyPropertiesIgnoreNull(obj, instance);
//            if (CollUtil.isEmpty(leftObjectMap)) {
//                return instance;
//            }
//            //获取版本相关的Master对象进行赋值
//            WorkspaceObjectEntity leftEntity = leftObjectMap.get(obj.getUid());
//            if (ObjectUtil.isNotEmpty(leftEntity)){
//                instance.setLeftObject(leftEntity.getUid());
//                instance.setLeftObjectType(leftEntity.getObjectType());
//            }
//            return instance;
//        }).sorted(Comparator.comparing(WorkspaceObjectResp::getLastUpdate).reversed()).collect(Collectors.toList());;
//        return respList;
        return null;
    }

    /***
     * 列表下拉查询
     * @param returnUid true 需要返回UID字段 false 不需要返回UID,并且自动去重
     * @author 徐鹏军
     * @date 2022/5/12 13:32
     * @return {@link List<BusinessObjectResp>}
     */
    default List<? extends WorkspaceObjectResp> listSelect(WorkspaceObjectReq req,boolean returnUid) {
        // 获取查询条件
//        Pair<List<Triple<String, Ops, Object>>, OrderSpecifier[]> pair = EntityUtil.getAnnotationQueryParam(req);
//        if (Objects.isNull(pair)) {
//            return Collections.emptyList();
//        }
//        WorkspaceObjectResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(EntityUtil.getObjectType()));
//        Class<?> clazz = EntityUtil.getEntityClass(EntityUtil.getObjectType());
//        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
//        // 1:获取需要查询的字段
//        Path[] fields = FieldUtils.getFieldsListWithAnnotation(req.getClass(), QueryField.class).stream().map(field -> {
//            Object fieldValue = ReflectUtil.getFieldValue(req, field);
//            if (Objects.isNull(fieldValue) || StrUtil.isBlank(fieldValue.toString())) {
//                return null;
//            }
//            return ExpressionUtils.path(field.getType(), field.getName());
//        }).filter(Objects::nonNull).collect(Collectors.toList()).toArray(new Path[]{});
//        // 2：获取需要查询的表
//        EntityPathBase table = new EntityPathBase(clazz, EntityUtil.getObjectType());
//        // 3：获取查询条件
//        BooleanBuilder where = new BooleanBuilder();
//        pair.getKey().forEach(data -> {
//            PredicateOperation predicate = ExpressionUtils.predicate(
//                    data.getMiddle(),
//                    ExpressionUtils.path(clazz, data.getLeft()),
//                    ExpressionUtils.toExpression(data.getRight())
//            );
//            where.and(predicate);
//        });
//        JPAQuery<? extends WorkspaceObjectResp> query = null;
//        if (!returnUid) {
//            query = jpaQueryFactory.selectDistinct(Projections.fields(resp.getClass(), fields)).from(table).where(where);
//        } else {
//            // 针对需要返回UID字段的，此处手动加上，并且不用去重了，因为UID不可能重复，去重无意义
//            fields = ArrayUtil.append(fields, ExpressionUtils.path(String.class, BusinessObjectEntity.UID));
//            query = jpaQueryFactory.select(Projections.fields(resp.getClass(), fields)).from(table).where(where);
//        }
//        if (ArrayUtil.isNotEmpty(pair.getValue())) {
//            query.orderBy(pair.getValue());
//        }
//        return query.fetch();
        return null;
    }

    /***
     * 权限校验 true，有权限，false，无权限
     * @param entity 实体
     * @author 徐鹏军
     * @date 2022/4/14 15:52
     */
    default boolean verifyAuthorityReturn(WorkspaceObjectEntity entity, OperatorEnum operator) {
        try {
            if (Objects.isNull(entity)) {
                return false;
            }
            this.verifyAuthority(entity,operator);
        } catch (ServiceException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ErrorCode.ERROR);
        }
        return true;
    }

    /**
     * 单个默认调用提示语
     * @param entity
     * @param operator
     * @param tipType
     * @param args
     */
    default void verifyAuthority(WorkspaceObjectEntity entity, OperatorEnum operator, String tipType,String... args) {
        verifyAuthority(entity,operator,DataTypeEnum.CURRENT_DATA,tipType,args);
    }

    /**
     * 单个外部调用提示语
     * @param entity
     * @param operator
     * @param tipType
     * @param args
     */
    default void verifyAuthority(WorkspaceObjectEntity entity, OperatorEnum operator, DataTypeEnum dataType,String tipType,Object... args) {
        try {
            verifyAuthority(entity, operator, dataType);
        }catch (TipServiceException e){
            if(args == null || args.length == 0){
                args = new Object[]{e.getMessage()};
            }else{
                List<Object> tipList = new ArrayList<Object>(Arrays.asList(args));
                tipList.add(e.getMessage());
                args = tipList.toArray();
            }
            throw new TipServiceException(e.getCode(),MessageSourceUtil.getMessage(tipType,args));
        }
    }

    /**
     * 单个内部调用提示语
     * dataType，目前支持，currentData or parentData 类型
     * @param entity
     * @param operator
     */
    default void verifyAuthority(WorkspaceObjectEntity entity, OperatorEnum operator, DataTypeEnum dataType) {
        try {
            verifyAuthorityBase(entity,operator);
        }catch (TipServiceException e){
            throw new TipServiceException(e.getCode(),MessageSourceUtil.getMessage(dataType.getCode(),e.getMessage()));
        }
    }

    default void verifyAuthorityList(List<WorkspaceObjectEntity> list, OperatorEnum operator,String tipType){
        verifyAuthorityList(list, operator, DataTypeEnum.CURRENT_DATA,tipType);
    }

    /**
     * 默认验证当前数据权限
     * @param list
     * @param operator
     */
    default void verifyAuthorityList(List<WorkspaceObjectEntity> list, OperatorEnum operator,String tipType,String... args){
        verifyAuthorityList(list, operator, DataTypeEnum.CURRENT_DATA,tipType,args);
    }

    default void verifyAuthorityList(List<WorkspaceObjectEntity> list, OperatorEnum operator) {
        verifyAuthorityList(list,operator, DataTypeEnum.CURRENT_DATA);
    }

    default void verifyAuthorityList(List<WorkspaceObjectEntity> list, OperatorEnum operator,DataTypeEnum dataType) {
        if (CollUtil.isEmpty(list)){
            return;
        }
        StringBuilder tip = new StringBuilder();
        list.forEach(entity -> {
            try {
                verifyAuthority(entity,operator,dataType);
            }catch (TipServiceException e){
                tip.append(e.getMessage());
            }
        });
        if(tip.length()>0){
            throw new TipServiceException(ErrorCode.FAIL,tip.toString());
        }
    }

    /**
     * 列表验证权限
     * @param list
     * @param operator
     * @param dataType
     */
    default void verifyAuthorityList(List<WorkspaceObjectEntity> list, OperatorEnum operator,DataTypeEnum dataType,String tipType,Object... args) {
        if (CollUtil.isEmpty(list)){
            return;
        }
        StringBuilder tip = new StringBuilder();
        list.forEach(entity -> {
            try {
                verifyAuthority(entity,operator,dataType);
            }catch (TipServiceException e){
                tip.append(e.getMessage());
            }
        });
        if(tip.length()>0){
            if(args == null || args.length == 0){
                args = new Object[]{tip.toString()};
            }else{
                List<Object> tipList = new ArrayList<Object>(Arrays.asList(args));
                tipList.add(tip.toString());
                args = tipList.toArray();
            }
            throw new TipServiceException(ErrorCode.FAIL,MessageSourceUtil.getMessage(tipType,args));
        }
    }

    /**
     * 默认验证当前数据权限
     * @param entity
     * @param operator
     */
    default void verifyAuthority(WorkspaceObjectEntity entity, OperatorEnum operator) {
        try {
            verifyAuthorityBase(entity,operator);
        }catch (TipServiceException e){
            throw new TipServiceException(e.getCode(),MessageSourceUtil.getMessage(DataTypeEnum.CURRENT_DATA.getCode(),e.getMessage()));
        }
    }

    /***
     * 权限校验
     * <p>
     *     ①：所有者
     *     ②：所有组
     *     ③：系统管理员
     *     ④：所有用户
     * </p>
     * @param entity 实体
     * @author 徐鹏军
     * @date 2022/4/14 15:52
     */
    default void verifyAuthorityBase(WorkspaceObjectEntity entity, OperatorEnum operator) {
        UserUtils userUtils = SpringUtil.getBean(UserUtils.class);
        if (LifeCycleStateEnum.Working.name().equalsIgnoreCase(entity.getLifeCycleState())
                || LifeCycleStateEnum.Obsolete.name().equalsIgnoreCase(entity.getLifeCycleState())
        ) {
            switch (operator) {
                case SaveAs:
                    // 所有者 所有组 所有用户 有权限 TODO 待对接IAM才能判断当前用户是否是系统管理员
                case Read:
                    // 所有人均有此权限
                    return;
                case Write:
                    // 只有所有者有此权限
                case Delete:
                    // 只有所有者有此权限
                case SubmitTrial:
                    // 只有所有者有此权限
                case ChangeOwner:
                    /**
                     * 所有者,系统管理员有此权限
                     * 所有组,所有用户没有权限
                     * TODO 待对接IAM才能判断当前用户是否是系统管理员
                     * 目前只先判断所有者这一个
                     */
                case DownLoad:
                    /**
                     * 所有者,所有组有此权限
                     * 系统管理员,所有用户没有权限
                     * TODO 待对接IAM才能判断当前用户是否是所有组
                     * 目前只先判断所有者这一个
                     */
                    if (!userUtils.getCurrentUserId().equals(entity.getOwnerId())) {
                        throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("dataNotCurrentUser",entity.getObjectName()));
                    }
                    break;
                case Upgrade:
                    // 所有人均没有此权限
                    throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("dataInWork",entity.getObjectName()));
                default:
            }
        }
        if (LifeCycleStateEnum.InJob.name().equalsIgnoreCase(entity.getLifeCycleState())) {
            switch (operator) {
                case SaveAs:
                    // 所有者 所有组 所有用户 有权限 TODO 待对接IAM才能判断当前用户是否是系统管理员
                case Read:
                    // 所有人均有此权限
                    return;
                case Write:
                    // 所有人均没有此权限
                case Delete:
                    // 所有人均没有此权限
                case SubmitTrial:
                    // 所有人均没有此权限
                case Upgrade:
                    // 所有人均没有此权限
                case ChangeOwner:
                    // 所有人均没有此权限
                    throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("dataInJob",entity.getObjectName()));
                case DownLoad:
                    /**
                     * 所有者,所有组有此权限
                     * 系统管理员,所有用户没有权限
                     * TODO 待对接IAM才能判断当前用户是否是所有组
                     * 目前只先判断所有者这一个
                     */
                    if (!userUtils.getCurrentUserId().equals(entity.getOwnerId())) {
                        throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("dataNotCurrentUser",entity.getObjectName()));
                    }
                default:
            }
        }
        if (LifeCycleStateEnum.Released.name().equalsIgnoreCase(entity.getLifeCycleState())) {
            switch (operator) {
                case SaveAs:
                    // 所有者 所有组 所有用户 有权限 TODO 待对接IAM才能判断当前用户是否是系统管理员
                case Read:
                    // 所有人均有此权限
                    return;
                case Write:
                    // 所有人均没有此权限
                case Delete:
                    // 所有人均没有此权限
                case SubmitTrial:
                    // 所有人均没有此权限
                    throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("dataReleased",entity.getObjectName()));
                case Upgrade:
                    // 只有所有者有此权限
                case ChangeOwner:
                    /**
                     * 所有者,系统管理员有此权限
                     * 所有组,所有用户没有权限
                     * TODO 待对接IAM才能判断当前用户是否是系统管理员
                     * 目前只先判断所有者这一个
                     */
                    if (!userUtils.getCurrentUserId().equals(entity.getOwnerId())) {
                        throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("dataNotCurrentUser",entity.getObjectName()));
                    }
                case DownLoad:
                    /**
                     * 所有者,所有组有此权限
                     * 系统管理员,所有用户没有权限
                     * TODO 待对接IAM才能判断当前用户是否是所有组
                     * 目前只先判断所有者这一个
                     */
                    if (!userUtils.getCurrentUserId().equals(entity.getOwnerId())) {
                        throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("dataNotCurrentUser",entity.getObjectName()));
                    }
                default:
            }
        }
        if (LifeCycleStateEnum.Prepare.name().equalsIgnoreCase(entity.getLifeCycleState())) {
            switch (operator) {
                case SaveAs:
                    // 所有者 所有组 所有用户 有权限 TODO 待对接IAM才能判断当前用户是否是系统管理员
                case Read:
                    // 所有人均有此权限
                    return;
                case Delete:
                    // 所有人均没有此权限
                case SubmitTrial:
                    // 所有人均没有此权限
                case Upgrade:
                    // 所有人均没有此权限
                case ChangeOwner:
                    // 所有人均没有此权限
                    throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("dataInJob",entity.getObjectName()));
                case Write:
                    // 所有者,所有组有此权限
                case DownLoad:
                    /**
                     * 所有者,所有组有此权限
                     * 系统管理员,所有用户没有权限
                     * TODO 待对接IAM才能判断当前用户是否是所有组
                     * 目前只先判断所有者这一个
                     */
                    if (!userUtils.getCurrentUserId().equals(entity.getOwnerId())) {
                        throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage("dataNotCurrentUser",entity.getObjectName()));
                    }
                default:
            }
        }
    }

    /***
     *页签的详情
     *
     * @param req 分页查询条件
     * @author: 王辉
     * @date: 2022/4/14 16:33
     * @return:  {@link TableResponse<IncludeOrMasterRLResp>}
     */
    default TableResponse<IncludeOrMasterRLResp> getTabDetail(TableRequest<IdRequest> req) {
        List<IncludeOrMasterRLResp> list = getRightObjects(req.getData());
        if (CollUtil.isEmpty(list)) {
            return TableResponse.<IncludeOrMasterRLResp>builder()
                    .total(0)
                    .data(Collections.emptyList())
                    .build();
        }
        List<IncludeOrMasterRLResp> pageResult = MemoryUtil.page(list, req.getSkip(), req.getTake());
        return TableResponse.<IncludeOrMasterRLResp>builder()
                .total(list.size())
                .data(pageResult)
                .build();
    }

    /***
     *页签的关系功能：查询所有右对象
     *
     * @param id id
     * @author: 王辉
     * @date: 2022/4/15 8:32
     * @return:  {@link List<IncludeOrMasterRLResp>}
     */
    default List<IncludeOrMasterRLResp> getRightObjects(IdRequest id) {
//        MasterRL masterRL = SpringUtil.getBean(MasterRLFactory.class).create();
//        String objectType = StrUtil.blankToDefault(id.getObjectType(),EntityUtil.getObjectType());
//        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
//        List<WorkspaceObjectEntity> childs = masterRL.getChilds(id.getUid(), objectType,entity -> {
//            // 如果是版本，则只获取激活状态下的
//            if (entity instanceof ItemRevisionEntity) {
//                ItemRevisionEntity revisionEntity = (ItemRevisionEntity) entity;
//                return revisionEntity.getActive();
//            }
//            return true;
//        });
//        //处理BOMNode节点信息
//        Class<?> entityClass = EntityUtil.getEntityClass(id.getObjectType());
//        List<IncludeOrMasterRLResp> byBomNode = new ArrayList<>();
//        if (ItemEntity.class.isAssignableFrom(entityClass)){
//
//        }else if(ItemRevisionEntity.class.isAssignableFrom(entityClass)){
//            //需要查找BOMNode表
//             byBomNode = this.findByBomNode(id.getUid(), ReflectUtil.newInstance(BOMNodeEntity.class).getObjectType(), id.getObjectType());
//        }else if (DatasetEntity.class.isAssignableFrom(entityClass)){
//
//        }else if (FolderEntity.class.isAssignableFrom(entityClass)){
//
//        }else {
//            //处理工步
//            byBomNode = this.findByBomNode(id.getUid(), ReflectUtil.newInstance(BOMNodeEntity.class).getObjectType(), id.getObjectType());
//        }
//        if (CollUtil.isEmpty(childs)&&CollUtil.isEmpty(byBomNode)) {
//            return Collections.emptyList();
//        }
//        // 转换数据
//        List<IncludeOrMasterRLResp> respList = childs.stream().map(data -> {
//            IncludeOrMasterRLResp resp = new IncludeOrMasterRLResp();
//            BeanUtil.copyPropertiesIgnoreNull(data, resp);
//            resp.setOwnerGroupName("工艺技术处.型号组");
//            resp.setLeftObject(id.getUid());
//            resp.setLeftObjectType(EntityUtil.getObjectType());
//            // 判断右对象列表数据是否还有子节点
//            Class<?> entityClass1 = EntityUtil.getEntityClass(data.getObjectType());
//            boolean hasChildren = masterRL.hasChildren(data.getUid(), data.getObjectType(), false);
//            if (ItemEntity.class.isAssignableFrom(entityClass1)){
//
//            }else if(ItemRevisionEntity.class.isAssignableFrom(entityClass1)){
//                //需要查找BOMNode表
//                if (!hasChildren){
//                    //获取相同版本的所有版次
//                    List<WorkspaceObjectEntity> allSequences = itemRevision.getAllSequences(data.getUid(), data.getObjectType());
//                    List<String> collect = allSequences.stream().map(BusinessObjectEntity::getUid).collect(Collectors.toList());
//                    hasChildren = EntityUtil.getDynamicQuery(ReflectUtil.newInstance(BOMNodeEntity.class).getObjectType(), Triple.of(BOMNodeEntity.PARENT_ITEM_REV, Ops.IN, collect)).fetchCount()>0;
//                }
//            }else if (DatasetEntity.class.isAssignableFrom(entityClass1)){
//
//            }else if (FolderEntity.class.isAssignableFrom(entityClass1)){
//
//            }else {
//                //处理工步
//                if (!hasChildren){
//                    hasChildren = masterRL.bomNodeHasChildren(resp.getUid(), resp.getObjectType(), false, resp.getUid());
//                }
//            }
//            resp.setHasChildren(hasChildren);
//            return resp;
//        }).collect(Collectors.toList());
//        respList.addAll(byBomNode);
//        respList = respList.stream().sorted(Comparator.comparing(IncludeOrMasterRLResp::getCreationDate).reversed()).collect(Collectors.toList());
//        SpringUtil.getBean(DictUtil.class).translate(respList);
//        return respList;
        return null;
    }

    /***
     *页签的关系功能：查询所有左对象
     *
     * @param id id
     * @author: 王辉
     * @date: 2022/4/15 8:32
     * @return:  {@link List<IncludeOrMasterRLResp>}
     */
    default List<IncludeOrMasterRLResp> getLeftObjects(IdRequest id) {
//        MasterRL masterRL = SpringUtil.getBean(MasterRLFactory.class).create();
//        String objectType = StrUtil.blankToDefault(id.getObjectType(),EntityUtil.getObjectType());
//        List<WorkspaceObjectEntity> parents = masterRL.getParents(id.getUid(),objectType ,entity -> {
//            // 如果是版本，则只获取激活状态下的
//            if (entity instanceof ItemRevisionEntity) {
//                ItemRevisionEntity revisionEntity = (ItemRevisionEntity) entity;
//                return revisionEntity.getActive();
//            }
//            return true;
//        });
//        //处理BOMNODE：如果是零组件或者是工步获取到零组件查找具体信息
//        Class<?> entityClass = EntityUtil.getEntityClass(id.getObjectType());
//        List<WorkspaceObjectEntity> parentItem = new ArrayList<>();
//        if (ItemEntity.class.isAssignableFrom(entityClass)){
//            parentItem = this.getParentItem(ReflectUtil.newInstance(BOMNodeEntity.class).getObjectType(),CollUtil.newArrayList(id.getUid()));
//        }else if(ItemRevisionEntity.class.isAssignableFrom(entityClass)){
//
//        }else if (DatasetEntity.class.isAssignableFrom(entityClass)){
//
//        }else if (FolderEntity.class.isAssignableFrom(entityClass)){
//
//        }else {
//             parentItem = this.getParentItem(ReflectUtil.newInstance(BOMNodeEntity.class).getObjectType(), CollUtil.newArrayList(id.getUid()));
//        }
//        parents.addAll(StreamEx.of(parentItem).distinct(WorkspaceObjectEntity::getUid).collect(Collectors.toList()));
//        if (CollUtil.isEmpty(parents)) {
//            return Collections.emptyList();
//        }
//        // 转换数据
//        List<IncludeOrMasterRLResp> respList = parents.stream().map(data -> {
//            IncludeOrMasterRLResp resp = new IncludeOrMasterRLResp();
//            BeanUtil.copyPropertiesIgnoreNull(data, resp);
//            resp.setOwnerGroupName(StrUtil.EMPTY);
//            resp.setLeftObject(id.getUid());
//            resp.setLeftObjectType(StrUtil.blankToDefault(id.getObjectType(),EntityUtil.getObjectType()));
//            boolean hasChildren = false;
//            // 判断右对象列表数据是否还有子节点
//            //零组件版本不需要判断
//            Class<?> entityClass1 = EntityUtil.getEntityClass(resp.getObjectType());
//            hasChildren = masterRL.hasParents(data.getUid(), data.getObjectType(), false);
//            if(ItemRevisionEntity.class.isAssignableFrom(entityClass1)){
//                hasChildren = true;
//            }else if (ItemEntity.class.isAssignableFrom(entityClass1)){
//                //零组件需要判断BOMNode与关联关系
//                if (!hasChildren){
//                    //判断是否存在BOM行属性
//                    hasChildren = this.getParentItem(ReflectUtil.newInstance(BOMNodeEntity.class).getObjectType(),CollUtil.newArrayList(data.getUid())).size()>0 ? true : false;
//                }
//            } else if (DatasetEntity.class.isAssignableFrom(entityClass1)|| FolderEntity.class.isAssignableFrom(entityClass1)){
//
//            }else {
//                //工步是否存在子集
//                if (!hasChildren){
//                    //判断是否存在BOM行属性
//                    hasChildren = this.getParentItem(ReflectUtil.newInstance(BOMNodeEntity.class).getObjectType(),CollUtil.newArrayList(data.getUid())).size()>0 ? true : false;
//                }
//            }
//            resp.setHasChildren(hasChildren);
//            return resp;
//        }).sorted(Comparator.comparing(IncludeOrMasterRLResp::getCreationDate).reversed()).collect(Collectors.toList());
//        SpringUtil.getBean(DictUtil.class).translate(respList);
//        return respList;
        return null;
    }




//    default TableResponse<WorkspaceObjectResp> pageTwo(TableRequest<? extends WorkspaceObjectReq> req) {
//        JPAQueryFactory jpaQueryFactory = SpringUtil.getBean(JPAQueryFactory.class);
//        MasterRLFactory masterRLFactory = SpringUtil.getBean(MasterRLFactory.class);
//        // 先递归获取当前节点下所有子节点的id
//        List<WorkspaceObjectEntity> relationEntityList = masterRLFactory.create().recursionChildBreak(req.getData().getUid(),"LibraryFolder",null);
//        List<String> rightObjectList = relationEntityList.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
//        // 将自身的id放入
//        rightObjectList.add(req.getData().getUid());
//        // 根据uid去中间表获取所有右边id
//        QLibraryFolderRLEntity libraryIncludeRL = QLibraryFolderRLEntity.libraryFolderRLEntity;
//        BooleanBuilder builder = new BooleanBuilder();
//        builder.and(libraryIncludeRL.leftObject.in(rightObjectList));
//        builder.and(libraryIncludeRL.leftObjectType.eq("LibraryFolder"));
//        List<LibraryFolderRLEntity> includeRLEntities = jpaQueryFactory.selectFrom(libraryIncludeRL).where(builder).fetch();
//        if (CollUtil.isEmpty(includeRLEntities)) {
//            return TableResponse.<WorkspaceObjectResp>builder().total(0).build();
//        }
//        PageRequest pageRequest = PageHelper.ofReq(req);
//        // 根据所有右id集合单表查询分页
//        List<String> uidList = includeRLEntities.stream().map(LibraryFolderRLEntity::getRightObject).collect(Collectors.toList());
//        OrderSpecifier order = new OrderSpecifier(Order.DESC, ExpressionUtils.path(EntityUtil.getEntityClass(EntityUtil.getObjectType()), WorkspaceObjectEntity.CREATION_DATE));
//        List<Triple<String, Ops,Object>> params = new ArrayList<>();
//        params.add(Triple.of(BusinessObjectEntity.UID, Ops.IN,uidList));
//        if (StrUtil.isNotBlank(req.getData().getObjectName())) {
//            params.add(Triple.of(WorkspaceObjectEntity.OBJECT_NAME, Ops.LIKE, SqlUtil.like(req.getData().getObjectName())));
//        }
//        JPAQuery<WorkspaceObjectEntity> jpaQuery = EntityUtil.getDynamicQuery(EntityUtil.getObjectType(), params).orderBy(order);
//        long total = jpaQuery.fetchCount();
//        if (total <= 0) {
//            return TableResponse.<WorkspaceObjectResp>builder().total(0).build();
//        }
//        List<WorkspaceObjectEntity> itemList = jpaQuery.offset(pageRequest.getOffset()).limit(pageRequest.getPageSize()).fetch();
//        // 获取版本类型
//        String objectType = EntityUtil.getRevision(EntityUtil.getObjectType());
//        Item item = SpringUtil.getBean(ItemFactory.class).create();
//        // 根据所有的组件，分别查询对应的最新版本,组装响应对象
//        List<String> itemUids = itemList.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
//        Map<String, WorkspaceObjectEntity> itemLastVersionMap = item.getLastVersion(itemUids, EntityUtil.getObjectType());
//        List<WorkspaceObjectResp> respList = itemList.stream().map(entity -> {
//            WorkspaceObjectEntity lastVersion = itemLastVersionMap.get(entity.getUid());
//            ItemRevisionResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(objectType));
//            if (Objects.nonNull(lastVersion)) {
//                BeanUtil.copyPropertiesIgnoreNull(lastVersion, resp);
//            }
//            // 设置组件的id和类型
//            resp.setLeftObject(entity.getUid());
//            resp.setLeftObjectType(entity.getObjectType());
//            return resp;
//        }).collect(Collectors.toList());
//        // 字典翻译
//        SpringUtil.getBean(DictUtil.class).translate(respList);
//        return TableResponse.<WorkspaceObjectResp>builder().total(total).data(respList).build();
//    }

    /**
     *
     * @param files 附件
     * @param leftObjectType 左对象类型
     * @param leftObject 左对象uid
     * @author: 薛锦龙
     * @time: 2022/6/15
     * @return: {@link WorkspaceObjectResp}
     */
//    default WorkspaceObjectResp saveAttachment(List<FileAttrReq> files, String leftObjectType, String leftObject){
//        //字典获取附件类型
//        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
//        Map<String, String> dataSetType = dictUtil.getCodeValueMap("DataSetType");
//        Set<Map.Entry<String, String>> entries = dataSetType.entrySet();
//        String value = "";
//        for (Map.Entry<String, String> entry : entries) {
//            boolean contains = entry.getKey().contains(files.get(0).getFileType());
//            if (contains) {
//                value = entry.getValue();
//                break;
//            }
//        }
//        String filePath = files.get(0).getFilePath();
//        AttachmentReq attachmentReq = new AttachmentReq();
//        attachmentReq.setObjectName(filePath.substring(filePath.lastIndexOf("/") + 1));
//        attachmentReq.setFiles(files);
//        attachmentReq.setLeftObjectType(leftObjectType);
//        attachmentReq.setObjectType(value);
//        attachmentReq.setLeftObject(leftObject);
//        WorkspaceObjectResp save = SpringUtil.getBean(IDatasetDomainService.class).save(attachmentReq);
//        return save;
//    }

    /***
     * 分页查询  (针对返回没有版本的模糊查询接口)
     * 1：获取请求对象所有标注需要查询的属性，即属性中带有@QueryField注解的
     * 2：将属性值是空的过滤
     * 3：根据@QueryField的value组合查询条件
     * 4：带入分页查询
     *
     * @author 拓凯
     * @date 2022/6/20
     * @return {@link TableResponse < WorkspaceObjectResp>}
     */
    @TimeLog
    default TableResponse<WorkspaceObjectResp> findLike(TableRequest<? extends WorkspaceObjectReq> req) {
        // 获取查询条件
//        Pair<List<Triple<String, Ops, Object>>, OrderSpecifier[]> pair = EntityUtil.getAnnotationQueryParam(req.getData());
//        if (Objects.isNull(pair)) {
//            return TableResponse.<WorkspaceObjectResp>builder().total(0).data(Collections.emptyList()).build();
//        }
//        String objectType = StrUtil.blankToDefault(req.getData().getObjectType(), EntityUtil.getObjectType());
//        PageRequest request = PageHelper.ofReq(req);
//        // 1：带入分页查询
//        JPAQuery<WorkspaceObjectEntity> jpaQuery = EntityUtil.getDynamicQuery(objectType, pair.getKey())
//                .offset(request.getOffset())
//                .limit(request.getPageSize());
//        if (ArrayUtil.isNotEmpty(pair.getValue())) {
//            jpaQuery.orderBy(pair.getValue());
//        }
//        QueryResults<WorkspaceObjectEntity> queryResults = jpaQuery.fetchResults();
//        if (queryResults.getTotal() <= 0) {
//            return TableResponse.<WorkspaceObjectResp>builder().total(0).data(Collections.emptyList()).build();
//        }
//        List<WorkspaceObjectEntity> results = queryResults.getResults();
//        List<WorkspaceObjectResp> collect = results.stream().map(obj -> {
//            WorkspaceObjectResp instance = ReflectUtil.newInstance(EntityUtil.getRespPackage(objectType));
//            BeanUtil.copyPropertiesIgnoreNull(obj, instance);
//            return instance;
//        }).collect(Collectors.toList());
//        // 字段翻译
//        SpringUtil.getBean(DictUtil.class).translate(collect);
//        return TableResponse.<WorkspaceObjectResp>builder().total(queryResults.getTotal()).data(collect).build();
        return null;
    }

    /**
     * 通过版本获取BOMNode关系表关联的数据
     * @param id
     * @author: 薛锦龙
     * @time: 2022/7/12
     * @return: {@link List< WorkspaceObjectEntity>}
     */
//    default List<WorkspaceObjectEntity> getBomNodeData(IdRequest id){
//        //获取相同的版本所有版次
//        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
//        BOMNode bomNode = SpringUtil.getBean(BOMNodeFactory.class).create();
//        List<WorkspaceObjectEntity> itemRevision1 = itemRevision.getAllSequences(id.getUid(),id.getObjectType());
//        List<String> collect = itemRevision1.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
//        //获取BOMNode表的信息
//        List<BOMNodeResp> firstNode = bomNode.getFirstNode( ReflectUtil.newInstance(BOMNodeEntity.class).getObjectType(), collect, null);
//        List<WorkspaceObjectEntity> workspaceObjectEntities = new ArrayList<>();
//        firstNode.stream().collect(Collectors.groupingBy(BOMNodeResp::getChildItemType)).forEach((objectType,value)->{
//            List<String> stringStream = value.stream().map(BOMNodeResp::getChildItem).collect(Collectors.toList());
//            List<WorkspaceObjectEntity> fetch = EntityUtil.getDynamicQuery(objectType, Triple.of(BusinessObjectEntity.UID, Ops.IN, stringStream)).fetch();
//            workspaceObjectEntities.addAll(fetch);
//        });
//        return workspaceObjectEntities;
//    }

    /**
     * 通过子对象获取BOMNode信息
     * @param childItem 子集id
     * @param objectType 关系表类型
     * @author: 薛锦龙
     * @time: 2022/7/12
     * @return: {@link List< BOMNodeResp>}
     */
//    default List<WorkspaceObjectEntity> getParentItem(String objectType,List<String> childItem){
//        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
//        List<WorkspaceObjectEntity> entities = new ArrayList<>();
//        List<WorkspaceObjectEntity> fetch = EntityUtil.getDynamicQuery(objectType, Triple.of(BOMNodeEntity.CHILD_ITEM, Ops.IN, childItem)).fetch();
//        List<BOMNodeEntity> collect = fetch.stream().map(BOMNodeEntity.class::cast).collect(Collectors.toList());
//        collect.stream().collect(Collectors.groupingBy(BOMNodeEntity::getParentItemType)).forEach((type,value)->{
//            Class<?> entityClass = EntityUtil.getEntityClass(type);
//            List<String> list = value.stream().map(BOMNodeEntity::getParentItem).collect(Collectors.toList());
//            if (ItemEntity.class.isAssignableFrom(entityClass)){
//                //获取版本类型
//                List<String> lists = value.stream().map(BOMNodeEntity::getParentItemRev).collect(Collectors.toList());
//                String revisionType = EntityUtil.getRevision(type);
//                //通过版本获取当前激活版本的最新版次
//                entities.addAll(itemRevision.getItemRevision(revisionType, lists));
//            }else if(ItemRevisionEntity.class.isAssignableFrom(entityClass)){
//
//            }else if (DatasetEntity.class.isAssignableFrom(entityClass)){
//
//            }else if (FolderEntity.class.isAssignableFrom(entityClass)){
//
//            }else {
//                //处理工步
//                entities.addAll(EntityUtil.getByIds(type, list));
//            }
//        });
//        //根据UID进行数据去重
//        List<WorkspaceObjectEntity> list = StreamEx.of(entities).distinct(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
//        return list;
//    }

    /**
     * 查找BOMNODE
     * @param uid 数据UID
     * @param objectType BOMNode的类型
     * @param leftObjectType 数据类型
     * @author: 薛锦龙
     * @time: 2022/6/21
     * @return: {@link List< IncludeOrMasterRLResp>}
     */
//    default List<IncludeOrMasterRLResp> findByBomNode(String uid,String objectType,String leftObjectType){
//        BOMNode bomNode = SpringUtil.getBean(BOMNodeFactory.class).create();
//        List<IncludeOrMasterRLResp> resps = new ArrayList<>();
//        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
//        List<WorkspaceObjectEntity> fetch= new ArrayList<>();
//        //工步特殊处理
//        List<BOMNodeResp> firstNode;
//        if (Gte4MfgStepEntity.class.isAssignableFrom(EntityUtil.getEntityClass(leftObjectType))){
//            firstNode = bomNode.getFirstNode(null, CollUtil.toList(uid),objectType);
//        }else {
//            //根据版本查询零组件，及版本号，
//            //根据parentItem与版本号，查询bomNode，返回的为标准的零组件
//            WorkspaceObjectEntity entity = EntityUtil.getById(leftObjectType,uid);
//            ItemRevisionEntity itemRevisionEntity = (ItemRevisionEntity)entity;
//            ItemEntity leftObjectByRightObject = itemRevision.getLeftObjectByRightObject(uid, leftObjectType);
//            List<Triple<String,Ops,Object>> triples = Arrays.asList(
//                    Triple.of(BOMNodeEntity.PARENT_ITEM,Ops.EQ,leftObjectByRightObject.getUid()),
//                    Triple.of(BOMNodeEntity.PARENT_ITEM_REV_ID,Ops.EQ,itemRevisionEntity.getRevisionId())
//            );
//            OrderSpecifier order = new OrderSpecifier(Order.ASC, ExpressionUtils.path(BOMNodeEntity.class, BOMNodeEntity.FOUND_NO));
//            List<WorkspaceObjectEntity> objectEntities = EntityUtil.getDynamicQuery(objectType, triples).orderBy(order).fetch();
//            firstNode = objectEntities.stream().map(data-> {
//                BOMNodeResp resp = new BOMNodeResp();
//                BeanUtil.copyPropertiesIgnoreNull(data,resp);
//                return resp;
//            }).collect(Collectors.toList());
//
//
//            //获取相同版本的所有版次
////            List<WorkspaceObjectEntity> revisions = itemRevision.getAllSequences(uid, leftObjectType);
////            List<String> revisionUids = revisions.stream().map(BusinessObjectEntity::getUid).collect(Collectors.toList());
////            //查询BOMNode表，找到所有的子集
////            firstNode = bomNode.getFirstNode(objectType, revisionUids, null);
//        }
//        //根据类型分组查找子集
//        List<String> childUids = new ArrayList<>();
//        //过滤精确BOM的数据
//        Map<String, List<BOMNodeResp>> listMap = firstNode.stream().filter(data -> StrUtil.isNotBlank(data.getChildItemTypeRevision())).collect(Collectors.groupingBy(BOMNodeResp::getChildItemTypeRevision));
//        listMap.forEach((key,value)->{
//            List<String> uids = value.stream().map(BOMNodeResp::getChildItemRevision).collect(Collectors.toList());
//            childUids.addAll(uids);
//            List<WorkspaceObjectEntity> fetch1 = EntityUtil.getDynamicQuery(key, Triple.of(BusinessObjectEntity.UID, Ops.IN, uids)).fetch();
//            fetch.addAll(fetch1);
//        });
//        //非精确BOM的数据
//        Map<String, List<BOMNodeResp>> collect = firstNode.stream().filter(data->data.getChildItemTypeRevision()==null).collect(Collectors.groupingBy(BOMNodeResp::getChildItemType));
//        //遍历查找所有相关的子节点
//        collect.forEach((key, value) -> {
//            List<String> uids = value.stream().map(BOMNodeResp::getChildItem).collect(Collectors.toList());
//            childUids.addAll(uids);
//            //查找子集
//            List<WorkspaceObjectEntity> fetch1 = EntityUtil.getDynamicQuery(key, Triple.of(BusinessObjectEntity.UID, Ops.IN, uids)).fetch();
//            fetch.addAll(fetch1);
//        });
//
//        if (CollUtil.isEmpty(fetch)) {
//            return CollUtil.newArrayList();
//        }
//        MasterRL masterRL = SpringUtil.getBean(MasterRLFactory.class).create();
//        //转换返回值类型
//        fetch.forEach(data -> {
//            IncludeOrMasterRLResp resp1 = new IncludeOrMasterRLResp();
//            BeanUtil.copyPropertiesIgnoreNull(data, resp1);
//            resp1.setOwnerGroupName("工艺技术处.型号组");
//            resp1.setLeftObject(uid);
//            resp1.setLeftObjectType(leftObjectType);
//            resp1.setHasChildren(true);
//            resp1.setRelationId(IdGeneratorUtil.generate());
//            if (!ItemEntity.class.isAssignableFrom(EntityUtil.getEntityClass(data.getObjectType()))){
//                //说明是工步，需要单独判断是否有子集
//                boolean hasChildren = masterRL.bomNodeHasChildren(resp1.getUid(), resp1.getObjectType(), false, resp1.getUid());
//                resp1.setHasChildren(hasChildren);
//            }
//            resps.add(resp1);
//        });
//
//        //排序
//        Map<String, IncludeOrMasterRLResp> dataRespMap = resps.stream().collect(Collectors.toMap(WorkspaceObjectResp::getUid, Function.identity()));
//        return childUids.stream().filter(data -> data != null && dataRespMap.containsKey(data))
//                .map(data -> dataRespMap.get(data)).collect(Collectors.toList());
//    }

    /**
     * 编辑权限校验
     * @param id
     * @author: 薛锦龙
     * @time: 2022/7/20
     * @return: {@link boolean}
     */
//    default boolean editPermissionCheck(IdRequest id){
//        WorkspaceObjectEntity entity = EntityUtil.getDynamicQuery(id.getObjectType(), Triple.of(BusinessObjectEntity.UID, Ops.EQ, id.getUid())).fetchFirst();
//        boolean flag = true;
//        try {
//            this.verifyAuthority(entity,OperatorEnum.Write);
//        }catch (RuntimeException e){
//            flag = false;
//        }
//        return flag;
//    }

    /**
     * 新建权限校验
     * @param id
     * @author: 薛锦龙
     * @time: 2022/7/20
     * @return: {@link }
     */
//    default void newPermissionCheck(IdRequest id){
//        WorkspaceObjectEntity entity = EntityUtil.getDynamicQuery(id.getObjectType(), Triple.of(BusinessObjectEntity.UID, Ops.EQ, id.getUid())).fetchFirst();
//        WorkspaceObjectResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(entity.getObjectType()));
//        BeanUtil.copyPropertiesIgnoreNull(entity,resp);
//        this.verifyAuthority(entity,OperatorEnum.Write,"addData",resp.getDisplayName());
//    }


   /**
    *
    * @param modelMap
    * @param request
    * @param response
    * @param fileds 表头
    * @param params 表单设置
    * @param mapData 数据的map
    * @param fileName 文件名
    * @author: 薛锦龙
    * @time: 2022/7/25
    * @return: {@link }
    */
    default void export( ModelMap modelMap, HttpServletRequest request, HttpServletResponse response,List<ExcelExportEntity> fileds,
                         ExportParams params,List<Map<String, Object>> mapData,String fileName) throws UnsupportedEncodingException {
        //表头集合
        modelMap.put(MapExcelConstants.ENTITY_LIST, fileds);
        //导出的数据集合
        modelMap.put(MapExcelConstants.MAP_LIST, mapData);
        //写入的表单
        modelMap.put(MapExcelConstants.PARAMS, params);
        //文件名
        modelMap.put(MapExcelConstants.FILE_NAME,fileName);
        response.setHeader("fileName", URLEncoder.encode(fileName, "UTF-8") + ".xls");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition,fileName");
        PoiBaseView.render(modelMap, request, response, MapExcelConstants.EASYPOI_MAP_EXCEL_VIEW);
    }


    /**
     * 数据平铺并设置层级适用于树结构处理数据
     * @param bomNodeViewResp
     * @param list
     * @author: 薛锦龙
     * @time: 2022/7/22
     * @return: {@link }
     */
    default void groupingEntity(BOPNodeViewResp bomNodeViewResp, List<BOPNodeViewResp> list){
        if (CollUtil.isEmpty(bomNodeViewResp.getChildren())){
            return;
        }
        for (int i = 0; i < bomNodeViewResp.getChildren().size(); i++) {
            bomNodeViewResp.getChildren().get(i).setLevel(bomNodeViewResp.getLevel()+ StrUtil.DOT+(i+1));
            list.add((BOPNodeViewResp)bomNodeViewResp.getChildren().get(i));
            groupingEntity( (BOPNodeViewResp)bomNodeViewResp.getChildren().get(i),list);
        }
    }


    /**
     * 用于结构导出
     * @param req
     * @param modelMap
     * @param request
     * @param response
     * @param nameEnum App类型
     * @author: 薛锦龙
     * @time: 2022/7/25
     * @return: {@link }
     */
//    default void structureExport(ExportBOMReq req, ModelMap modelMap, HttpServletRequest request,
//                                 HttpServletResponse response,AppNameEnum nameEnum,String sheetName){
//        IBOMNodeDomainService service = SpringUtil.getBean(IBOMNodeDomainService.class);
//        BOPNodeViewResp bomNodeViewResp = service.find(BOPNodeViewResp.class, new FindReq(req.getUid(), req.getObjectType(),-1), nameEnum);
//        bomNodeViewResp.setLevel("1");
//        List<BOPNodeViewResp> list = new ArrayList<>();
//        list.add(bomNodeViewResp);
//        this.groupingEntity(bomNodeViewResp,list);
//        System.err.println("-------------处理数据结束------------");
//        //文件名
//        LocalDate date = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
//        String df = date.format(formatter);
//        String fileName =bomNodeViewResp.getDisplayName()+"-"+df;
//        List<Map<String, Object>> mapData = new ArrayList<>();
//        //表单
//        ExportParams params = new ExportParams(null, sheetName, ExcelType.HSSF);
//        //数据处理成Map
//        for (BOPNodeViewResp bopNodeViewResp : list) {
//            try {
//                ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);
//                Map map = objectMapper.convertValue(bopNodeViewResp, Map.class);
//
//                mapData.add(map);
//            } catch (RuntimeException e) {
//                throw new ServiceException(ErrorCode.E_10, "对象转化Map失败");
//            }
//        }
//        //设置表头样式
//        List<ExcelExportEntity> fileds = new ArrayList<>();
//        fileds.add(new ExcelExportEntity("层级","level"));
//        List<ColumnReq> columnReqList = req.getColumnReqList();
//        for (ColumnReq columnReq : columnReqList) {
//            ExcelExportEntity excelExportEntity = new ExcelExportEntity(columnReq.getValue(),columnReq.getCode());
//            fileds.add(excelExportEntity);
//        }
//        try {
//            this.export(modelMap,request,response,fileds,params,mapData,fileName);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 通用返回提示语
     * @param tipBuilder
     * @param tipData
     * @author: 拓凯
     * @time: 2022/7/25
     * @return: {@link}
     */
    default void tipBack(StringBuilder tipBuilder,String tipData) {
        if(tipBuilder.length()>0){
            throw new TipServiceException(ErrorCode.FAIL, MessageSourceUtil.getMessage(tipData,tipBuilder.toString()));
        }
    }

    /**
     * EasyExcel导出
     * @param req
     * @param response
     * @param nameEnum
     * @author: 薛锦龙
     * @time: 2022/7/29
     * @return: {@link}
     */
//    @SneakyThrows
//    default void export(ExportBOMReq req, HttpServletResponse response,AppNameEnum nameEnum) {
//        //获取当前用户在当前系统的列设置
//        FrameworkConverterProperties frameworkConverterProperties = SpringUtil.getBean("frameworkConverterProperties",FrameworkConverterProperties.class);
//        List<String> filedNames = ListUtil.list(true);
//        List<List<String>> columnList = ListUtil.list(true);
//        //表头
////        String parentTitle = "";
//        List<ColumnReq> columnReqLists = new ArrayList<>();
//        columnReqLists.add(new ColumnReq("level","层级"));
//        columnReqLists.addAll(req.getColumnReqList());
//        //数据处理
//        IBOMNodeDomainService service = SpringUtil.getBean(IBOMNodeDomainService.class);
//        BOPNodeViewResp bomNodeViewResp = service.find(BOPNodeViewResp.class, new FindReq(req.getUid(), req.getObjectType(),-1), nameEnum);
//        bomNodeViewResp.setLevel("1");
//        List<BOPNodeViewResp> list = new ArrayList<>();
//        list.add(bomNodeViewResp);
//        this.groupingEntity(bomNodeViewResp,list);
//        System.err.println("-------------处理数据结束------------");
//        //处理文件名
//        LocalDate date = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
//        String df = date.format(formatter);
//        String fileName =bomNodeViewResp.getDisplayName()+"-"+df;
//        //数据处理
//        ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);
//        ArrayList<List<Object>> data = new ArrayList<>();
//        //对象转换
//        List<Object> bo = new ArrayList<>();
//        List<Map<String,Object>> maps = new ArrayList<>();
//        list.forEach(base->{
//            Object o = objectMapper.convertValue(base, Object.class);
//            maps.add(objectMapper.convertValue(base, Map.class));
//            bo.add(o);
//
//        });
//        for (ColumnReq columnReq : columnReqLists) {
//                String title = columnReq.getValue();
//                String filed = columnReq.getCode();
//                if (StrUtil.isNotBlank(filed)) {
//                    List<String> lists = ListUtil.list(true);
////                        lists.add(parentTitle);
//                        lists.add(title);
//                        columnList.add(lists);
//                        filedNames.add(filed);
//                } else {
////                    parentTitle = title;
//                }
//            }
//        System.err.println("表头处理结束");
//        for (Map<String, Object> stringObjectMap : maps) {
//            List<Object> lists = new ArrayList<>();
//            for (String filedName1 : filedNames) {
//                if (stringObjectMap.containsKey(filedName1)) {
//                    Object object = stringObjectMap.get(filedName1);
//                    if (object instanceof Boolean) {
//                        lists.add(object == Boolean.TRUE ? "是" : "否");
//                    } else {
//                        lists.add(object);
//                    }
//                }else{
//                    lists.add("");
//                }
//
//            }
//            data.add(lists);
//        }
//
//        fileName = URLEncoder.encode(fileName, "UTF-8") + ".xlsx";
//        response.setHeader("fileName", fileName);
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        response.setCharacterEncoding("utf-8");
//        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition,fileName");
//        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + fileName);
//        ServletOutputStream outputStream = null;
//        try {
//            Map<Integer,Short> map = new HashMap();
//            map.put(1,(short)2);
//            WriteCellStyle style = new WriteCellStyle();
//            style.setFillBackgroundColor(IndexedColors.GOLD.getIndex());
//            Map<Integer,Short> colorMap=new HashMap<>();
//            colorMap.put(1, IndexedColors.BLUE.index);
//            outputStream = response.getOutputStream();
//            ExcelWriter writer = EasyExcelFactory
//                    .write(outputStream)
//                    // 表头和内容的样式在此
//                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
////                    .registerWriteHandler(new ExcelUtils(map))
//                    .build();
//            WriteSheet sheet1 = new WriteSheet();
//            sheet1.setSheetName("BOM导出信息");
//            sheet1.setSheetNo(0);
//
//            // 创建一个表格，用于 Sheet 中使用
//            WriteTable table = new WriteTable();
//            table.setTableNo(1);
//            // 设置表头
//            table.setHead(columnList);
//            // 写数据
//            writer.write(data, sheet1, table);
//            writer.finish();
//            outputStream.close();
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            try {
//                System.err.println("关闭流");
//                outputStream.close();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }

    /**
     * 通过左对象的id与类型获取数据集与存储路径
     * @param id
     * @author: 薛锦龙
     * @time: 2022/9/16
     * @return: {@link List< WorkspaceObjectResp>}
     */
//    default List<WorkspaceObjectResp> getDataSet(IdRequest id){
//        MasterRL masterRL = SpringUtil.getBean(MasterRLFactory.class).create();
//        List<WorkspaceObjectEntity> childs = masterRL.getChilds(id.getUid(), id.getObjectType(), entity -> {
//            if (entity instanceof ItemRevisionEntity) {
//                ItemRevisionEntity revisionEntity = (ItemRevisionEntity) entity;
//                return revisionEntity.getActive();
//            }
//            return true;
//        });
//        if (CollUtil.isEmpty(childs)){
//            return null;
//        }
//        //过滤出数据集数据
//        List<WorkspaceObjectEntity> entities = StreamEx.of(childs).filter(data -> DatasetEntity.class.isAssignableFrom(EntityUtil.getEntityClass(data.getObjectType()))).toList();
//        String objectType = new DatasetFileRLEntity().getObjectType();
//        List<String> entityUids = entities.stream().map(WorkspaceObjectEntity::getUid).collect(Collectors.toList());
//        List<WorkspaceObjectEntity> entityList = EntityUtil.getDynamicQuery(objectType, Triple.of(RelationEntity.LEFT_OBJECT, Ops.IN, entityUids)).fetch();
//        List<RelationEntity> relationEntities = StreamEx.of(entityList).map(RelationEntity.class::cast).toList();
//        Map<String, List<RelationEntity>> stringListMap = StreamEx.of(relationEntities).groupingBy(RelationEntity::getRightObjectType);
//        List<WorkspaceObjectEntity> fileStory = new ArrayList<>();
//        stringListMap.forEach((type,values)->{
//            List<String> rightUids = StreamEx.of(values).map(RelationEntity::getRightObject).toList();
//            List<WorkspaceObjectEntity> byIds = EntityUtil.getByIds(type, rightUids);
//            fileStory.addAll(byIds);
//        });
//        Map<String, WorkspaceObjectEntity> fileStoryMap = StreamEx.of(fileStory).toMap(WorkspaceObjectEntity::getUid, Function.identity());
//        Map<String, WorkspaceObjectEntity> entityMap = StreamEx.of(entities).toMap(WorkspaceObjectEntity::getUid, Function.identity());
//        List<WorkspaceObjectResp> resps = new ArrayList<>();
//        relationEntities.forEach(data->{
//            if (fileStoryMap.containsKey(data.getRightObject()) && entityMap.containsKey(data.getLeftObject())){
//                WorkspaceObjectResp workspaceObjectResp = ReflectUtil.newInstance(EntityUtil.getRespPackage(data.getLeftObjectType()));
//                BeanUtil.copyPropertiesIgnoreNull(entityMap.get(data.getLeftObject()),workspaceObjectResp);
//                FileStorageResp resp = ReflectUtil.newInstance(EntityUtil.getRespPackage(data.getRightObjectType()));
//                BeanUtil.copyPropertiesIgnoreNull(fileStoryMap.get(data.getRightObject()),resp);
//                workspaceObjectResp.setFile(resp);
//                resps.add(workspaceObjectResp);
//            }
//        });
//        return resps;
//    }

}
