package com.example.loan.service;

import com.example.loan.domain.Application;
import com.example.loan.domain.Judgment;
import com.example.loan.dto.ApplicationDTO.GrantAmount;
import com.example.loan.dto.JudgmentDTO;
import com.example.loan.dto.JudgmentDTO.Request;
import com.example.loan.dto.JudgmentDTO.Response;
import com.example.loan.exception.BaseException;
import com.example.loan.exception.ResultType;
import com.example.loan.repository.ApplicationRepository;
import com.example.loan.repository.JudgmentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class JudgmentServiceImpl implements JudgmentService {

    private final JudgmentRepository judgmentRepository;

    private final ApplicationRepository applicationRepository;

    private final ModelMapper modelMapper;

    @Override
    public Response create(Request request) {
        // 신청 정보 검증
        Long applicationId = request.getApplicationId();
        if (!isPresentApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        // request dto -> entity -> save
        Judgment judgment = modelMapper.map(request, Judgment.class);

        Judgment save = judgmentRepository.save(judgment);

        // save -> response dto
        return modelMapper.map(save, Response.class);
    }

    @Override
    public Response get(Long judgmentId) {
        Judgment judgment = judgmentRepository.findById(judgmentId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        return modelMapper.map(judgment, JudgmentDTO.Response.class);
    }

    @Override
    public Response getJudgmentOfApplication(Long applicationId) {
        if (!isPresentApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        // 신청 정보에 심사가 완료되지 않았을 경우에는 심사 정보가 없을테니 그때는 없다는 것을 에러를 통해 반환
        Judgment judgment = judgmentRepository.findByApplicationId(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        return modelMapper.map(judgment, JudgmentDTO.Response.class);
    }

    @Override
    public Response update(Long judgmentId, Request request) {
        Judgment judgment = judgmentRepository.findById(judgmentId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        judgment.setName(request.getName());
        judgment.setApprovalAmount(request.getApprovalAmount());

        judgmentRepository.save(judgment);

        return modelMapper.map(judgment, JudgmentDTO.Response.class);
    }

    @Override
    public void delete(Long judgmentId) {
        Judgment judgment = judgmentRepository.findById(judgmentId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        judgment.setIsDeleted(true);

        judgmentRepository.save(judgment);
    }

    @Override
    public GrantAmount grant(Long judgmentId) {
        Judgment judgment = judgmentRepository.findById(judgmentId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        Long applicationId = judgment.getApplicationId();
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        BigDecimal approvalAmount = judgment.getApprovalAmount();
        application.setApprovalAmount(approvalAmount);

        applicationRepository.save(application);

        return modelMapper.map(application, GrantAmount.class);
    }

    private boolean isPresentApplication(Long applicationId) {
        return applicationRepository.findById(applicationId).isPresent();
    }
}
