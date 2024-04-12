package com.nancal.remote.to;

import lombok.Data;

import java.util.List;

/**
 * @author hewei
 * @date 2022/10/18 15:20
 * @Description
 */
@Data
public class MoreDictEntryReq {

    private List<String> appCodes;

    private List<String> dictTypes;
}
