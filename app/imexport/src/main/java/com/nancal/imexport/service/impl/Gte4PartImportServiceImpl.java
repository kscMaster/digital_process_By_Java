package com.nancal.imexport.service.impl;


import cn.hutool.core.lang.Pair;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.api.utils.excel.analysis.ExcelPoiWordAnalysis;
import com.nancal.common.base.IdRequest;
import com.nancal.common.config.TokenContext;
import com.nancal.common.config.TokenInfo;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.common.enums.LifeCycleStateEnum;
import com.nancal.common.utils.BeanUtil;
import com.nancal.imexport.service.IImexportServiceAdaptor;
import com.nancal.imexport.service.IImexportTaskDomainServiceAdaptor;
import com.nancal.model.entity.*;
import com.nancal.service.bo.Item;
import com.nancal.service.factory.ItemFactory;
import com.nancal.service.service.IBOMNodeDomainService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service
public class Gte4PartImportServiceImpl extends ExcelPoiWordAnalysis<Gte4PartRevisionImportReq> implements IImexportServiceAdaptor {

    @Autowired
    private IBOMNodeDomainService ibomNodeDomainService;

    @Autowired
    private DictUtil dictUtil;

    @Autowired
    private IImportRevisionServiceImpl gte4PartRevisionImport;

    @Autowired
    private IImexportTaskDomainServiceAdaptor iImexportTaskDomainService;

    /**
     * ebom导入
     * @param tokenInfo 请求头
     * @param uid 任务uid
     */
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void importExcel(TokenInfo tokenInfo,String uid) {
        try {
            TokenContext.setToken(tokenInfo);
            MultipartFile file = TokenContext.getToken().getFile();
            List<Gte4PartRevisionImportReq> analysis = excelImportVerify(file);
            Item item = SpringUtil.getBean(ItemFactory.class).create();
            List<String> collect = analysis.stream().filter(t -> StringUtils.isNotBlank(t.getItemId())).map(Gte4PartRevisionImportReq::getItemId).collect(Collectors.toList());
            Map<String,List<ItemRevisionEntity>> itemsLastVersion = item.listByItemIds(collect, new Gte4PartRevisionEntity().getObjectType());
            analysis.forEach(t ->{
                t.setObjectType(new Gte4PartEntity().getObjectType());
                List<ItemRevisionEntity> itemRevisionEntities = itemsLastVersion.get(t.getItemId());
                if (null != itemRevisionEntities && !itemRevisionEntities.isEmpty()){
                    List<ItemRevisionEntity> isExist = itemRevisionEntities.stream().filter(r -> r.getRevisionId().equals(t.getRevisionId())).collect(Collectors.toList());
                    if (!isExist.isEmpty()){
                        ItemRevisionEntity itemRevisionEntity = isExist.get(0);
                        t.setUid(itemRevisionEntity.getUid());
                        t.setLeftObject(itemRevisionEntity.getLeftObject());
                        t.setLeftObjectType(itemRevisionEntity.getLeftObjectType());
                    }
                }
            });
            List<Gte4PartRevisionImportReq> treeList = new ArrayList<>();
            for (int i = 1; i < 10000; i++) {
                String current = String.valueOf(i);
                List<Gte4PartRevisionImportReq> currentList = analysis.stream().filter(it -> current.equals(it.getRank())).collect(Collectors.toList());
                currentList.forEach(gprir -> {
                    recursionChildren(gprir, analysis);
                });
                treeList.addAll(currentList);
            }

            AtomicReference<WorkspaceObjectResp> workspaceObjectResp = new AtomicReference<>();
            // 递归执行
            treeList.forEach(tree ->{
                if (null != tree.getUid()) {
                    workspaceObjectResp.set(topUpdate(tree));
                    comparison(tree);
                }else {
                    workspaceObjectResp.set(topInsert(tree));
                }
            });
            iImexportTaskDomainService.success(uid);
        }catch (Exception e){
            e.printStackTrace();
            iImexportTaskDomainService.error(uid,e.getMessage());
        }
    }
    /**
     * 解析excel 获取集合  失败数 成功数  失败描述
     * @param file
     * @return
     */
    public List<Gte4PartRevisionImportReq> excelImportVerify(MultipartFile file){
        super.setDictUtil(dictUtil);
        super.setNumber("层级");
        return super.analysis(file);
    }

    /**
     *  递归对比执行方法
     *  执行 executeMethod 方法 执行下一层及的递归判断
     * @param tree 导入结构
     */
    public WorkspaceObjectResp comparison(Gte4PartRevisionImportReq tree){
        return this.comparison(tree,null);
    }

