package com.example.loan.service;

import com.example.loan.dto.TermsDTO;
import com.example.loan.dto.TermsDTO.Request;

public interface TermsService {

    TermsDTO.Response create(Request request);
}
