package com.example.loan.controller;

import com.example.loan.dto.ApplicationDTO.AcceptTerms;
import com.example.loan.dto.ApplicationDTO.Request;
import com.example.loan.dto.ApplicationDTO.Response;
import com.example.loan.dto.FileDTO;
import com.example.loan.dto.ResponseDTO;
import com.example.loan.service.ApplicationService;
import com.example.loan.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.loan.dto.ResponseDTO.ok;

@RequiredArgsConstructor
@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    private final FileStorageService fileStorageService;

    @PostMapping
    public ResponseDTO<Response> create(@RequestBody Request request) {
        return ok(applicationService.create(request));
    }

    @GetMapping("/{applicationId}")
    public ResponseDTO<Response> get(@PathVariable Long applicationId) {
        return ok(applicationService.get(applicationId));
    }

    @PutMapping("/{applicationId}")
    public ResponseDTO<Response> update(@PathVariable Long applicationId, @RequestBody Request request) {
        return ok(applicationService.update(applicationId, request));
    }

    @DeleteMapping("/{applicationId}")
    public ResponseDTO<Void> delete(@PathVariable Long applicationId) {
        applicationService.delete(applicationId);
        return ok();
    }

    @PostMapping("/{applicationId}/terms")
    public ResponseDTO<Boolean> acceptTerms(@PathVariable Long applicationId, @RequestBody AcceptTerms request) {
        return ok(applicationService.acceptTerms(applicationId, request));
    }

    @PostMapping("/files")
    public ResponseDTO<Void> upload(MultipartFile file) {
        fileStorageService.save(file);
        return ok();
    }

    @GetMapping("/files")
    public ResponseEntity<Resource> download(@RequestParam(value = "fileName") String fileName) {
        Resource file = fileStorageService.load(fileName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    // 한 대출 신청 정보에 어떤 입회 서류들이 올라와 있는지 확인해야 되는 경우가 있다.
    // 내가 없로드한 파일 정보를 알 수 있는 기능
    @GetMapping("/files/infos")
    public ResponseDTO<List<FileDTO>> getFileInfos() {
        List<FileDTO> fileInfos = fileStorageService.loadAll().map(path -> {
            String fileName = path.getFileName().toString();
            return FileDTO.builder()
                    .name(fileName)
                    /* 파일 업로드한 리소스라고 하죠 모든 리소스(자원)은 URI라는 것을 통해 그 위치를 나타낸다.
                     * 그래서 여기서 파일 데이터를 리턴해 주기 위해 내부적으로 URI를 통해 정보를 요청하고 그 요청한 정보를 받아와서 client에 내려 주어야 한다.
                     * URI를 통해 제공해 줄때 MVC URI 컴포넌트 빌더를 사용해서 실제로 다운로드할 파일을 (위에거 만들어 놓은) download()를 통해 URI를 준비 해준다.
                     */
                    .url(MvcUriComponentsBuilder.fromMethodName(ApplicationController.class, "download", fileName).build().toString())
                    .build();
        }).collect(Collectors.toList());
        return ok(fileInfos);
    }

    @DeleteMapping("/files")
    public ResponseDTO<Void> deleteAll() {
        fileStorageService.deleteAll();
        return ok();
    }
}
