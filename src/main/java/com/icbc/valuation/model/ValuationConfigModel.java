package com.icbc.valuation.model;

import com.icbc.valuation.entity.CompuFormula;
import com.icbc.valuation.entity.RangeConfig;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ValuationConfigModel {
    private Integer id;

    private String name;

    private String description;

    private String status;

    private List<AttriConfigModel> attriConfigs; //Map<String, Map<String, Double>>

    private List<RangeConfig> rangeConfigs;

    private List<CompuFormula> compuFormulas;

}
