package com.icbc.valuation.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class ValuationConfigModel {
    private Integer id;

    private String name;

    private Map<String, Map<String, Double>> attriConfigs;

    private List<CompuFormula> rangeConfigs;

    private List<CompuFormula> compuFormulas;

}
