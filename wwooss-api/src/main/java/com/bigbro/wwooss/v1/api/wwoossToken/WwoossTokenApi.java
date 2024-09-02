package com.bigbro.wwooss.v1.api.wwoossToken;


import com.bigbro.wwooss.v1.dto.request.user.UserCredential;
import com.bigbro.wwooss.v1.dto.request.wwoossToken.IssueTokenRequest;
import com.bigbro.wwooss.v1.dto.response.wwoossToken.IssuedWwoossToken;
import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import com.bigbro.wwooss.v1.service.wwoossToken.WwoossTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/wwooss-token")
@RequiredArgsConstructor
public class WwoossTokenApi {

    private final WwoossTokenService wwoossTokenService;

    @PostMapping
    public ResponseEntity<WwoossResponse<IssuedWwoossToken>> issueToken(UserCredential userCredential, @Valid @RequestBody IssueTokenRequest issueTokenRequest) {
        return WwoossResponseUtil.responseCreatedAddData(wwoossTokenService.issueWwoossToken(userCredential.getUserId(), issueTokenRequest.getIssueType()));
    }

}
