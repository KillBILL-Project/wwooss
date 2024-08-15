package com.bigbro.wwooss.v1.api.wallet;

import com.bigbro.wwooss.v1.dto.request.user.UserCredential;
import com.bigbro.wwooss.v1.enumType.IssueType;
import com.bigbro.wwooss.v1.response.WwoossResponse;
import com.bigbro.wwooss.v1.response.WwoossResponseUtil;
import com.bigbro.wwooss.v1.service.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/wallet")
@RestController
@RequiredArgsConstructor
public class WalletApi {

    private final WalletService walletService;

    @GetMapping("/wwooss-token")
    public ResponseEntity<WwoossResponse<Double>> getTotalTokenByType(UserCredential userCredential, @RequestParam("issue-type") IssueType issueType) {
        return WwoossResponseUtil.responseOkAddData(walletService.getTotalTokenValueByType(userCredential.getUserId(), issueType));
    }
}
