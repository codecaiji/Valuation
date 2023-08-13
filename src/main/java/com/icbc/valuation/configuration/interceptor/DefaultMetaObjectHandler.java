package com.icbc.valuation.configuration.interceptor;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.icbc.valuation.model.Authority;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.icbc.valuation.model.Constants.*;

@Component
public class DefaultMetaObjectHandler implements MetaObjectHandler {
    @Autowired
    private HttpServletRequest request;

    @Override
    public void insertFill(MetaObject metaObject) {
        Date now = new Date();
        this.setFieldValByName("createTime", now, metaObject);
        this.setFieldValByName("modifyTime", now, metaObject);

        String userName = StringUtils.hasText(getCurrentAuthority().getUserName()) ? getCurrentAuthority().getUserName() : "";
        if (metaObject.hasGetter(CREATEUSER) && ObjectUtils.isEmpty(metaObject.getValue(CREATEUSER))) {
            this.setFieldValByName("createUser", userName, metaObject);
        }
        // 修改者始终自动填充
        this.setFieldValByName("modifyUser", userName, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("modifyTime", new Date(), metaObject);
        this.setFieldValByName("modifyUser", getCurrentAuthority().getUserName(), metaObject);
    }

    private Authority getCurrentAuthority() {
        return (Authority)request.getAttribute(AUTHORITY);
    }
}