    /**
     *  递归对比方法
     * @param importExcelData 导入结构
     * @param parentObject 父级对象
     */
    public WorkspaceObjectResp comparison(Gte4PartRevisionImportReq importExcelData,WorkspaceObjectResp parentObject){

        // 获取当前节点的bom行数据。
        FindReq findReq = new FindReq();
        findReq.setDeep(10);
        findReq.setUid(importExcelData.getLeftObject());
        findReq.setObjectType(importExcelData.getLeftObjectType());
        BOPNodeViewResp bopNodeViewResp = ibomNodeDomainService.find(BOPNodeViewResp.class, findReq, AppNameEnum.EBOM);
        Item item = SpringUtil.getBean(ItemFactory.class).create();
        /**
         *  此方法出现的情况：
         *      当父类为新增节点,但子类为已存在节点。更新子级数据并且添加父类于子类的 bom行关联
         *      会调用这个方法的地方只有当父类未添加 子类已存在会调用此方法，其他情况都不会进入此方法中
         */
        if (null != parentObject){
            WorkspaceObjectResp workspaceObjectResp = new WorkspaceObjectResp();
            BeanUtil.copyPropertiesIgnoreNull(importExcelData,workspaceObjectResp);
            this.update(item,importExcelData,bopNodeViewResp,importExcelData.getUid());
            this.addBomNodeRelation(importExcelData,workspaceObjectResp, parentObject.getRightObject(), parentObject.getUid(),parentObject.getRightObjectRevId(),true);
        }
        /**
         *  此方法出现的情况：
         *      当父类为已存在节点,更新子级数据并且添加父类于子类的 bom行关联
         *      会调用这个方法的地方只有当父类已存在 子类已存在会调用此方法，进入executeMethod 在进行递归对比方法。其他情况都不会进入此方法中
         */
        if (null == bopNodeViewResp.getChildren()){
            WorkspaceObjectResp workspaceObjectResp = new WorkspaceObjectResp();
            workspaceObjectResp.setRightObject(importExcelData.getUid());
            workspaceObjectResp.setUid(importExcelData.getLeftObject());
            workspaceObjectResp.setRightObjectRevId(importExcelData.getRevisionId());
            executeMethod(importExcelData.getGte4PartRevisionImportReqList(), workspaceObjectResp);
        }else {
            // 第二层级 对比递归方法
            // 因为当父类未关联 并且已存在 进入executeMethod 在进行递归对比方法。所以不需要在次执行 递归对比
            recursionComparison(item,bopNodeViewResp.getChildren(), importExcelData.getGte4PartRevisionImportReqList(),bopNodeViewResp);

        }
        WorkspaceObjectResp workspaceObject = new WorkspaceObjectResp();
        BeanUtil.copyPropertiesIgnoreNull(importExcelData,workspaceObject);
        return workspaceObject;
    }



    /**
     * 新增顶级节点
     * @param tree
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public WorkspaceObjectResp topInsert(Gte4PartRevisionImportReq tree){
        //1、单独创建设计零件对象
        WorkspaceObjectResp save = this.save(tree);
        if (null != tree.getGte4PartRevisionImportReqList() && !tree.getGte4PartRevisionImportReqList().isEmpty()) {
            executeMethod(tree.getGte4PartRevisionImportReqList(), save);
        }
        AssociatedBoardsReq associatedBoardsReq = new AssociatedBoardsReq();
        associatedBoardsReq.setUid(save.getUid());
        associatedBoardsReq.setObjectType(save.getObjectType());
        associatedBoardsReq.setBoardKey("PB-000923");
        this.associatedBoards(associatedBoardsReq);
        return save;
    }

    /**
     * 更新顶级节点
     * @param tree
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public WorkspaceObjectResp topUpdate(Gte4PartRevisionImportReq tree){
        Gte4PartBomReq gte4PartBomReq = new Gte4PartBomReq();
        tree.setUid(tree.getUid());
        tree.setLeftObject(tree.getLeftObject());
        tree.setLeftObjectType(tree.getLeftObjectType());
        gte4PartBomReq.setPartRevisionReq(tree);
        BomEditRevisionResp gte4PartRevision = (BomEditRevisionResp)gte4PartRevisionImport.updateGte4Part(gte4PartBomReq);
        WorkspaceObjectResp workspaceObjectResp = new WorkspaceObjectResp();
        workspaceObjectResp.setUid(gte4PartRevision.getObjectResp().getLeftObject());
        workspaceObjectResp.setObjectType(gte4PartRevision.getObjectResp().getLeftObjectType());
        return workspaceObjectResp;
    }

    /**
     *
     * 会进入此方法的情况有2种：
     *     第一种为：已存在节点的新增父级了节点.会进入判断 走 递归新增还是递归对比
     *     第二种为：节点原先没有下级 新增了一个下级节点。会进入此方法进入递归新增方法种
     * @param gte4PartRevisionImportReq 导入数据
     * @param workspaceObjectResp 父级节点
     *  判断执行方法
     *  第二层及下级递归执行此方法判断 执行逻辑
     *      下一层级：存在执行对比递归方法
     *              不存在执行新增递归方法
     */
    public void executeMethod(List<Gte4PartRevisionImportReq> gte4PartRevisionImportReq,WorkspaceObjectResp workspaceObjectResp){
        if (null != gte4PartRevisionImportReq && !gte4PartRevisionImportReq.isEmpty()) {
            gte4PartRevisionImportReq.forEach(tree -> {
                if (null != tree.getUid()) {
                    comparison(tree, workspaceObjectResp);
                } else {
                    recursionRankInsert(tree, workspaceObjectResp.getRightObject(), workspaceObjectResp.getUid(),
                            workspaceObjectResp.getRightObjectRevId(), true);
                }
            });
        }
    }

