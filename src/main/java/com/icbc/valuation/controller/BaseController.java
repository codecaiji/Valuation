package com.icbc.valuation.controller;

import com.icbc.valuation.model.Constants;
import com.icbc.valuation.model.Result;
import com.icbc.valuation.model.enums.Status;

import java.text.MessageFormat;
import java.util.Map;

public class BaseController {
    /**
     * return data
     *
     * @param result result code
     * @return result code
     */
    public Result<Object> returnData(Map<String, Object> result) {
        Status status = (Status) result.get(Constants.STATUS);
        if (status == Status.SUCCESS) {
            String msg = Status.SUCCESS.getMsg();
            Object data = result.get(Constants.DATA);
            return success(msg, data);
        } else {
            Integer code = status.getCode();
            String msg = (String) result.get(Constants.MSG);
            Object data = result.get(Constants.DATA);
            return error(code, msg, data);
        }
    }

    /**
     * success
     *
     * @return success result code
     */
    public Result<Object> success() {
        Result<Object> result = new Result<>();
        result.setCode(Status.SUCCESS.getCode());
        result.setMsg(Status.SUCCESS.getMsg());

        return result;
    }

    /**
     * success does not need to return data
     *
     * @param msg success message
     * @return success result code
     */
    public Result<Object> success(String msg) {
        Result<Object> result = new Result<>();
        result.setCode(Status.SUCCESS.getCode());
        result.setMsg(msg);
        return result;
    }

    /**
     * return data no paging
     *
     * @param msg  success message
     * @param list data list
     * @return success result code
     */
    public Result<Object> success(String msg, Object list) {
        return getResult(msg, list);
    }

    /**
     * return data
     *
     * @param data success
     * @return success result code
     */
    public Result<Object> success(Object data) {
        return getResult(Status.SUCCESS.getMsg(), data);
    }

    /**
     * return the data use Map format, for example, passing the value of key, value, passing a value
     * eg. "/user/add"  then return user name: zhangsan
     *
     * @param msg  message
     * @param data success object data
     * @return success result code
     */
    public Result<Object> success(String msg, Map<String, Object> data) {
        return getResult(msg, data);
    }

    /**
     * error handle
     *
     * @param code result code
     * @param msg  result message
     * @return error result code
     */
    public Result<Object> error(Integer code, String msg, Object data) {
        Result<Object> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    /**
     * put message to map
     *
     * @param result       result
     * @param status       status
     * @param statusParams object messages
     */
    protected void putMsg(Map<String, Object> result, Status status, Object... statusParams) {
        result.put(Constants.STATUS, status);
        if (statusParams != null && statusParams.length > 0) {
            result.put(Constants.MSG, MessageFormat.format(status.getMsg(), statusParams));
        } else {
            result.put(Constants.MSG, status.getMsg());
        }
    }

    /**
     * put message to result object
     *
     * @param result       result
     * @param status       status
     * @param statusParams status parameters
     */
    protected void putMsg(Result<Object> result, Status status, Object... statusParams) {
        result.setCode(status.getCode());

        if (statusParams != null && statusParams.length > 0) {
            result.setMsg(MessageFormat.format(status.getMsg(), statusParams));
        } else {
            result.setMsg(status.getMsg());
        }

    }

    /**
     * get result
     *
     * @param msg  message
     * @param data object data
     * @return result = { code: xx, data: {}, msg: "" }
     */
    private Result<Object> getResult(String msg, Object data) {
        Result<Object> result = new Result<>();
        result.setCode(Status.SUCCESS.getCode());
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
}
