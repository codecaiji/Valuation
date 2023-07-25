package com.icbc.valuation.model.enums;

public enum Status {
    SUCCESS(0, "成功"),
    FAILED(1, "{0}"),

    INTERNAL_SERVER_FAILED(10000, "服务端异常: {0}"),

    DATA_UPLOAD_FAILED(20001, "数据上传失败：{0}"),
    DATA_EMPTY(20002, "数据为空，请先上传数据"),
    COMPUTE_FAILED(20003, "计算失败：{0}"),
    CLEAN_FAILED(20004, "数据初始化失败：{0}"),

    CONFIG_NAME_EXIST(30001, "配置名 {0} 已存在"),
    CONFIG_NOT_EXIST(30002, "配置 {0} 不存在"),
    CONFIG_CREATE_FAILED(30003, "创建配置失败：{0}"),
    CONFIG_UPDATE_FAILED(30004, "更新配置失败：{0}"),
    CONFIG_QUERY_FAILED(30005, "查询配置失败：{0}"),
    CONFIG_QUERY_LIST_FAILED(30006, "查询配置列表失败：{0}"),
    CONFIG_DELETE_FAILED(30007, "删除配置失败：{0}");

    private final int code;
    private final String msg;

    Status(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return msg;
    }
}