    /**
     *  新增二级及以下节点方法
     *      isRecursion：
     *      true:
     *          当顶级节点不存在时，为 true。因为不会进入对比方法。需要进入 executeMethod 方法
     *          当executeMethod方法中判断下一级是否存在  如果下一级不存在 为true 继续递归
     *          当进行对比时 原始节点数据不存在 导入数据节点存在时 为 true 递归新增 导入节点下的全部数据
     *       false:
     *          当对比数据时 原始节点存在  导入节点存在。对比下层数据时 为false。当出现第三级节点时 需要因为二级节点导入顺序的不同,节点位置出现偏差
     *          对比时需要进行 原始三级节点循环去匹配所以导入的三级节点已保证不会因为位置偏差出现 误删 误增，所以不能进行递归判断 需要一级一级判断
     * @param gte4PartRevisionImportReq 导入单个新增数据对象
     *
     * @param parentItemRevUid 上级版本uid
     * @param parentItemUid 上级主键uid
     * @param isRecursion 是否需要递归新增 true需要 false不需要
     */
    @Transactional(rollbackFor = Exception.class)
    public void recursionRankInsert(Gte4PartRevisionImportReq gte4PartRevisionImportReq
            ,String parentItemRevUid,String parentItemUid,String parentItemRevId,boolean isRecursion){
        //        1、单独创建设计零件对象
        WorkspaceObjectResp workspaceObjectResp = this.save(gte4PartRevisionImportReq);
        addBomNodeRelation( gte4PartRevisionImportReq,  workspaceObjectResp, parentItemRevUid, parentItemUid,parentItemRevId,false);
        gte4PartRevisionImportReq.setUid(workspaceObjectResp.getRightObject());
        gte4PartRevisionImportReq.setLeftObject(workspaceObjectResp.getUid());
        gte4PartRevisionImportReq.setLeftObjectType(workspaceObjectResp.getObjectType());
        if (isRecursion){
            executeMethod(gte4PartRevisionImportReq.getGte4PartRevisionImportReqList(), workspaceObjectResp);
        }
   }

    /**
     * 添加bom行关联
     * @param gte4PartRevisionImportReq 导入单个新增数据对象
     * @param workspaceObjectResp ParentItem主键对象
     * @param parentItemRevUid 上级版本uid
     * @param parentItemUid 上级主键uid
     * @param isRevision 是否是版本对象
     */
   @Transactional(rollbackFor = Exception.class)
   public void addBomNodeRelation(Gte4PartRevisionImportReq gte4PartRevisionImportReq, WorkspaceObjectResp workspaceObjectResp,
                                  String parentItemRevUid,String parentItemUid,String parentItemRevId,boolean isRevision){
       BOMNodeReq bomNodeReq = new BOMNodeReq();
       if(null != gte4PartRevisionImportReq.getQuantity() && 0 != gte4PartRevisionImportReq.getQuantity()){
           bomNodeReq.setQuantity(gte4PartRevisionImportReq.getQuantity());
       }
       if(null != gte4PartRevisionImportReq.getFoundNo() && 0 != gte4PartRevisionImportReq.getFoundNo()){
           bomNodeReq.setFoundNo(gte4PartRevisionImportReq.getFoundNo());
       }
       if(StringUtils.isNotBlank(gte4PartRevisionImportReq.getTagNo())){
           bomNodeReq.setTagNo(gte4PartRevisionImportReq.getTagNo());
       }
       bomNodeReq.setParentItemRevId(parentItemRevId);
       bomNodeReq.setParentItemRev(parentItemRevUid);
       bomNodeReq.setParentItemType(workspaceObjectResp.getObjectType());
       bomNodeReq.setChildItem(workspaceObjectResp.getUid());
       if (isRevision){
           bomNodeReq.setParentItemType(EntityUtil.getObjectTypeByRevisionType(workspaceObjectResp.getObjectType()));
           bomNodeReq.setChildItem(workspaceObjectResp.getLeftObject());
       }
       bomNodeReq.setParentItem(parentItemUid);

       bomNodeReq.setChildItemType(new Gte4PartEntity().getObjectType());
       ibomNodeDomainService.createNode(bomNodeReq, AppNameEnum.EBOM);
   }


