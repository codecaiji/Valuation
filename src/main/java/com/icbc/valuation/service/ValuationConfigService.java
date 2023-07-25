package com.icbc.valuation.service;

import com.icbc.valuation.model.Authority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ValuationConfigService {
    Map<String, Object> createValuationConfig(
            Authority authority, String name, String attriConfigs, String rangeConfigs, String compuFormulas);

    Map<String, Object> getValuationConfigByName(Authority authority, String name);

    Map<String, Object> getValuationConfigs(Authority authority, int pageNo, int pageSize);

    Map<String, Object> updateValuationConfig(
            Authority authority, Integer id, String name, String attriConfigs, String rangeConfigs, String compuFormulas);

    Map<String, Object> deletValuationConfig(Authority authority, List<Integer> ids);

}
