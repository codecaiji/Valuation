package com.icbc.valuation.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icbc.valuation.entity.AttriConfig;
import com.icbc.valuation.entity.RangeConfig;
import com.icbc.valuation.entity.ValuationConfig;
import com.icbc.valuation.mapper.AttriConfigMapper;
import com.icbc.valuation.mapper.CompuFormulaMapper;
import com.icbc.valuation.mapper.RangeConfigMapper;
import com.icbc.valuation.mapper.ValuationConfigMapper;
import com.icbc.valuation.model.AttriConfigModel;
import com.icbc.valuation.model.Authority;
import com.icbc.valuation.entity.CompuFormula;
import com.icbc.valuation.model.FieldScore;
import com.icbc.valuation.model.SheetData;
import com.icbc.valuation.service.ValuateService;
import com.icbc.valuation.utils.PoiUtils;
import com.icbc.valuation.utils.ValuationUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.icbc.valuation.model.Constants.UPLOAD_DATA;
import static com.icbc.valuation.model.enums.Status.CONFIG_NOT_EXIST;
import static com.icbc.valuation.model.enums.Status.DATA_EMPTY;


@Service
@Slf4j
public class ValuateServiceImpl extends BaseService implements ValuateService {
//    private List<UploadSheetData> uploadData = new LinkedList<>();

    @Autowired
    HttpServletRequest request;
    @Autowired
    HttpServletResponse response;
    @Resource
    ValuationConfigMapper valuationConfigMapper;
    @Resource
    AttriConfigMapper attriConfigMapper;
    @Resource
    RangeConfigMapper rangeConfigMapper;
    @Resource
    CompuFormulaMapper compuFormulaMapper;

    @Override
    public Map<String, Object> compute(Authority authority, Integer configId) {
        Map<String, Object> result = new HashMap<>();
        List<SheetData> uploadData = JSON.parseObject(JSON.toJSONString(request.getSession().getAttribute(UPLOAD_DATA)),
                new TypeReference<List<SheetData>>(){});
        /*List<SheetData> uploadData = new LinkedList<>();
        uploadData.addAll((List<SheetData>)request.getSession().getAttribute(UPLOAD_DATA));*/
        if (CollectionUtils.isEmpty(uploadData)) {
            putMsg(result, DATA_EMPTY);
            return result;
        }

        ValuationConfig valuationConfig = valuationConfigMapper.selectOne(
                new QueryWrapper<ValuationConfig>().lambda().eq(ValuationConfig::getId, configId));
        if (ObjectUtils.isEmpty(valuationConfig)) {
            putMsg(result, CONFIG_NOT_EXIST, configId);
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

        Map<String, Map<String, Double>> attriConfigsMap = ValuationUtil.attriListToMap(attriConfigModels);
        ValuationUtil.transToScoresByConfig(uploadData, attriConfigsMap, rangeConfigs);

        List<CompuFormula> compuFormulas = compuFormulaMapper.selectList(new LambdaQueryWrapper<CompuFormula>()
                .eq(CompuFormula::getConfigId, valuationConfig.getId()));
        List<SheetData> outputList = ValuationUtil.computeByformulas(uploadData, compuFormulas);

        //输出excel
        PoiUtils poiUtils = new PoiUtils();
//        poiUtils.output(response, outputList);
        /*poiUtils.output(outputList);
        return success(result, outputList);*/
        return success(result, poiUtils.outputToBase64(outputList));
    }

    @Override
    public Map<String, Object> upload(Authority authority, MultipartFile file) throws IOException {
        //上传配置
        //EasyExcel.read(file.getInputStream(), UploadData.class, new UploadDataListener()).sheet().doRead();
        Map<String, Object> result = new HashMap<>();

        if (file.isEmpty()) {
            putMsg(result, DATA_EMPTY);
            return result;
        }
        PoiUtils poiUtils = new PoiUtils(file.getInputStream());

        request.getSession().setAttribute(UPLOAD_DATA, poiUtils.upload());

        return success(result, request.getSession().getAttribute(UPLOAD_DATA));
    }

    @Override
    public Map<String, Object> clean(Authority authority) {
        Map<String, Object> result = new HashMap<>();

        request.getSession().removeAttribute(UPLOAD_DATA);
        return result;
    }

}