    /**
     * @param bopNodeViewResp bom行第二级级列表信息
     * @param trees 第二级列表信息
     * @param parentBOMNode 虚拟bom父级信息
     *  递归对比第二级方法
     *  1.当 原始数据不为空 并且 导入数据为空 删除所以原始数据
     *  2.当 原始数据为空 并且 导入数据不为空 循环 新增所以导入数据
     *  3.当都不为空时 判断集合数据
     *      3.1 集合原始数据存在 导入数据不存在 删除原始数据
     *      3.2 都存在更新原始数据
     *              判断原始二级节点已存在于导入节点数据中更新
     *              判断原始二级节点不存在导入节点数据中删除
     *              判断导入节点不存在于原始节点中新增
     *      3.3 原始数据不存在 导入数据存在 新增导入数据
     *
     */
   public void recursionComparison(Item item,List<BOMNodeResp> bopNodeViewResp,List<Gte4PartRevisionImportReq> trees, BOMNodeResp parentBOMNode){
       // 父级版本信息

       ItemRevisionEntity lastVersion = (ItemRevisionEntity)item.getLastVersion(parentBOMNode.getChildItem(), parentBOMNode.getChildItemType());
       List<Gte4PartRevisionEntity> originalPartRevision = new ArrayList<>();
       // 原始数据不为空 并且 导入数据为空 删除所以原始数据
        if (null != bopNodeViewResp && null == trees){
            bopNodeViewResp.forEach(bomNodeResp -> {
                IdRequest idRequest = IdRequest.builder().uid(bomNodeResp.getUid()).objectType(new BOMNodeEntity().getObjectType()).build();
                ibomNodeDomainService.deleteObject(idRequest);
            });
        }
        /**
         * 原始数据为空 并且 导入数据不为空 循环
         *  进入 insertOrRelation 方法 判断 导入数据 是进行新增操作 还是 进行添加关联操作
         */
        else if (null == bopNodeViewResp && null != trees){
            trees.forEach(gte4PartRevisionImportReq ->{
                bopNodeViewResp.forEach(bomNodeResp -> {
                    // 通过bom行获取版本信息
                    Gte4PartRevisionEntity gte4PartRevisionEntity = (Gte4PartRevisionEntity)item.getLastVersion(bomNodeResp.getChildItem(), bomNodeResp.getChildItemType());
                    originalPartRevision.add(gte4PartRevisionEntity);
                });
                insertOrRelation(item,originalPartRevision, gte4PartRevisionImportReq,
                        trees,parentBOMNode.getChildItem(),lastVersion.getUid(),lastVersion.getRevisionId());
            });
            //判断集合数据
        }else if (null != bopNodeViewResp && null != trees){
            // 判断原始数据和导入数据的对比 执行 删除 或者 更新方法
            bopNodeViewResp.forEach(bomNodeResp -> {
                // 通过bom行获取版本信息
                Gte4PartRevisionEntity gte4PartRevisionEntity = (Gte4PartRevisionEntity)item.getLastVersion(bomNodeResp.getChildItem(), bomNodeResp.getChildItemType());
                originalPartRevision.add(gte4PartRevisionEntity);
                List<Gte4PartRevisionImportReq> collect = trees.stream().filter(t ->
                                null != t.getItemId() &&
                                t.getItemId().equals(gte4PartRevisionEntity.getItemId()) &&
                                t.getRevisionId().equals(gte4PartRevisionEntity.getRevisionId()))
                        .collect(Collectors.toList());
                if (!collect.isEmpty()){
                    this.update(item,collect.get(0), bomNodeResp, gte4PartRevisionEntity.getUid());
                }else {
                    IdRequest idRequest = IdRequest.builder().uid(bomNodeResp.getUid()).objectType(new BOMNodeEntity().getObjectType()).build();
                    ibomNodeDomainService.deleteObject(idRequest);
                }
            });
            // 判断导入数据和 原始数据的 执行新增方法
            trees.forEach(gte4PartRevisionResp ->{
                if (null == gte4PartRevisionResp.getItemId()){
                    recursionRankInsert(gte4PartRevisionResp,lastVersion.getUid(),parentBOMNode.getChildItem(),lastVersion.getRevisionId(),false);
                }else {
                    insertOrRelation(item,originalPartRevision, gte4PartRevisionResp, trees,parentBOMNode.getChildItem(),
                            lastVersion.getUid(),lastVersion.getRevisionId());
                }
            });
        }
       if ( (null != bopNodeViewResp || null != trees )) {
           recursionComparisonChildren(bopNodeViewResp, trees, item);
       }
   }


