package com.icbc.valuation.service;

import com.icbc.valuation.model.Authority;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public interface ValuateService {

    Map<String, Object> compute(Authority authority, Integer configId);

    Map<String, Object> upload(Authority authority, MultipartFile file) throws IOException;

    Map<String, Object> clean(Authority authority);
}
