package com.icbc.valuation.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("compu_formulas")
@Data
@Accessors(chain = true)
public class CompuFormula {

    private Integer configId;

    private String targetName;

    private String func;
}
