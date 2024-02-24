package com.example.loan.service;

import com.example.loan.dto.BalanceDTO;
import com.example.loan.dto.BalanceDTO.RepaymentRequest;
import com.example.loan.dto.BalanceDTO.Request;
import com.example.loan.dto.BalanceDTO.Response;
import com.example.loan.dto.BalanceDTO.UpdateRequest;

public interface BalanceService {

    Response create(Long applicationId, Request request);

    Response update(Long applicationId, UpdateRequest request);

    // 상환금을 넣었을 때도 반영, 상환금에 대해 원복을 해야되는 경우도 있다. 잘못된 상환에 대한 롤백 기능 고료
    Response repaymentUpdate(Long applicationId, RepaymentRequest request);

}