    /**
     * 递归对比第三层及以下节点
     * @param bopNodeViewResp
     * @param trees
     */
   public void recursionComparisonChildren(List<BOMNodeResp> bopNodeViewResp,List<Gte4PartRevisionImportReq> trees,Item item){
       List<Gte4PartRevisionImportReq> reqList = new ArrayList<>();
       if (null != trees) {
           trees.forEach(tree -> {
               if (null != tree.getGte4PartRevisionImportReqList()) {
                   tree.getGte4PartRevisionImportReqList().forEach(t -> {
                       reqList.add(t);
                   });
               }
           });
       }
       List<BOMNodeResp> bomNodeRespList = new ArrayList<>();
       if (null != bopNodeViewResp) {
           bopNodeViewResp.forEach(bomNodeResp -> {
               if (null != bomNodeResp.getChildren()) {
                   bomNodeResp.getChildren().forEach(t -> {
                       bomNodeRespList.add(t);
                   });
               }
           });
       }
       List<Gte4PartRevisionEntity> originalPartRevision = new ArrayList<>();
       if (null != bopNodeViewResp) {
           bopNodeViewResp.forEach(bomNodeResp -> {
               List<BOMNodeResp> children = bomNodeResp.getChildren();
               // 原始数据不为空 并且 导入数据为空 删除所以原始数据
               if (null != children && reqList.isEmpty()) {
                   children.forEach(childrenBomNodeResp -> {
                       IdRequest idRequest = IdRequest.builder().uid(childrenBomNodeResp.getUid()).objectType(new BOMNodeEntity().getObjectType()).build();
                       ibomNodeDomainService.deleteObject(idRequest);
                       bomNodeRespList.remove(childrenBomNodeResp);
                   });
               } else if (null != children && null != reqList) {
                   // 判断原始数据和导入数据的对比 执行更新方法
                   children.forEach(childrenBomNodeResp -> {
                       // 通过bom行获取版本信息
                       Gte4PartRevisionEntity gte4PartRevisionEntity = (Gte4PartRevisionEntity) item.getLastVersion(childrenBomNodeResp.getChildItem(), childrenBomNodeResp.getChildItemType());
                       originalPartRevision.add(gte4PartRevisionEntity);
                       List<Gte4PartRevisionImportReq> collect = reqList.stream().filter(t ->
                               null != t.getItemId() &&
                                       t.getItemId().equals(gte4PartRevisionEntity.getItemId()) &&
                                       t.getRevisionId().equals(gte4PartRevisionEntity.getRevisionId()))
                               .collect(Collectors.toList());
                       // 坑点当出现 同一个itemId 和 版本 在多个子级下面 会出现 删除不了 情况  因为判断的整个下级所以集合 所以 根据 itemId和版本 查询肯定可以查询出来
                       // 所以当把同一个itemId的数据 移动不同到不同的父级下面时 原父级数据不会被删除  新父级会被添加
                       if (!collect.isEmpty()) {
                           //  解决 当可以匹配ItemId 需要在去判断 这条数据的bom行父级uid 是否等级 导入的结构的父级 uid
                           Gte4PartRevisionImportReq parentObject = this.getImportParent(collect.get(0), trees);
                           if (parentObject.getLeftObject() .equals(childrenBomNodeResp.getParentItem())) {
                               this.update(item, collect.get(0), childrenBomNodeResp, gte4PartRevisionEntity.getUid());
                           }else {
                               IdRequest idRequest = IdRequest.builder().uid(childrenBomNodeResp.getUid()).objectType(new BOMNodeEntity().getObjectType()).build();
                               ibomNodeDomainService.deleteObject(idRequest);
                               bomNodeRespList.remove(childrenBomNodeResp);
                           }
                       } else {
                           IdRequest idRequest = IdRequest.builder().uid(childrenBomNodeResp.getUid()).objectType(new BOMNodeEntity().getObjectType()).build();
                           ibomNodeDomainService.deleteObject(idRequest);
                           bomNodeRespList.remove(childrenBomNodeResp);
                       }
                   });
               }
           });
       }
       /**
        * 第二级以下：
        *      原始数据为空，导入数据不为空，执行insertOrRelation 判断 新增 还是 添加关联
        *      新入库父级节点为 trees(第二级节点) 新增或者编辑第二级节点时 将 该节点的 左对象uid 左对象类型 右对象uid 全部重新赋值上去
        *      再次新增以下节点时 可以直接绑定父级节点并赋值。
        */
       if (originalPartRevision.isEmpty() && !reqList.isEmpty()){
           reqList.forEach(gte4PartRevisionResp ->{
               Gte4PartRevisionImportReq gte4PartRevisionImportReq = getImportParent(gte4PartRevisionResp,trees);
                insertOrRelation( item,originalPartRevision, gte4PartRevisionResp, trees,gte4PartRevisionImportReq.getLeftObject()
                        ,gte4PartRevisionImportReq.getUid(),gte4PartRevisionImportReq.getRevisionId());
                });
           }
       /**
        * 当第二级以下：
        *   原始数据和导入数据都不为空时。 循环判断需要新增的 导入数据节点。
        *   当itemId和版本都匹配不上时。则证明该导入节点并不存在于原始节点
        *   新增该节点
        */
       if (!originalPartRevision.isEmpty() && !reqList.isEmpty()) {
           // 判断导入数据和 原始数据的 执行新增方法
           reqList.forEach(gte4PartRevisionResp -> {

               if (null == gte4PartRevisionResp.getItemId()){
                   Gte4PartRevisionImportReq gte4PartRevisionImportReq = getImportParent(gte4PartRevisionResp,trees);
                   recursionRankInsert(gte4PartRevisionResp, gte4PartRevisionImportReq.getUid(),
                           gte4PartRevisionImportReq.getLeftObject(),gte4PartRevisionImportReq.getRevisionId(),false);
               }else {
                   insertOrRelation( item,originalPartRevision, gte4PartRevisionResp, trees);
               }


           });
       }
       if (!bomNodeRespList.isEmpty() || !reqList.isEmpty()){
           recursionComparisonChildren(bomNodeRespList , reqList,item);
       }

   }

