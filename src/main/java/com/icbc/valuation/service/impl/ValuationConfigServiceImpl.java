package com.icbc.valuation.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icbc.valuation.entity.ValuationConfig;
import com.icbc.valuation.mapper.ValuationConfigMapper;
import com.icbc.valuation.model.Authority;
import com.icbc.valuation.model.CompuFormula;
import com.icbc.valuation.model.ValuationConfigModel;
import com.icbc.valuation.service.ValuationConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.icbc.valuation.model.enums.Status.CONFIG_NAME_EXIST;
import static com.icbc.valuation.model.enums.Status.CONFIG_NOT_EXIST;

@Service
@Slf4j
public class ValuationConfigServiceImpl extends BaseService implements ValuationConfigService {

    @Resource
    ValuationConfigMapper valuationConfigMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> createValuationConfig(
            Authority authority, String name, String attriConfigs, String rangeConfigs, String compuFormulas) {
        Map<String, Object> result = new HashMap<>();

        if (!checkName(name, result)) {
            return result;
        }
        //String configId = randomUUID().toString();
        ValuationConfig valuationConfig = new ValuationConfig()//.setId(configId)
                .setName(name).setAttriConfigs(attriConfigs).setRangeConfigs(rangeConfigs)
                        .setCompuFormulas(compuFormulas);
        valuationConfigMapper.insert(valuationConfig);
        return success(result, valuationConfig.getId());
    }

    @Override
    public Map<String, Object> getValuationConfigByName(Authority authority, String name) {
        Map<String, Object> result = new HashMap<>();

        ValuationConfig valuationConfig = valuationConfigMapper.selectOne(
                new QueryWrapper<ValuationConfig>().lambda()
                        .eq(ValuationConfig::getName, name));
        if (ObjectUtils.isEmpty(valuationConfig)) {
            putMsg(result, CONFIG_NOT_EXIST, name);
            return result;
        }
        ValuationConfigModel valuationConfigModel = new ValuationConfigModel().setId(valuationConfig.getId())
                .setName(valuationConfig.getName())
                .setAttriConfigs(JSON.parseObject(valuationConfig.getAttriConfigs(), new TypeReference<Map<String, Map<String, Double>>>() {}))
                .setRangeConfigs(JSON.parseArray(valuationConfig.getRangeConfigs(), CompuFormula.class))
                .setCompuFormulas(JSON.parseArray(valuationConfig.getCompuFormulas(), CompuFormula.class));

        return success(result, valuationConfigModel);
    }

    @Override
    public Map<String, Object> getValuationConfigs(Authority authority, int pageNo, int pageSize) {
        Map<String, Object> result = new HashMap<>();

        Page<ValuationConfig> page = new Page<>(pageNo, pageSize);
        IPage<ValuationConfig> iPage = valuationConfigMapper.selectPage(page,
                new QueryWrapper<ValuationConfig>().lambda());

        return success(result, iPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> updateValuationConfig(
            Authority authority, Integer id, String name, String attriConfigs, String rangeConfigs, String compuFormulas) {
        Map<String, Object> result = new HashMap<>();

        if (!checkName(name, result)) {
            return result;
        }

        ValuationConfig valuationConfig = valuationConfigMapper.selectOne(new LambdaQueryWrapper<ValuationConfig>()
                .eq(ValuationConfig::getId, id));
        if (ObjectUtils.isEmpty(valuationConfig)) {
            putMsg(result, CONFIG_NOT_EXIST, id);
            return result;
        }

        valuationConfig.setName(name).setAttriConfigs(attriConfigs).setRangeConfigs(rangeConfigs)
                .setCompuFormulas(compuFormulas);
        int updateRes = valuationConfigMapper.updateById(valuationConfig);

        return success(result, updateRes);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> deletValuationConfig(Authority authority, List<Integer> ids) {
        Map<String, Object> result = new HashMap<>();

        if (CollectionUtils.isEmpty(ids)) {
            return success(result, 0);
        }
        int deleteRes = valuationConfigMapper.deleteBatchIds(ids);

        return success(result, deleteRes);
    }

    private boolean checkName(String name, Map<String, Object> result) {
        List<ValuationConfig> valuationConfigs = valuationConfigMapper.selectList(new LambdaQueryWrapper<ValuationConfig>()
                .eq(ValuationConfig::getName, name));
        if (CollectionUtils.isNotEmpty(valuationConfigs)) {
            putMsg(result, CONFIG_NAME_EXIST, name);
            return false;
        }
        return true;
    }
}
