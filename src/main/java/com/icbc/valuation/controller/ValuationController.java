package com.icbc.valuation.controller;

import com.icbc.valuation.configuration.aspect.ApiException;
import com.icbc.valuation.model.Authority;
import com.icbc.valuation.model.Result;
import com.icbc.valuation.service.ValuateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

import static com.icbc.valuation.model.Constants.AUTHORITY;
import static com.icbc.valuation.model.enums.Status.*;

@Api(tags = "上传excel文件与按公式计算")
@RestController
@RequestMapping("/valuation")
public class ValuationController extends BaseController {
    @Resource
    private ValuateService valuateService;

    @ApiOperation(value = "按公式计算", notes = "返回excel结果")
    @GetMapping(value = "/compute/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(COMPUTE_FAILED)
    public Result<Object> compute(
            @ApiIgnore @RequestAttribute(value = AUTHORITY) Authority authority,
            @PathVariable("id") Integer configId) {
        Map<String, Object> result = valuateService.compute(authority, configId);
        return returnData(result);
    }

    @ApiOperation(value = "上传excel", notes = "输入的excel以列名为参数名")
    @PostMapping(value = "/upload")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(DATA_UPLOAD_FAILED)
    public Result<Object> upload(
            @ApiIgnore @RequestAttribute(value = AUTHORITY) Authority authority,
            @RequestPart MultipartFile file) throws IOException {
        Map<String, Object> result = valuateService.upload(authority, file);
        return returnData(result);
    }

    @ApiOperation(value = "模型重启", notes = "清除已训练的参数，重新训练")
    @DeleteMapping(value = "/clean")
    @ResponseStatus(HttpStatus.OK)
    @ApiException(CLEAN_FAILED)
    public Result<Object> clean(@ApiIgnore @RequestAttribute(value = AUTHORITY) Authority authority) {
        Map<String, Object> result = valuateService.clean(authority);
        return success();
    }
}
