package com.bigbro.wwooss.v1.api.common;

import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/health")
@RequiredArgsConstructor
public class HealthCheck {
    @GetMapping
    public ResponseEntity<WwoossResponse<Void>> health() {
        return WwoossResponseUtil.responseOkNoData();
    }
}