    /**
     * 获取父级节点对象
     * @param gte4PartRevisionResp 子级级导入层级结构
     * @param trees 父级层级集合
     * @return
     */
   public Gte4PartRevisionImportReq getImportParent(Gte4PartRevisionImportReq gte4PartRevisionResp,List<Gte4PartRevisionImportReq> trees){
       String rank = gte4PartRevisionResp.getRank();
       String childrenRank = rank.substring(0, rank.lastIndexOf("."));
       return trees.stream().filter(originals -> originals.getRank().equals(childrenRank)).findFirst().get();
   }


    /**
     * 修改节点属性和bom属性
     * @param gte4PartRevisionImportReq 更新的版本信息
     * @param grr 主键信息
     */
    @Transactional(rollbackFor = Exception.class)
   public void update(Item item, Gte4PartRevisionImportReq gte4PartRevisionImportReq,BOMNodeResp grr,String revisionUid){
        gte4PartRevisionImportReq.setUid(revisionUid);
        gte4PartRevisionImportReq.setLeftObject(grr.getChildItem());
        gte4PartRevisionImportReq.setLeftObjectType(grr.getChildItemType());
       if (this.dataVerify(item,grr)) {
           // 调用修改方法  修改版本和bom行信息
           Gte4PartBomReq gte4PartBomReq = new Gte4PartBomReq();
           gte4PartBomReq.setPartRevisionReq(gte4PartRevisionImportReq);
           BOMNodeReq bomNodeReq = new BOMNodeReq();
           if (null != grr.getUid() && !"0".equals(grr.getUid())) {
               bomNodeReq.setUid(grr.getUid());
               if (null != gte4PartRevisionImportReq.getQuantity() && 0 != gte4PartRevisionImportReq.getQuantity()) {
                   bomNodeReq.setQuantity(gte4PartRevisionImportReq.getQuantity());
               } else {
                   bomNodeReq.setQuantity(grr.getQuantity());
               }
               if (null != gte4PartRevisionImportReq.getFoundNo() && 0 != gte4PartRevisionImportReq.getFoundNo()) {
                   bomNodeReq.setFoundNo(gte4PartRevisionImportReq.getFoundNo());
               } else {
                   bomNodeReq.setFoundNo(grr.getFoundNo());
               }
               gte4PartBomReq.setBomNodeReq(bomNodeReq);
           }
           BomEditRevisionResp gte4PartRevision = (BomEditRevisionResp) gte4PartRevisionImport.updateGte4Part(gte4PartBomReq);
           ItemRevisionResp itemRevisionResp = (ItemRevisionResp)gte4PartRevision.getObjectResp();
           gte4PartRevisionImportReq.setUid(itemRevisionResp.getUid());
           gte4PartRevisionImportReq.setRevisionId(itemRevisionResp.getRevisionId());
           gte4PartRevisionImportReq.setLeftObject(itemRevisionResp.getLeftObject());
           gte4PartRevisionImportReq.setLeftObjectType(itemRevisionResp.getLeftObjectType());
       }

   }

