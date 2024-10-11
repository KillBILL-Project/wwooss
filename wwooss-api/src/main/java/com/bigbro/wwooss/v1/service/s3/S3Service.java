package com.bigbro.wwooss.v1.service.s3;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Service {
    String s3Upload(MultipartFile image, String bucket) throws IOException;
}
