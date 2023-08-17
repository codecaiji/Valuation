package com.icbc.valuation.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class AttriConfigModel {

    private String name;

    private List<FieldScore> fieldScores;
}
