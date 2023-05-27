package com.bigbro.killbill.v1.config;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

public interface DocumentConfig {

    static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(modifyUris().scheme("https").host("localhost").removePort(), prettyPrint());
    }

    static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(prettyPrint());
    }

//    static FieldDescriptor getSearchResponseCodeField(List<ResponseCode> responseCodes) {
//        String responseCodeStr = responseCodes.stream().map(responseCode ->
//                        String.format("%s[%s] ", responseCode.getCode(), responseCode.getMessage()))
//                .collect(Collectors.joining("/ "));
//
//        return fieldWithPath("code").description("응답 코드 - " + responseCodeStr);
//    }

}
