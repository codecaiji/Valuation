package com.icbc.valuation.model;

import lombok.Data;

@Data
public class FieldScore {
    private String field;
    private Double score;

    FieldScore(String field, Double score) {
        this.field = field;
        this.score = score;
    }
}
