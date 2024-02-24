package com.example.loan.service;

import com.example.loan.dto.JudgmentDTO.Request;
import com.example.loan.dto.JudgmentDTO.Response;

public interface JudgmentService {

    Response create(Request request);

    Response get(Long judgmentId);

    Response getJudgmentOfApplication(Long applicationId); // 대출 신청 정보에 해당하는 Judgment를 가져온다.

    Response update(Long judgmentId, Request request);

}
