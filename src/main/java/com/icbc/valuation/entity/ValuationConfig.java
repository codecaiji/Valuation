package com.icbc.valuation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@TableName("valuation_configs")
@Data
@Accessors(chain = true)
public class ValuationConfig {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String attriConfigs;

    private String rangeConfigs;

    private String compuFormulas;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date modifyTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String modifyUser;

}
