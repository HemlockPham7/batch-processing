package com.convit.batchprocessing.service;

import com.convit.batchprocessing.service.dto.ObjectDTO;

public interface StoreFileToObjectStorageUseCase {

    String store(ObjectDTO objectDTO);

    void createBucketIfDoesNotExist(String bucketName);

}