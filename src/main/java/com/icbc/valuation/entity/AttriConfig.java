package com.icbc.valuation.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("attri_configs")
@Data
@Accessors(chain = true)
public class AttriConfig {

    private Integer configId;

    private String name;

    private String fieldScores;
}
