package com.nancal.remote.vo;

import lombok.Data;

import java.util.List;

/**
 * @author hewei
 * @date 2022/10/18 15:14
 * @Description
 */
@Data
public class MoreDictEntryGroupVo {


    private String appCode;


    private List<MoreDictEntryVo> dictList;


    private String dictType;

}
