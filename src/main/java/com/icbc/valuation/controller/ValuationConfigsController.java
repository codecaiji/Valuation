package com.icbc.valuation.controller;

import com.icbc.valuation.configuration.aspect.ApiException;
import com.icbc.valuation.model.Authority;
import com.icbc.valuation.model.Result;
import com.icbc.valuation.model.ValuationConfigModel;
import com.icbc.valuation.service.ValuationConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.icbc.valuation.model.Constants.AUTHORITY;
import static com.icbc.valuation.model.enums.Status.*;

@Api(tags = "计算规则配置")
@RestController
@RequestMapping("/valuation-configs")
public class ValuationConfigsController extends BaseController {
    @Resource
    private ValuationConfigService valuationConfigService;

    @ApiOperation(value = "创建配置表", notes = "")
    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(CONFIG_CREATE_FAILED)
    public Result<Object> createValuationConfig(
            @ApiIgnore @RequestAttribute(value = AUTHORITY) Authority authority,
            @RequestParam(value = "name") String configName,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "status", required = false, defaultValue = "0") String status,
            @RequestParam(value = "attriConfigs", required = false, defaultValue = "[]") String attriConfigs,
            @RequestParam(value = "rangeConfigs", required = false, defaultValue = "[]") String rangeConfigs,
            @RequestParam(value = "compuFormulas", required = false, defaultValue = "[]") String compuFormulas) {
        Map<String, Object> result =
                valuationConfigService.createValuationConfig(authority, configName, description, status,
                        attriConfigs, rangeConfigs, compuFormulas);
        return returnData(result);
    }

    @ApiOperation(value = "按名称获取配置表", notes = "")
    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(CONFIG_QUERY_FAILED)
    public Result<Object> getValuationConfigByName(
            @ApiIgnore @RequestAttribute(value = AUTHORITY) Authority authority,
            @PathVariable(value = "id") Integer id) {
        Map<String, Object> result =
                valuationConfigService.getValuationConfigDetail(authority, id);
        return returnData(result);
    }

    @ApiOperation(value = "获取所有配置表", notes = "")
    @GetMapping(value = "")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(CONFIG_QUERY_LIST_FAILED)
    public Result<Object> getValuationConfigs(
            @ApiIgnore @RequestAttribute(value = AUTHORITY) Authority authority,
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = "5") int pageSize,
            @RequestParam(value = "queryString", required = false, defaultValue = "") String queryString,
            @RequestParam(value = "status", required = false, defaultValue = "") String status) {
        Map<String, Object> result =
                valuationConfigService.getValuationConfigs(authority, currentPage, pageSize, queryString, status);
        return returnData(result);
    }

    @ApiOperation(value = "修改配置表", notes = "")
    @PutMapping(value = "{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(CONFIG_UPDATE_FAILED)
    public Result<Object> updateValuationConfig(
            @ApiIgnore @RequestAttribute(value = AUTHORITY) Authority authority,
            @PathVariable(value = "id") Integer id,
            @RequestBody ValuationConfigModel valuationConfig
            /*@RequestParam(value = "id") Integer id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "attriConfigs", required = false) String attriConfigs,
            @RequestParam(value = "rangeConfigs", required = false) String rangeConfigs,
            @RequestParam(value = "compuFormulas", required = false) String compuFormulas*/) {
        Map<String, Object> result =
                valuationConfigService.updateValuationConfig(authority, id, valuationConfig);
        return returnData(result);
    }

    @ApiOperation(value = "修改共享状态", notes = "")
    @PutMapping(value = "/{id}/change-status")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(CONFIG_UPDATE_FAILED)
    public Result<Object> changeConfigStatus(
            @ApiIgnore @RequestAttribute(value = AUTHORITY) Authority authority,
            @PathVariable(value = "id") Integer id,
            @RequestParam(value = "status") String status) {
        Map<String, Object> result = valuationConfigService.changeConfigStatus(authority, id, status);
        return returnData(result);
    }

    @ApiOperation(value = "批量删除配置表", notes = "")
    @DeleteMapping(value = "")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(CONFIG_DELETE_FAILED)
    public Result<Object> deleteValuationConfig(
            @ApiIgnore @RequestAttribute(value = AUTHORITY) Authority authority,
            @RequestParam(value = "ids") List<Integer> ids) {
        Map<String, Object> result =
                valuationConfigService.deletValuationConfig(authority, ids);
        return returnData(result);
    }
}
