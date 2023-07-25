package com.icbc.valuation.model;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Data
public class SheetData {
    private List<String> paramsName = new LinkedList<>();
    private List<Map<String, Object> > paramsScores = new LinkedList<>();

    public void cleanSheetData() {
        paramsName.clear();
        paramsScores.clear();
    }
}
