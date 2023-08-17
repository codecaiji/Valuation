package com.icbc.valuation.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("range_configs")
@Data
@Accessors(chain = true)
public class RangeConfig {

    private Integer configId;

    private String targetName;

    private String func;
}