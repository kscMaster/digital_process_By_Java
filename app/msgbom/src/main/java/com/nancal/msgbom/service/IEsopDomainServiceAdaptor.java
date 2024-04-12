package com.nancal.msgbom.service;

import com.nancal.api.model.*;
import com.nancal.service.service.IBusinessObjectDomainService;

import java.util.List;

public interface IEsopDomainServiceAdaptor extends IBusinessObjectDomainService {

    /***
     * 生成ESOP
     *
     * @param req
     * @author 徐鹏军
     * @date 2022/8/30 19:00
     * @return {@link List<String>}
     */
    public List<String> creatEsop(EsopCreateReq req);

    /***
     * 根据BOM的查询结果，获取所有线体工艺（包括它的子节点）
     *
     * @param req BOM查询结果
     * @author 徐鹏军
     * @date 2022/8/31 10:34
     * @return {@link List< BOMNodeResp>}
     */
    public List<BOMNodeResp> getAllGte4MfgLinePrBom(BOMNodeResp req);

    /***
     * 根据线体工艺，找出所有的工序
     *
     * @param resps 线体工艺bom
     * @author 徐鹏军
     * @date 2022/8/31 19:30
     * @return {@link List< BOMNodeResp>}
     */
    public List<BOMNodeResp> getAllOperationBom(List<BOMNodeResp> resps);

    /***
     * 线平衡前操作，需要先将基础表数据同步
     *
     * @param node 线体工艺的BOM对象
     * @param stationNum 工位数量
     * @param emptyStation 空工位数量
     * @author 徐鹏军
     * @date 2022/8/31 11:18
     * @return {@link String}
     */
    public String lineBalanceBefore(BOMNodeResp node, Integer stationNum, String emptyStation);

    /***
     * 根据线体工艺零组件版本id清空之前生成的线平衡数据
     *
     * @param nodes 线体工艺的BOM对象集合
     * @author 徐鹏军
     * @date 2022/8/31 11:18
     * @return {@link String}
     */
    public void cleanLineBalance(List<BOMNodeResp> nodes);

    /***
     * 执行Python脚本（线平衡算法）
     *
     * @param taskId 任务id
     * @author 徐鹏军
     * @date 2022/8/30 19:14
     * @return {@link int}
     */
    public int executePython(String taskId);

    /***
     * 获取最大工位数量
     *
     * @param req
     * @author 徐鹏军
     * @date 2022/9/1 11:26
     * @return {@link Integer}
     */
    public Integer findMaxStationNum(FindReq req, Boolean stationFlag);

    /***
     * 获取线平衡柱状图
     *
     * @param uid 线体工艺版本id
     * @author 徐鹏军
     * @date 2022/9/1 13:35
     * @return {@link List< EsopGaProcessStationResp>}
     */
    List<EsopGaProcessStationResp> findLineBalancePic(String uid);

    /***
     * 获取线平衡后左侧树
     *
     * @param req
     * @author 徐鹏军
     * @date 2022/9/1 17:28
     * @return {@link BOMNodeResp}
     */
    public BOMNodeResp findLineBalanceAfterTree(FindReq req);


    /***
     * 获取线平衡前的柱状图
     *
     * @param uid 线体工艺版本id
     * @author 徐鹏军
     * @date 2022/9/1 13:35
     * @return {@link List< EsopGaProcessStationResp>}
     */
    List<EsopGaProcessStationResp> findLineBalancePicFront(String uid);


    /**
     * 查询线平衡树
     * @param req
     * @return
     */
    BOPNodeViewResp find(FindReq req);

    /**
     * esop卡片
     * @param id
     * @return
     */
    BOPNodeViewResp esopCard(FindReq id);
}
