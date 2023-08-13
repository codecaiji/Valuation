package com.icbc.valuation.model;

import lombok.Data;

import java.util.List;

@Data
public class AttriConfig {
    private String name;
    private List<FieldScore> fieldScores;
}
