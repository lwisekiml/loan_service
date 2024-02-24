package com.example.loan.service;

import com.example.loan.exception.BaseException;
import com.example.loan.exception.ResultType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Override
    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), Paths.get(uploadPath).resolve(file.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING); // 중복된 이름의 파일이 있을 경우 덮어 쓴다.
        } catch (Exception e) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
    }

    @Override // 단건 다운로드 대출, 신청 서류 조회
    public Resource load(String fileName) {
        try {
            Path file = Paths.get(uploadPath).resolve(fileName);

            Resource resource = new UrlResource(file.toUri());

            if (resource.isReadable() || resource.exists()) {
                return resource;
            } else {
                throw new BaseException(ResultType.NOT_EXIST);
            }
        } catch (Exception e) {
            throw new BaseException(ResultType.NOT_EXIST);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            // uploadPath 경로에 있는 파일 리스트의 정보를 보여줌
            return Files.walk(Paths.get(uploadPath), 1).filter(path -> !path.equals(Paths.get(uploadPath)));
        } catch (Exception e) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(Paths.get(uploadPath).toFile());
    }
}
