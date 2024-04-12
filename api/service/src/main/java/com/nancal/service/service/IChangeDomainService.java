package com.nancal.service.service;


import cn.afterturn.easypoi.word.WordExportUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.nancal.api.model.*;
import com.nancal.api.model.dataset.FileAttrReq;
import com.nancal.api.utils.DictUtil;
import com.nancal.api.utils.EntityUtil;
import com.nancal.api.utils.TranslateUtil;
import com.nancal.common.base.IdRequest;
import com.nancal.common.utils.Word2PDFUtil;
import com.nancal.model.entity.*;
import com.nancal.service.bo.ItemRevision;
import com.nancal.service.factory.ItemRevisionFactory;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface IChangeDomainService extends IWorkspaceObjectDomainService {

    Logger log = LoggerFactory.getLogger(IChangeDomainService.class);

    /***
     * 绑定更改的附件,默认是实现的工艺更改单和发次更改单
     * @param changeEntity 更改单
     * @author 徐鹏军
     * @date 2022/3/31 17:40
     */
    default void uploadChangePDF(ChangeEntity changeEntity, String compareDict,String templatePath,
                                 Class<? extends RunTimeChangeAttachResp> clazz) throws Exception {
        RequestContextHolder.currentRequestAttributes().setAttribute(WorkspaceObjectEntity.OBJECT_TYPE,
                changeEntity.getObjectType(), RequestAttributes.SCOPE_REQUEST);
        // 获取有差异的数据
        BusinessObjectResp businessObjectResp = changeData(new IdRequest(changeEntity.getUid()),compareDict,clazz);
        if (Objects.isNull(businessObjectResp)) {
            log.error("业务数据不存在,uid={}",changeEntity.getUid());
            return;
        }
        RunTimeChangeAttachOuterResp outerResp = (RunTimeChangeAttachOuterResp) businessObjectResp;
        List<RunTimeChangeAttachResp> list = outerResp.getList();
        if(CollUtil.isEmpty(list)){
            log.error("变更对象不存在,uid={}",changeEntity.getUid());
            return;
        }
        // 根据word模板的表格，按照19一个分组，
        List<RunTimeChangeAttachResp> changeHTML = new ArrayList<>();

        for (RunTimeChangeAttachResp resp : list) {
            if (CollUtil.isNotEmpty(resp.getItems())) {
                List<List<RunTimeChangeAttachItemResp>> split = CollUtil.split(resp.getItems(), 19);
                // 设置总页数
                resp.setTNum(split.size());
                for (int i = 1; i <= split.size(); i++) {
                    RunTimeChangeAttachResp attachResp = new RunTimeChangeAttachResp();
                    BeanUtils.copyProperties(resp, attachResp, "items");
                    attachResp.setCNum(i);
                    attachResp.setItems(split.get(i - 1));
                    changeHTML.add(resp);
                }
            } else {
                changeHTML.add(resp);
            }
        }
        List<Map<String, Object>> wordTemplateMap = this.transformWordTemplateMap(changeHTML, 19);
        // 用程序生成PDF附件到流中
        XWPFDocument doc = WordExportUtil.exportWord07(templatePath, wordTemplateMap);
        String fileName = changeEntity.getObjectName() + StrUtil.UNDERLINE + System.currentTimeMillis();
        IDatasetDomainService datasetDomainService = SpringUtil.getBean(IDatasetDomainService.class);
        String filePath = Word2PDFUtil.wordToPdf(fileName, doc);
        FileAttrReq fileAttrReq = this.redepositFile(filePath);
        try{FileUtil.del(filePath);}catch (Exception e){}
        // 保存附件并绑定更改单和附件的关系
        PDFReq pdfReq = new PDFReq();
        pdfReq.setLeftObjectType(changeEntity.getObjectType());
        pdfReq.setLeftObject(changeEntity.getUid());
        pdfReq.setObjectName(FileUtil.mainName(fileName));
        pdfReq.setFiles(Collections.singletonList(fileAttrReq));
        pdfReq.setObjectType(EntityUtil.getObjectType(PDFEntity.class));
        datasetDomainService.save(pdfReq);
    }

    default RunTimeChangeAttachOuterResp changeData(IdRequest id, String compareDict,Class<? extends RunTimeChangeAttachResp> clazz) {
        String objectTypeURL = EntityUtil.getObjectType();
        Object attribute = RequestContextHolder.currentRequestAttributes().
                getAttribute(WorkspaceObjectEntity.OBJECT_TYPE, RequestAttributes.SCOPE_REQUEST);
        if (Objects.nonNull(attribute)) {
            objectTypeURL = attribute.toString();
        }
        DictUtil dictUtil = SpringUtil.getBean(DictUtil.class);
        ItemRevision itemRevision = SpringUtil.getBean(ItemRevisionFactory.class).create();
        TranslateUtil translateUtil = SpringUtil.getBean(TranslateUtil.class);
        ChangeEntity changeEntity = EntityUtil.getById(objectTypeURL, id.getUid());
        if(ObjectUtils.isEmpty(changeEntity)){
            log.error("更改单id：[" + id.getUid() + "],类型：[" + id.getObjectType() + "]不存在");
            return null;
        }
        // 1：获取更改单更改前后零组件
        List<Pair<String, Object>> params = Arrays.asList(
                Pair.of(RelationEntity.LEFT_OBJECT, changeEntity.getUid()),
                Pair.of(RelationEntity.LEFT_OBJECT_TYPE, changeEntity.getObjectType())
        );
        List<ChangeAfterRLEntity> afterRLList = EntityUtil.getDynamicEqQuery(ChangeAfterRLEntity.class, params).fetch();
        if (CollUtil.isEmpty(afterRLList)) {
            log.error("更改单id：[" + changeEntity.getUid() + "],类型：[" + changeEntity.getObjectType() + "]不存在更改后对象");
            return null;
        }
        List<ChangeBeforeRLEntity> beforeRLList = EntityUtil.getDynamicEqQuery(ChangeBeforeRLEntity.class, params).fetch();
        if (CollUtil.isEmpty(beforeRLList)) {
            beforeRLList = Collections.EMPTY_LIST;
        }

        RunTimeChangeAttachOuterResp outerResp = new RunTimeChangeAttachOuterResp();
        outerResp.setPartChangeId(changeEntity.getChangeId());
        outerResp.setPartChangeName(changeEntity.getObjectName());

        Map<String, ChangeBeforeRLEntity> beforeMap = beforeRLList.stream().collect(Collectors.toMap(ChangeBeforeRLEntity::getItemUid, Function.identity()));
        for (ChangeAfterRLEntity after : afterRLList) {
            String itemUid = after.getItemUid();
            WorkspaceObjectEntity afterItemRevision = itemRevision.getActiveRevision(after.getRightObject(), after.getRightObjectType());
            if(ObjectUtils.isEmpty(afterItemRevision)){
                log.error("更改单id：[" + changeEntity.getUid() + "],类型：[" + changeEntity.getObjectType() + "]的更改后对象绑定的版本不存在");
                continue;
            }
            ItemRevisionResp afterResp = (ItemRevisionResp)EntityUtil.entityToResp(afterItemRevision);
            translateUtil.translate(afterResp);
            ItemRevisionResp beforeResp = null;
            if(beforeMap.containsKey(itemUid)){
                ChangeBeforeRLEntity before = beforeMap.get(itemUid);
                WorkspaceObjectEntity beforeItemRevision = itemRevision.getActiveRevision(before.getRightObject(), before.getRightObjectType());
                if(ObjectUtils.isEmpty(afterItemRevision)){
                    log.error("更改单id：[" + changeEntity.getUid() + "],类型：[" + changeEntity.getObjectType() + "]的更改前对象绑定的版本不存在");
                    continue;
                }
                beforeResp = (ItemRevisionResp)EntityUtil.entityToResp(beforeItemRevision);
                translateUtil.translate(beforeResp);
            }

            RunTimeChangeAttachResp resp = ReflectUtil.newInstance(clazz);
            resp.setChangeId(changeEntity.getChangeId());
            resp.setChangeObjectName(changeEntity.getObjectName());
            resp.setDisplayName(afterResp.getDisplayName());
            resp.setChangeAtOnce(after.getChangeAtOnce());
            resp.setProcessedSuggestion(after.getProcessedSuggestion());
            resp.setWipSuggestion(after.getWipSuggestion());
            resp.setRemark(after.getRemark());
            fullOtherData(resp,beforeResp,afterResp,changeEntity,after);
            if(CollUtil.isEmpty(outerResp.getList())){
                outerResp.setList(new ArrayList<>());
            }
            // 如果更改前后对象版本一致，就不进行属性和结构进行比较了
            if (!Objects.isNull(beforeResp) && afterResp.getRevisionId().equals(beforeResp.getRevisionId())) {
                outerResp.getList().add(resp);
                continue;
            }
            //TODO 子bom行没有处理
            dictUtil.compareField(beforeResp, afterResp, compareDict).forEach(data -> {
                RunTimeChangeAttachItemResp itemResp = new RunTimeChangeAttachItemResp();
                itemResp.setItemId(afterResp.getItemId());
                itemResp.setContent(data.getLeft());
                itemResp.setBefore(data.getMiddle());
                itemResp.setAfter(data.getRight());
                resp.getItems().add(itemResp);
            });
            outerResp.getList().add(resp);
        }
        outerResp.setPartChangePage(CollUtil.isEmpty(outerResp.getList())?0:outerResp.getList().size());
        return outerResp;
    }

    default void fullOtherData(RunTimeChangeAttachResp resp,ItemRevisionResp beforeResp,ItemRevisionResp afterResp,
                               ChangeEntity changeEntity,ChangeAfterRLEntity changeAfterRLEntity){}


    default List<Map<String, Object>> transformWordTemplateMap(List<RunTimeChangeAttachResp> respList, Integer splitCount) {
        List<Map<String, Object>> result = new ArrayList<>();
        Field[] fields = ReflectUtil.getFields(RunTimeChangeAttachResp.class);
        Field[] itemFields = ReflectUtil.getFields(RunTimeChangeAttachItemResp.class);
        for (RunTimeChangeAttachResp attachResp : respList) {
            Map<String, Object> pageMap = new HashMap<>();
            for (Field field : fields) {
                Object fieldValue = ReflectUtil.getFieldValue(attachResp, field);
                if (!field.getType().isAssignableFrom(List.class)) {
                    pageMap.put(field.getName(), Objects.isNull(fieldValue) ? StrUtil.EMPTY : fieldValue);
                    continue;
                }
                int foreachCount = Convert.toInt(splitCount, 1);
                List items = Objects.isNull(fieldValue) ? Collections.emptyList() : (List) fieldValue;
                for (int i = 0; i < foreachCount; i++) {
                    for (Field itemField : itemFields) {
                        if (CollUtil.isEmpty(items) || items.size() - 1 < i) {
                            pageMap.put(itemField.getName() + (i + 1), StrUtil.EMPTY);
                            continue;
                        }
                        Object itemFieldValue = ReflectUtil.getFieldValue(items.get(i), itemField);
                        if (Objects.isNull(itemFieldValue)) {
                            pageMap.put(itemField.getName() + (i + 1), StrUtil.EMPTY);
                        } else {
                            pageMap.put(itemField.getName() + (i + 1), itemFieldValue);
                        }
                    }
                }
            }
            result.add(pageMap);
        }
        return result;
    }

}
