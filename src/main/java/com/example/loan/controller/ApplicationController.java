package com.example.loan.controller;

import com.example.loan.dto.ApplicationDTO;
import com.example.loan.dto.ApplicationDTO.Response;
import com.example.loan.dto.ResponseDTO;
import com.example.loan.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.loan.dto.ResponseDTO.ok;

@RequiredArgsConstructor
@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseDTO<Response> create(@RequestBody ApplicationDTO.Request request) {
        return ok(applicationService.create(request));
    }
}
