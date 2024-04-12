package com.nancal.library.service.impl;

import com.nancal.api.model.LibraryFolderReq;
import com.nancal.api.model.LibraryFolderResp;
import com.nancal.api.model.LibraryResp;
import com.nancal.api.model.BusinessObjectReq;
import com.nancal.api.model.WorkspaceObjectResp;
import com.nancal.common.annotation.TimeLog;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.enums.LibraryEnum;
import com.nancal.common.exception.ServiceException;
import com.nancal.service.service.ILibraryFolderDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class LibraryFolderDomainServiceImpl implements ILibraryFolderDomainService {

    /**
     *  创建库
     *  1.创建根节点文件夹
     *      leftObject传空，先创建LibraryFolder,注意library_type
     *      再创建IncludeRl，注意leftObject为空，rightObject为LibraryFolder的uid
     *
     *  2.在文件夹下创建文件夹
     *      leftObject传父节点的uid,先创建LibraryFolder,注意library_type
     *      再创建IncludeRl，注意leftObject为父节点的uid，rightObject为LibraryFolder的uid
     *
     * @param req
     * @author: 拓凯
     * @time: 2022/5/24
     * @return: {@link WorkspaceObjectResp}
     */
    @Override
    @Transactional
    public WorkspaceObjectResp save(BusinessObjectReq req) {
        LibraryFolderReq libraryFolderReq = (LibraryFolderReq) req;
        if (!LibraryEnum.isContains(libraryFolderReq.getLibraryType())) {
            throw new ServiceException(ErrorCode.E_12, "库类型输入有误，请校验库的类型");
        }
        WorkspaceObjectResp save = ILibraryFolderDomainService.super.save(libraryFolderReq);
        return save;
    }

    /**
     * 获取基础库树结构
     * @param
     * @author: 拓凯
     * @time: 2022/5/24
     * @return: {@link List< LibraryFolderResp>}
     */
    @Override
    @TimeLog
    public List<LibraryResp> treeLibrary(){
        List type = new ArrayList();
        type.add(LibraryEnum.TOOL.getCode());
        type.add(LibraryEnum.MEASURE.getCode());
        type.add(LibraryEnum.EQUIPMENT.getCode());
        type.add(LibraryEnum.AUXILIARYMATERIAL.getCode());
        List tree = ILibraryFolderDomainService.super.tree(type);
        return tree;
    }


}
