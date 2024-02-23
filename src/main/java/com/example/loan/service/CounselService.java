package com.example.loan.service;

import com.example.loan.dto.CounselDTO.Request;
import com.example.loan.dto.CounselDTO.Response;

public interface CounselService {
    Response create(Request request);

    Response get(Long counselId);

    Response update(Long counselId, Request request);

    // empty body로 api에 대한 응답 값을 내려줌
    void delete(Long counselId);
}
