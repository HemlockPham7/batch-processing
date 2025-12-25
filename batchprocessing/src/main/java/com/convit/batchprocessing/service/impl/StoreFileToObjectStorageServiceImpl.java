package com.convit.batchprocessing.service.impl;

import com.convit.batchprocessing.service.StoreFileToObjectStorageUseCase;
import com.convit.batchprocessing.service.dto.ObjectDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.CompletedFileUpload;
import software.amazon.awssdk.transfer.s3.model.FileUpload;
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;

@Component
@RequiredArgsConstructor
@Slf4j
public class StoreFileToObjectStorageServiceImpl implements StoreFileToObjectStorageUseCase {


    private final S3TransferManager s3TransferManager;

    @Value("${cos.aws.s3.bucket.name}")
    private String bucket;

    @Override
    public String store(ObjectDTO objectDTO) {
        createBucketIfDoesNotExist(bucket);

        UploadFileRequest uploadFileRequest = UploadFileRequest.builder()
                .putObjectRequest(builder -> builder.bucket(bucket)
                        .key(objectDTO.name()))
                .source(objectDTO.path())
                .build();

        FileUpload fileUpload = s3TransferManager.uploadFile(uploadFileRequest);

        CompletedFileUpload completedFileUpload = fileUpload.completionFuture().join();
        boolean successful = completedFileUpload.response().sdkHttpResponse().isSuccessful();

        log.info("-----------> upload succeeded: {}", successful);

        return objectDTO.name();
    }

    @Override
    public void createBucketIfDoesNotExist(final String bucketName) {
        Assert.hasText(bucketName, "The bucket name should not be blank");
    }

}