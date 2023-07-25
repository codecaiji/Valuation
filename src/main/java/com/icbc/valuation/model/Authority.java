package com.icbc.valuation.model;

import lombok.Data;

@Data
public class Authority {

    private String userName;

    public Authority(String userName) {
        this.userName = userName;
    }
}
