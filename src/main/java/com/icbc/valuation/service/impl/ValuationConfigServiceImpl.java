package com.icbc.valuation.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icbc.valuation.entity.AttriConfig;
import com.icbc.valuation.entity.CompuFormula;
import com.icbc.valuation.entity.RangeConfig;
import com.icbc.valuation.entity.ValuationConfig;
import com.icbc.valuation.mapper.AttriConfigMapper;
import com.icbc.valuation.mapper.CompuFormulaMapper;
import com.icbc.valuation.mapper.RangeConfigMapper;
import com.icbc.valuation.mapper.ValuationConfigMapper;
import com.icbc.valuation.model.AttriConfigModel;
import com.icbc.valuation.model.Authority;
import com.icbc.valuation.model.FieldScore;
import com.icbc.valuation.model.ValuationConfigModel;
import com.icbc.valuation.service.ValuationConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.icbc.valuation.model.enums.Status.CONFIG_NAME_EXIST;
import static com.icbc.valuation.model.enums.Status.CONFIG_NOT_EXIST;

@Service
@Slf4j
public class ValuationConfigServiceImpl extends BaseService implements ValuationConfigService {


    @Resource
    ValuationConfigMapper valuationConfigMapper;
    // TODO: attriConfig数据库可优化,将fieldScores再拆成一个表
    @Resource
    AttriConfigMapper attriConfigMapper;
    @Resource
    RangeConfigMapper rangeConfigMapper;
    @Resource
    CompuFormulaMapper compuFormulaMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> createValuationConfig(
            Authority authority, String name, String description, String status,
            String attriConfigs, String rangeConfigs, String compuFormulas) {
        Map<String, Object> result = new HashMap<>();

        if (!checkName(name, result)) {
            return result;
        }
        //String configId = randomUUID().toString();
        ValuationConfig valuationConfig = new ValuationConfig()//.setId(configId)
                .setName(name).setDescription(description).setStatus(status);
        valuationConfigMapper.insert(valuationConfig);

        return success(result, valuationConfig.getId());
    }

    @Override
    public Map<String, Object> getValuationConfigDetail(Authority authority, Integer id) {
        Map<String, Object> result = new HashMap<>();

        ValuationConfig valuationConfig = valuationConfigMapper.selectOne(
                new QueryWrapper<ValuationConfig>().lambda()
                        .eq(ValuationConfig::getId, id));
        if (ObjectUtils.isEmpty(valuationConfig)) {
            putMsg(result, CONFIG_NOT_EXIST, id);
            return result;
        }

        List<AttriConfig> attriConfigs = attriConfigMapper.selectList(new LambdaQueryWrapper<AttriConfig>()
                .eq(AttriConfig::getConfigId, valuationConfig.getId()));
        List<AttriConfigModel> attriConfigModels = attriConfigs.stream().map(attriConfig ->
                new AttriConfigModel().setName(attriConfig.getName())
                .setFieldScores(JSON.parseObject(attriConfig.getFieldScores(), new TypeReference<List<FieldScore>>() {})))
                .collect(Collectors.toList());

        List<RangeConfig> rangeConfigs = rangeConfigMapper.selectList(new LambdaQueryWrapper<RangeConfig>()
                .eq(RangeConfig::getConfigId, valuationConfig.getId()));

        List<CompuFormula> compuFormulas = compuFormulaMapper.selectList(new LambdaQueryWrapper<CompuFormula>()
                .eq(CompuFormula::getConfigId, valuationConfig.getId()));

        ValuationConfigModel valuationConfigModel = new ValuationConfigModel().setId(valuationConfig.getId())
                .setName(valuationConfig.getName()).setDescription(valuationConfig.getDescription())
                .setStatus(valuationConfig.getStatus())
                .setAttriConfigs(attriConfigModels)
                .setRangeConfigs(rangeConfigs)
                .setCompuFormulas(compuFormulas);

        return success(result, valuationConfigModel);
    }

