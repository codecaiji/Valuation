package com.icbc.valuation.service;

import com.icbc.valuation.model.Authority;
import com.icbc.valuation.model.ValuationConfigModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ValuationConfigService {
    Map<String, Object> createValuationConfig(
            Authority authority, String name,String description, String status,
            String attriConfigs, String rangeConfigs, String compuFormulas);

    Map<String, Object> getValuationConfigDetail(Authority authority, Integer id);

    Map<String, Object> getValuationConfigs(Authority authority, int currentPage, int pageSize, String queryString, String status);

    Map<String, Object> updateValuationConfig(Authority authority, Integer id, ValuationConfigModel valuationConfig);

    Map<String, Object> changeConfigStatus(Authority authority, Integer id, String status);

    Map<String, Object> deletValuationConfig(Authority authority, List<Integer> ids);

}