    public void insertOrRelation(Item item,List<Gte4PartRevisionEntity> originalPartRevision,Gte4PartRevisionImportReq gte4PartRevisionResp,List<Gte4PartRevisionImportReq> trees){
        this.insertOrRelation(item,originalPartRevision,gte4PartRevisionResp,trees,null,null,null);
    }
    /**
     * 新增节点或者重新关联节点方法
     *  当excel填入的 itemId 存在于数据库则添加节点于ItemId节点的bom行关联 反之则新增节点
     *  当itemId已存在时判断 上级是否与关联下级 无关联新增关联 有则不动
     * @param originalPartRevision
     * @param gte4PartRevisionResp
     * @param trees
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertOrRelation(Item item,List<Gte4PartRevisionEntity> originalPartRevision,Gte4PartRevisionImportReq gte4PartRevisionResp
            ,List<Gte4PartRevisionImportReq> trees
            ,String parentItemUid,String parentItemRevUid,String parentItemRevId){
        if (null == gte4PartRevisionResp.getUid()) {
            List<Gte4PartRevisionEntity> collect =
                    originalPartRevision.stream().filter(t ->
                            t.getItemId().equals(gte4PartRevisionResp.getItemId()) &&
                                    t.getRevisionId().equals(gte4PartRevisionResp.getRevisionId())).collect(Collectors.toList());
            if (collect.isEmpty()) {
                recursionRankInsert(gte4PartRevisionResp, parentItemRevUid, parentItemUid, parentItemRevId,false);
            }
        }else {
            if (null == parentItemUid && null == parentItemRevUid){
                Gte4PartRevisionImportReq gte4PartRevisionImportReq = getImportParent(gte4PartRevisionResp, trees);
                parentItemUid = gte4PartRevisionImportReq.getLeftObject();
                parentItemRevUid = gte4PartRevisionImportReq.getUid();
                parentItemRevId = gte4PartRevisionImportReq.getRevisionId();

            }
            if (!getChildrenBOMNodeIsExist(parentItemUid,gte4PartRevisionResp)) {
                /**
                 *  当 parentItemUid 出现的场景为 添加一个已存在的节点 并且该节点和源节点没有关系。这样的节点不会触发更新因为匹配itemId匹配不上，不会触发新增因为已存在。
                 *  所以不会被赋值左对象和右对象信息 当他在验证他自己带的或者新增的下一级子级时他的左对象和右对象都会为null 需要重新赋值
                 */
                // 更新 已存在节点数据
                BOMNodeResp grr = new BOMNodeResp();
                grr.setChildItem(gte4PartRevisionResp.getLeftObject());
                grr.setChildItemType(gte4PartRevisionResp.getLeftObjectType());
                this.update(item, gte4PartRevisionResp, grr,gte4PartRevisionResp.getUid());
                //
                WorkspaceObjectResp workspaceObjectResp = new WorkspaceObjectResp();
                BeanUtil.copyPropertiesIgnoreNull(gte4PartRevisionResp, workspaceObjectResp);
                this.addBomNodeRelation(gte4PartRevisionResp, workspaceObjectResp, parentItemRevUid, parentItemUid, parentItemRevId,true);
                // 新增结构 对比 没有添加的原结构进行对比 新增或者更新或者删除
                comparison(gte4PartRevisionResp,null);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Gte4PartRevisionEntity getRevision(Gte4PartRevisionImportReq gte4PartRevisionImportReq){
        if (null != gte4PartRevisionImportReq.getItemId() && null != gte4PartRevisionImportReq.getRevisionId()) {
            List<Pair<String, Object>> params = Arrays.asList(
                    Pair.of(Gte4PartRevisionEntity.ITEM_ID, gte4PartRevisionImportReq.getItemId()));
            List<WorkspaceObjectEntity> fetch = EntityUtil.getDynamicEqQuery(new Gte4PartEntity().getObjectType(), params).fetch();
            if (!fetch.isEmpty()){
                WorkspaceObjectEntity workspaceObjectEntity = fetch.get(0);
                Item item = SpringUtil.getBean(ItemFactory.class).create();
                WorkspaceObjectEntity lastVersion = item.getLastVersion(workspaceObjectEntity.getUid(), workspaceObjectEntity.getObjectType());
                lastVersion.setLeftObject(workspaceObjectEntity.getUid());
                lastVersion.setLeftObjectType(workspaceObjectEntity.getObjectType());
                // 重新添加关联的节点需要在此处更新
                return (Gte4PartRevisionEntity)lastVersion;
            }
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public Gte4PartRevisionImportReq getGte4PartRevisionImportReq(Gte4PartRevisionImportReq gte4PartRevisionImportReq){
        if (null != gte4PartRevisionImportReq.getItemId() && null != gte4PartRevisionImportReq.getRevisionId()) {
            List<Pair<String, Object>> params = Arrays.asList(
                    Pair.of(Gte4PartRevisionEntity.ITEM_ID, gte4PartRevisionImportReq.getItemId()));
            List<WorkspaceObjectEntity> fetch = EntityUtil.getDynamicEqQuery(new Gte4PartEntity().getObjectType(), params).fetch();
            if (!fetch.isEmpty()){
                WorkspaceObjectEntity workspaceObjectEntity = fetch.get(0);
                Item item = SpringUtil.getBean(ItemFactory.class).create();
                WorkspaceObjectEntity lastVersion = item.getLastVersion(workspaceObjectEntity.getUid(), workspaceObjectEntity.getObjectType());
                gte4PartRevisionImportReq.setUid(lastVersion.getUid());
                gte4PartRevisionImportReq.setLeftObject(workspaceObjectEntity.getUid());
                gte4PartRevisionImportReq.setLeftObjectType(workspaceObjectEntity.getObjectType());
                // 重新添加关联的节点需要在此处更新
                return gte4PartRevisionImportReq;
            }
        }
        return gte4PartRevisionImportReq;
    }

    /**
     *
     * 判断父级和子级是否存在关联关系
     * @param parentItemUid 父级Uid
     * @return children 子级
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean getChildrenBOMNodeIsExist(String parentItemUid,Gte4PartRevisionImportReq children){
        /**
         *  当 parentItemUid 出现的场景为 添加一个已存在的节点 并且该节点和源节点没有关系。这样的节点不会触发更新因为匹配itemId匹配不上，不会触发新增因为已存在。
         *  所以不会被赋值左对象和右对象信息 只需要添加关联就行
         */
        if (null != parentItemUid){
            List<Pair<String, Object>> params = Arrays.asList(
                    Pair.of(BOMNodeEntity.PARENT_ITEM, parentItemUid),
                    Pair.of(BOMNodeEntity.PARENT_ITEM_TYPE, children.getLeftObjectType()),
                    Pair.of(BOMNodeEntity.CHILD_ITEM,children.getLeftObject()),
                    Pair.of(BOMNodeEntity.CHILD_ITEM_TYPE,children.getLeftObjectType()));
            List<WorkspaceObjectEntity> fetch = EntityUtil.getDynamicEqQuery(new BOMNodeEntity().getObjectType(), params).fetch();
            if (!fetch.isEmpty()){
                return true;
            }
        }

        return false;
    }

    public Boolean dataVerify(Item item,BOMNodeResp bomNodeResp){
        WorkspaceObjectEntity dbRevision = item.getLastVersion(bomNodeResp.getChildItem(), bomNodeResp.getChildItemType());
        if (!LifeCycleStateEnum.Working.name().equals(dbRevision.getLifeCycleState())){
            return false;
        }
        return true;
    }

    /**
     * 递归解析 层级 组成树形解构
     * @param current
     * @param originalList
     */
    public void recursionChildren(Gte4PartRevisionImportReq current,List<Gte4PartRevisionImportReq> originalList) {
        List<Gte4PartRevisionImportReq> countList = new ArrayList<>();
        for (int i = 1; i < 10000; i++) {
            String currentNumber = String.valueOf(i);
            StringBuffer currentChildren = new StringBuffer(current.getRank()).append(".").append(currentNumber);
            List<Gte4PartRevisionImportReq> collect = originalList.stream().filter(gprirs -> currentChildren.toString().equals(gprirs.getRank())).collect(Collectors.toList());
            countList.addAll(collect);
            if (!collect.isEmpty()){
                collect.forEach(gprir -> {
                    recursionChildren(gprir,originalList);
                });
            }else {
                return;
            }
            current.setGte4PartRevisionImportReqList(countList);
        }
    }
}
