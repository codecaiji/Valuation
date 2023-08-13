package com.icbc.valuation.model;

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

    private List<AttriConfig> attriConfigs; //Map<String, Map<String, Double>>

    private List<CompuFormula> rangeConfigs;

    private List<CompuFormula> compuFormulas;

}
