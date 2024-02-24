package com.example.loan.controller;

import com.example.loan.dto.EntryDTO;
import com.example.loan.dto.EntryDTO.Request;
import com.example.loan.dto.EntryDTO.Response;
import com.example.loan.dto.ResponseDTO;
import com.example.loan.service.EntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

// 사후 처리에 대한 모든 관리를 내부 임직원이 한다라고 가정해서 Internal로 한다.
@RequiredArgsConstructor
@RestController
@RequestMapping("/internal/applications")
public class InternalController extends AbstractController {

    private final EntryService entryService;

    @PostMapping("{applicationId}/entries")
    public ResponseDTO<Response> create(@PathVariable Long applicationId, @RequestBody Request request) {
        return ok(entryService.create(applicationId, request));
    }

}
