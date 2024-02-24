package com.example.loan.controller;

import com.example.loan.dto.EntryDTO;
import com.example.loan.dto.EntryDTO.Request;
import com.example.loan.dto.EntryDTO.Response;
import com.example.loan.dto.EntryDTO.UpdateResponse;
import com.example.loan.dto.RepaymentDTO;
import com.example.loan.dto.RepaymentDTO.ListResponse;
import com.example.loan.dto.ResponseDTO;
import com.example.loan.service.EntryService;
import com.example.loan.service.RepaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 사후 처리에 대한 모든 관리를 내부 임직원이 한다라고 가정해서 Internal로 한다.
@RequiredArgsConstructor
@RestController
@RequestMapping("/internal/applications")
public class InternalController extends AbstractController {

    private final EntryService entryService;

    private final RepaymentService repaymentService;


    @PostMapping("{applicationId}/entries")
    public ResponseDTO<Response> create(@PathVariable Long applicationId, @RequestBody Request request) {
        return ok(entryService.create(applicationId, request));
    }

    @GetMapping("/{applicationId}/entries")
    public ResponseDTO<Response> get(@PathVariable Long applicationId) {
        return ok(entryService.get(applicationId));
    }

    @PutMapping("/entries/{entryId}")
    public ResponseDTO<UpdateResponse> update(@PathVariable Long entryId, @RequestBody Request request) {
        return ok(entryService.update(entryId, request));
    }

    @DeleteMapping("/entries/{entryId}")
    public ResponseDTO<Void> delete(@PathVariable Long entryId) {
        entryService.delete(entryId);
        return ok();
    }

    @PostMapping("{applicationId}/repayments")
    public ResponseDTO<RepaymentDTO.Response> create(@PathVariable Long applicationId, @RequestBody RepaymentDTO.Request request) {
        return ok(repaymentService.create(applicationId, request));
    }

    @GetMapping("{applicationId}/repayments")
    public ResponseDTO<List<ListResponse>> getPayments(@PathVariable Long applicationId) {
        return ok(repaymentService.get(applicationId));
    }
}
