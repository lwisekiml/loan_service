package com.example.loan.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    void save(MultipartFile file);
}