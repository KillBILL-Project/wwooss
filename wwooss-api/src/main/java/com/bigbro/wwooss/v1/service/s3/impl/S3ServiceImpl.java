package com.bigbro.wwooss.v1.service.s3.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.bigbro.wwooss.v1.exception.S3Exception;
import com.bigbro.wwooss.v1.service.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.bigbro.wwooss.v1.response.WwoossResponseCode.MULTIPART_ERROR;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String baseBucket;

    @Override
    public String s3Upload(MultipartFile image, String bucket) throws IOException {
        validateImageFileExtention(image);
        return uploadImageToS3(image, bucket);
    }

    private void validateImageFileExtention(MultipartFile image) {
        String fileName = image.getOriginalFilename();
        if(image.isEmpty() || Objects.isNull(fileName)){
            throw new S3Exception(MULTIPART_ERROR, "File이 존재하지 않습니다.");
        }

        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new S3Exception(MULTIPART_ERROR, "확장자가 존재하지 않습니다.");
        }

        String extention = fileName.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png");

        if (!allowedExtentionList.contains(extention)) {
            throw new S3Exception(MULTIPART_ERROR, "지원되지 않는 확장자입니다. - [jpg, jpeg, png]");
        }

    }

    private String uploadImageToS3(MultipartFile image, String bucket) throws IOException {
        String originalFilename = image.getOriginalFilename(); //원본 파일 명
        String extention = originalFilename.substring(originalFilename.lastIndexOf(".")); //확장자 명

        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + originalFilename; //변경된 파일 명

        InputStream is = image.getInputStream();
        byte[] bytes = IOUtils.toByteArray(is); //image를 byte[]로 변환

        ObjectMetadata metadata = new ObjectMetadata(); //metadata 생성
        metadata.setContentType("image/" + extention);
        metadata.setContentLength(bytes.length);

        //S3에 요청할 때 사용할 byteInputStream 생성
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        try{
            //S3로 putObject 할 때 사용할 요청 객체
            //생성자 : bucket 이름, 파일 명, byteInputStream, metadata
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(baseBucket + bucket , s3FileName, byteArrayInputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead);

            amazonS3Client.putObject(putObjectRequest); // put image to S3
        } catch (Exception e){
            throw new S3Exception(MULTIPART_ERROR, "S3 upload fail " + e.getMessage());
        } finally {
            byteArrayInputStream.close();
            is.close();
        }

        return s3FileName;
    }
}
