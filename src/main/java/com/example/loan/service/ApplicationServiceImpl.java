package com.example.loan.service;

import com.example.loan.domain.Application;
import com.example.loan.domain.Terms;
import com.example.loan.dto.ApplicationDTO.AcceptTerms;
import com.example.loan.dto.ApplicationDTO.Request;
import com.example.loan.dto.ApplicationDTO.Response;
import com.example.loan.exception.BaseException;
import com.example.loan.exception.ResultType;
import com.example.loan.repository.AcceptTermsRepository;
import com.example.loan.repository.ApplicationRepository;
import com.example.loan.repository.JudgmentRepository;
import com.example.loan.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService{

    private final ApplicationRepository applicationRepository;

    private final TermsRepository termsRepository;

    private final AcceptTermsRepository acceptTermsRepository;

    private final JudgmentRepository judgmentRepository;

    private final ModelMapper modelMapper;

    @Override
    public Response create(Request request) {
        Application application = modelMapper.map(request, Application.class);
        application.setAppliedAt(LocalDateTime.now()); // 생성 시점이 곧 신청 시점

        Application created = applicationRepository.save(application);

        return modelMapper.map(created, Response.class);
    }

    @Override
    public Response get(Long applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });
        return modelMapper.map(application, Response.class);
    }

    @Override
    public Response update(Long applicationId, Request request) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        application.setName(request.getName());
        application.setCellPhone(request.getCellPhone());
        application.setEmail(request.getEmail());
        application.setHopeAmount(request.getHopeAmount());

        applicationRepository.save(application);

        return modelMapper.map(application, Response.class);
    }

    @Override
    public void delete(Long applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        application.setIsDeleted(true);

        applicationRepository.save(application);
    }

    @Override
    public Boolean acceptTerms(Long applicationId, AcceptTerms request) {
        // 대출 신청 정보가 없으면 에러
        applicationRepository.findById(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        // 약관이 없으면 에러
        List<Terms> termsList = termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId"));
        if (termsList.isEmpty()) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        // 약관 수와 동의한 수 비교
        List<Long> acceptTermsIds = request.getAcceptTermsIds();
        if (termsList.size() != acceptTermsIds.size()) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        // 약관이 존재하지 않는 약관에 대해 동의를 한 경우
        // 우리가 가지고 있는 약관은 1,2,3인데 client가 4,5,6 약관을 보내는 경우(없는 약관을 보낸 경우)
        List<Long> termsIds = termsList.stream().map(Terms::getTermsId).collect(Collectors.toList());
        Collections.sort(acceptTermsIds);

        if (!termsIds.containsAll(acceptTermsIds)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        for (Long termsId : acceptTermsIds) {
            com.example.loan.domain.AcceptTerms accepted = com.example.loan.domain.AcceptTerms.builder()
                    .termsId(termsId)
                    .applicationId(applicationId)
                    .build();

            acceptTermsRepository.save(accepted);
        }

        return true;
    }

    @Override
    public Response contract(Long applicationId) {
        // 신청 정보 유부
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        // 심사 정보 유무
        judgmentRepository.findById(applicationId).orElseThrow(() -> {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        });

        // 승인 금액 > 0
        /*
            대출 승인 금액은 심사 과정에서
            JudgmentServiceImpl - grant() 에서
            application.setApprovalAmount(approvalAmount);
            로 신청 정보에 같이 세팅을 해주고 있으므로 이 값을 검증
            0원일 경우 대출 신청을 못하게끔 정함
         */
        if (application.getApprovalAmount() == null
                || application.getApprovalAmount().compareTo(BigDecimal.ZERO) == 0) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        // 계약 체결
        application.setContractedAt(LocalDateTime.now());

        Application updated = applicationRepository.save(application);

        return modelMapper.map(updated, Response.class);
    }
}
