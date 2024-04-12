package com.nancal.remote.vo;


import lombok.Data;

@Data
public class LezaoResult<T> {

    private Integer code;

    private String msg;

    private T data;

    private String cause;

    private String value;

}
