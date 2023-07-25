package com.icbc.valuation.service.impl;

import com.icbc.valuation.model.Constants;
import com.icbc.valuation.model.Result;
import com.icbc.valuation.model.enums.Status;

import java.text.MessageFormat;
import java.util.Map;

/**
 * base service
 */
public class BaseService {

    /**
     * return data
     *
     * @param data success
     * @return success result code
     */
    public Map<String, Object> success(Map<String, Object> result, Object data) {
        putMsg(result, Status.SUCCESS);
        result.put(Constants.DATA, data);
        return result;
    }

    /**
     * put message to map
     *
     * @param result result code
     * @param status status
     * @param statusParams status message
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
     * @param result result code
     * @param status status
     * @param statusParams status message
     */
    protected void putMsg(Result<Object> result, Status status, Object... statusParams) {
        result.setCode(status.getCode());

        if (statusParams != null && statusParams.length > 0) {
            result.setMsg(MessageFormat.format(status.getMsg(), statusParams));
        } else {
            result.setMsg(status.getMsg());
        }
    }
}
