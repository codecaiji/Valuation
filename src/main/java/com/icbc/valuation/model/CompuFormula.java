package com.icbc.valuation.model;

import lombok.Data;

@Data
public class CompuFormula {
    private String targetName;
    private String func;

    public CompuFormula(String targetName, String func) {
        this.targetName = targetName;
        this.func = func;
    }
}
