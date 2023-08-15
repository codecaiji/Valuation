package com.icbc.valuation.utils;

import com.googlecode.aviator.AviatorEvaluator;
import com.icbc.valuation.model.AttriConfig;
import com.icbc.valuation.model.CompuFormula;
import com.icbc.valuation.model.FieldScore;
import com.icbc.valuation.model.SheetData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ValuationUtil {

    public static List<SheetData> computeByformulas(List<SheetData> uploadData, List<CompuFormula> formulas) {
        List<SheetData> outputList = new LinkedList<>();
        if (CollectionUtils.isEmpty(formulas) || CollectionUtils.isEmpty(uploadData)) {
            return uploadData;
        }

        for (SheetData uploadSheetData : uploadData) {
            SheetData outputSheetData = new SheetData();
            List<Map<String, Object>> outputScores = new LinkedList<>();
            for (Map<String, Object> paramsScore : uploadSheetData.getParamsScores()) {
                //TODO:可预编译优化执行速度
                Map<String, Object> outputMap = new LinkedHashMap<>();
                for (CompuFormula formula : formulas) {
                    try {
                        //TODO:使用计算函数的参数为空时抛出的异常无法捕获
                        Object output = AviatorEvaluator.execute(formula.getFunc(), paramsScore);
                        if (Objects.nonNull(output)) {
                            outputMap.put(formula.getTargetName(), output);
                            paramsScore.put(formula.getTargetName(), output);
                        }
                    } catch (Exception e) {
                        // 公式中可能有空值
                        //throw new RuntimeException(e);
                        outputMap.put(formula.getTargetName(), null);
                        paramsScore.put(formula.getTargetName(), null);
                        //e.printStackTrace();
                        log.warn(e.getMessage());
                    }
                }
                if (!outputMap.isEmpty()) {
                    outputScores.add(outputMap);
                }
            }

            if (CollectionUtils.isNotEmpty(outputScores)) {
                outputSheetData.setParamsName(new LinkedList<>(outputScores.get(0).keySet()));
                outputSheetData.setParamsScores(outputScores);
                outputList.add(outputSheetData);
            }
        }
        return outputList;
    }

    public static Map<String, Map<String, Double>> attriListToMap(List<AttriConfig> attriConfigs) {
        Map<String, Map<String, Double>> attriConfigsMap = new HashMap<>();

        for (AttriConfig attriConfig : attriConfigs) {
            Map<String, Double> attriConfigMap = new HashMap<>();

            for (FieldScore fieldScore : attriConfig.getFieldScores()) {
                attriConfigMap.put(fieldScore.getField(), fieldScore.getScore());
            }
            attriConfigsMap.put(attriConfig.getName(), attriConfigMap);
        }
        return attriConfigsMap;
    }

    public static void transToScoresByConfig(List<SheetData> uploadData,
        Map<String, Map<String, Double>> attriConfigs, List<CompuFormula> rangeConfigs) {
        if (CollectionUtils.isEmpty(uploadData)) {
            return;
        }
        for (SheetData uploadSheetData : uploadData) {
            uploadSheetData.setParamsScores(transToScoreByConfig(uploadSheetData.getParamsScores(), attriConfigs, rangeConfigs));
        }
    }
    public static List<Map<String, Object>> transToScoreByConfig(
            List<Map<String, Object>> params, Map<String, Map<String, Double>> attriConfigs,
            List<CompuFormula> rangeConfigs) {
        List<Map<String, Object>> attriScores = transToScoreByAttriConfig(attriConfigs, params);
        return transToScoreByRangeConfig(rangeConfigs, attriScores);
    }

    public static List<Map<String, Object>> transToScoreByAttriConfig(
            Map<String, Map<String, Double>> attriConfigs, List<Map<String, Object>> params) {
        List<Map<String, Object>> attriScores = new LinkedList<>();
        if (CollectionUtils.isEmpty(params)) {
            return params;
        }
        for (Map<String, Object> param : params) {
            Map<String, Object> attriScore = new HashMap<>();
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                try {
                    if (MapUtils.isNotEmpty(attriConfigs) && attriConfigs.containsKey(entry.getKey())) {
                        attriScore.put(entry.getKey(),
                                attriConfigs.get(entry.getKey()).getOrDefault(entry.getValue(), 0.0));
                    } else {
                        attriScore.put(entry.getKey(), Double.parseDouble(entry.getValue().toString()));
                    }
                } catch (NumberFormatException e) {
                    attriScore.put(entry.getKey(), 0.0);
                }
            }
            attriScores.add(attriScore);
        }
        return attriScores;
    }

    public static List<Map<String, Object>> transToScoreByRangeConfig(List<CompuFormula> rangeConfigs, List<Map<String, Object>> params) {
        List<Map<String, Object>> rangeScores = new LinkedList<>();
        if (CollectionUtils.isEmpty(rangeConfigs) || CollectionUtils.isEmpty(params)) {
            return params;
        }
        for (Map<String, Object> param : params) {
            Map<String, Object> rangeScore = new HashMap<>();
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                for (CompuFormula valueConfig : rangeConfigs) {
                    try {
                        if (entry.getKey().equals(valueConfig.getTargetName())) {
                            Object output = AviatorEvaluator.execute(valueConfig.getFunc(), param);
                            rangeScore.put(valueConfig.getTargetName(), output);
                            entry.setValue(output);
                        } else {
                            rangeScore.put(entry.getKey(), Double.parseDouble(entry.getValue().toString()));
                        }
                    } catch (NumberFormatException e) {
                        rangeScore.put(entry.getKey(), 0.0);
                        entry.setValue(0.0);
                    }
                }
            }
            rangeScores.add(rangeScore);
        }
        return rangeScores;
    }
}
