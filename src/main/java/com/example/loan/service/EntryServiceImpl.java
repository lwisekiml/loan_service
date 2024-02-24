package com.example.loan.service;

import com.example.loan.domain.Application;
import com.example.loan.domain.Entry;
import com.example.loan.dto.BalanceDTO;
import com.example.loan.dto.EntryDTO;
import com.example.loan.exception.BaseException;
import com.example.loan.exception.ResultType;
import com.example.loan.repository.ApplicationRepository;
import com.example.loan.repository.EntryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {

    private final BalanceService balanceService;

    private final EntryRepository entryRepository;

    private final ApplicationRepository applicationRepository;

    private final ModelMapper modelMapper;

    @Override // 대출 집행
    public EntryDTO.Response create(Long applicationId, EntryDTO.Request request) {
        // 계약 체결 여부 검증
        if (!isContractApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Entry entry = modelMapper.map(request, Entry.class);
        entry.setApplicationId(applicationId);

        entryRepository.save(entry);

        // 대출 잔고 관리
        balanceService.create(applicationId,
                BalanceDTO.Request.builder()
                        .entryAmount(request.getEntryAmount())
                        .build());

        return modelMapper.map(entry, EntryDTO.Response.class);
    }

    /**
     * ApplicationServiceImpl -> contract() ->
     * // 계약 체결
     * application.setContractAt(LocalDateTime.now());
     * 계약 체결할 때 현재 시간으로 설정 하였다.
     * 그래서 계약 체결된 대출 신청 정보가 있어야 한다.
     * 이 후에 약전 체결을 해야될 것이다.
     */
    private boolean isContractApplication(Long applicationId) {
        Optional<Application> existed = applicationRepository.findById(applicationId);
        if (existed.isEmpty()) { // 대출 신청 정보가 있는지
            return false;
        }

        return existed.get().getContractedAt() != null; // 대출 신청 정보에 contract이 있는지
    }


}
