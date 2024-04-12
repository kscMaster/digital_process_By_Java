package com.nancal.imexport.service.impl;

import com.nancal.api.model.BusinessObjectResp;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.common.base.Response;
import com.nancal.common.config.TokenInfo;
import com.nancal.common.constants.ImportConstant;
import com.nancal.common.enums.AppNameEnum;
import com.nancal.service.service.IImexportTaskDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hewei
 * @date 2022/11/1 9:53
 * @Description
 */
@Service
public class IImexportTaskDomainServiceImpl implements IImexportTaskDomainService{

    private final AuxiliaryMaterialImportServiceImpl auxiliaryMaterialImportService;

    private EquipmentImportServiceImpl equipmentImportService;

    private MeasureImportServiceImpl measureImportService;

    private ToolImportServiceImpl toolImportService;

    private Gte4PartImportServiceImpl gte4PartImportService;

    private BopImportServiceImpl bopImportService;

    public IImexportTaskDomainServiceImpl(AuxiliaryMaterialImportServiceImpl auxiliaryMaterialImportService,
                                          EquipmentImportServiceImpl equipmentImportService,
                                          MeasureImportServiceImpl measureImportService,
                                          ToolImportServiceImpl toolImportService,
                                          Gte4PartImportServiceImpl gte4PartImportService,
                                          BopImportServiceImpl bopImportService) {
        this.auxiliaryMaterialImportService = auxiliaryMaterialImportService;
        this.equipmentImportService = equipmentImportService;
        this.measureImportService = measureImportService;
        this.toolImportService = toolImportService;
        this.gte4PartImportService = gte4PartImportService;
        this.bopImportService = bopImportService;
    }

    /**
     * 导入资源库
     * @param file 文件
     * @param leftObject  资源库左侧菜单uid
     * @param leftObjectType 资源库左侧菜单类型
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<BusinessObjectResp> saveLibrary(MultipartFile file, String leftObject, String  leftObjectType, String objectType){
        WorkspaceObjectResp save = saveImportTask(file.getOriginalFilename(), AppNameEnum.LIBRARY.getCode(), objectType);
        TokenInfo tokenInfo = saveImportTask(file);
        switch (objectType){
            case "AuxiliaryMaterial":
                auxiliaryMaterialImportService.importExcel(leftObject,leftObjectType,save.getUid(),tokenInfo);
                break;
            case "Equipment":
                equipmentImportService.importExcel(leftObject,leftObjectType,save.getUid(),tokenInfo);
                break;
            case "Measure":
                measureImportService.importExcel(leftObject,leftObjectType,save.getUid(),tokenInfo);
                break;
            case "Tool":
                toolImportService.importExcel(leftObject,leftObjectType,save.getUid(),tokenInfo);
                break;
            default:
                break;

        }
        return Response.of(save);
    }




    /**
     * 导入设计零组件
     * @param file 文件
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<BusinessObjectResp> saveEbom(MultipartFile file){
        WorkspaceObjectResp save = saveImportTask(file.getOriginalFilename(),AppNameEnum.EBOM.getCode(),ImportConstant.EBOM);
        TokenInfo tokenInfo = saveImportTask(file);
        gte4PartImportService.importExcel(tokenInfo,save.getUid());
        return Response.of(save);
    }



    /**
     * 导入工艺规划
     * @param file 文件
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<BusinessObjectResp> saveBop(MultipartFile file){
        WorkspaceObjectResp save = saveImportTask(file.getOriginalFilename(),AppNameEnum.MSGBOM.getCode(),ImportConstant.Bop);
        TokenInfo tokenInfo = saveImportTask(file);
        bopImportService.importExcel(tokenInfo,save.getUid());
        return Response.of(save);
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public Response<BusinessObjectResp> msgbomExport(ExportBOMReq req){
//        TokenInfo tokenInfo = saveImportTask();
//        WorkspaceObjectResp workspaceObjectResp = saveImexportTaskReq("", ImportConstant.EXPORT,AppNameEnum.MSGBOM.getCode(), ImportConstant.Bop, "工艺规划文件导出任务");
//        bopImportService.bopFieldExport(req.getUid(),req.getObjectType(),workspaceObjectResp.getUid(),tokenInfo,req.getColumnReqList());
//        return Response.of(workspaceObjectResp);
//    }
}