    @Override
    public Map<String, Object> getValuationConfigs(Authority authority, int currentPage, int pageSize, String queryString, String status) {
        Map<String, Object> result = new HashMap<>();

        LambdaQueryWrapper<ValuationConfig> queryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(status)) {
            queryWrapper.eq(ValuationConfig::getStatus, status);
        }
        if (StringUtils.hasText(queryString)) {
            queryWrapper.and(wrapper ->
                    wrapper.like(ValuationConfig::getName, queryString).or().like(ValuationConfig::getCreateUser, queryString)
                        .or().like(ValuationConfig::getModifyUser, queryString).or().like(ValuationConfig::getDescription, queryString)
            );
        }
//        queryWrapper.orderByAsc(ValuationConfig::getName);
        Page<ValuationConfig> page = new Page<>(currentPage, pageSize);
        IPage<ValuationConfig> iPage = valuationConfigMapper.selectPage(page, queryWrapper);

        return success(result, iPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> updateValuationConfig(
            Authority authority, Integer id, ValuationConfigModel valuationConfigModel) {
        Map<String, Object> result = new HashMap<>();

        if (!checkName(id, valuationConfigModel.getName(), result)) {
            return result;
        }

        ValuationConfig valuationConfig = valuationConfigMapper.selectOne(new LambdaQueryWrapper<ValuationConfig>()
                .eq(ValuationConfig::getId, id));
        if (ObjectUtils.isEmpty(valuationConfig)) {
            putMsg(result, CONFIG_NOT_EXIST, id);
            return result;
        }

        valuationConfig.setName(valuationConfigModel.getName())
                .setDescription(valuationConfigModel.getDescription()).setStatus(valuationConfigModel.getStatus());
        int updateRes = valuationConfigMapper.updateById(valuationConfig);

        attriConfigMapper.delete(new LambdaQueryWrapper<AttriConfig>().eq(AttriConfig::getConfigId, valuationConfig.getId()));
        valuationConfigModel.getAttriConfigs().forEach(attriConfigModel -> {
            AttriConfig attriConfig = new AttriConfig().setConfigId(valuationConfig.getId())
                    .setName(attriConfigModel.getName()).setFieldScores(JSON.toJSONString(attriConfigModel.getFieldScores()));
            attriConfigMapper.insert(attriConfig);
        });

        rangeConfigMapper.delete(new LambdaQueryWrapper<RangeConfig>().eq(RangeConfig::getConfigId, valuationConfig.getId()));
        valuationConfigModel.getRangeConfigs().forEach(rangeConfig -> {
            rangeConfig.setConfigId(valuationConfig.getId());
            rangeConfigMapper.insert(rangeConfig);
        });

        compuFormulaMapper.delete(new LambdaQueryWrapper<CompuFormula>().eq(CompuFormula::getConfigId, valuationConfig.getId()));
        valuationConfigModel.getCompuFormulas().forEach(compuFormula -> {
            compuFormula.setConfigId(valuationConfig.getId());
            compuFormulaMapper.insert(compuFormula);
        });

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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> changeConfigStatus(Authority authority, Integer id, String status) {
        Map<String, Object> result = new HashMap<>();

        ValuationConfig valuationConfig = valuationConfigMapper.selectOne(new LambdaQueryWrapper<ValuationConfig>()
                .eq(ValuationConfig::getId, id));
        if (ObjectUtils.isEmpty(valuationConfig)) {
            putMsg(result, CONFIG_NOT_EXIST, id);
            return result;
        }

        valuationConfig.setStatus(status);
        int updateRes = valuationConfigMapper.updateById(valuationConfig);

        return success(result, updateRes);
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

    private boolean checkName(Integer id, String name, Map<String, Object> result) {
        List<ValuationConfig> valuationConfigs = valuationConfigMapper.selectList(new LambdaQueryWrapper<ValuationConfig>()
                .eq(ValuationConfig::getName, name).ne(ValuationConfig::getId, id));
        if (CollectionUtils.isNotEmpty(valuationConfigs)) {
            putMsg(result, CONFIG_NAME_EXIST, name);
            return false;
        }
        return true;
    }
}
