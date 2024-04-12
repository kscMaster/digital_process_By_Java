package com.nancal.common.enums;

public enum ErrorCode implements IErrorCode {

    OK(0, ""),
    E_10(10, "参数错误"),
    E_11(11, "数据更新失败"),
    E_12(12, "数据不存在"),
    E_13(13, "重复请求"),
    E_14(14, "当前数据非最新状态，刷新后重试"),
    ERROR(-1, "系统异常"),
    ID_ERROR(-2, "ID不能为空"),
    FAIL(-3, ""),
    UNAUTHORIZED(401, "您需要登录"),
    FORBIDDEN(403, "没有访问权限"),
    RESOURCE_GET_FAIL(10001, "用户应用资源获取失败"),
    PARAM_INFO_LOSE(10002, "参数不完整"),
    REMOTE_FAIL(10003, ""),

    ;

    private int code;

    private String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String msg() {
        return msg;
    }
}
