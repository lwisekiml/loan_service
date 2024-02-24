package com.example.loan.service;

import com.example.loan.domain.Application;
import com.example.loan.domain.Judgment;
import com.example.loan.dto.ApplicationDTO;
import com.example.loan.dto.ApplicationDTO.GrantAmount;
import com.example.loan.dto.JudgmentDTO.Request;
import com.example.loan.dto.JudgmentDTO.Response;
import com.example.loan.repository.ApplicationRepository;
import com.example.loan.repository.JudgmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JudgmentServiceTest {

    @InjectMocks
    private JudgmentServiceImpl judgmentService;

    @Mock
    private JudgmentRepository judgmentRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Spy
    private ModelMapper modelMapper;

    @DisplayName("새로운 심사 등록 요청을 하면 Judgment Entity의 Response를 리턴한다.")
    @Test
    void Should_ReturnResponseOfNewJudgmentEntity_When_RequestNewJudgment() {
        Judgment judgment = Judgment.builder()
                .name("Member Kim")
                .applicationId(1L)
                .approvalAmount(BigDecimal.valueOf(50000000))
                .build();

        Application application = Application.builder()
                .applicationId(1L)
                .build();

        Request request = Request.builder()
                .name("Member Kim")
                .applicationId(1L)
                .approvalAmount(BigDecimal.valueOf(50000000))
                .build();

        // application find
        when(applicationRepository.findById(1L)).thenReturn(Optional.ofNullable(application));
        // judgment save
        when(judgmentRepository.save(ArgumentMatchers.any(Judgment.class))).thenReturn(judgment);

        Response actual = judgmentService.create(request);

        assertThat(actual.getName()).isSameAs(judgment.getName());
        assertThat(actual.getApplicationId()).isSameAs(judgment.getApplicationId());
        assertThat(actual.getApprovalAmount()).isSameAs(judgment.getApprovalAmount());
    }

    @DisplayName("요청한 심사 Id가 존재하면 Judgment Entity를 Response로 리턴한다.")
    @Test
    void Should_ReturnResponseOfExistJudgmentEntity_When_RequestExistJudgmentId() {
        Long findId = 1L;

        Judgment entity = Judgment.builder()
                .judgmentId(1L)
                .build();

        when(judgmentRepository.findById(findId)).thenReturn(Optional.ofNullable(entity));

        Response actual = judgmentService.get(1L);

        assertThat(actual.getJudgmentId()).isSameAs(findId);
    }

    // TODO: get judgment of application
    @Test
    void Should_ReturnResponseOfExistJudgmentEntity_When_RequestExistApplicationId() {
        Long findId = 1L;

        Judgment judgmentEntity = Judgment.builder()
                .judgmentId(1L)
                .build();

        Application applicationEntity = Application.builder()
                .applicationId(1L)
                .build();

        when(applicationRepository.findById(findId)).thenReturn(Optional.ofNullable(applicationEntity));
        when(judgmentRepository.findByApplicationId(findId)).thenReturn(Optional.ofNullable(judgmentEntity));

        Response actual = judgmentService.getJudgmentOfApplication(findId);

        assertThat(actual.getJudgmentId()).isSameAs(findId);
    }

    @DisplayName("요청한 심사 Id가 존재하면 정보를 업데이트하여 Judgment Entity를 Response로 리턴한다.")
    @Test
    void Should_ReturnUpdatedResponseOfExistJudgmentEntity_When_RequestUpdateExistJudgmentInfo() {
        Judgment entity = Judgment.builder()
                .judgmentId(1L)
                .name("Member Kim")
                .approvalAmount(BigDecimal.valueOf(5000000))
                .build();

        Request request = Request.builder()
                .name("Member Lee")
                .approvalAmount(BigDecimal.valueOf(10000000))
                .build();

        when(judgmentRepository.findById(1L)).thenReturn(Optional.ofNullable(entity));
        when(judgmentRepository.save(ArgumentMatchers.any(Judgment.class))).thenReturn(entity);

        Response actual = judgmentService.update(1L, request);

        assertThat(actual.getJudgmentId()).isSameAs(1L);
        assertThat(actual.getName()).isSameAs(request.getName());
        assertThat(actual.getApprovalAmount()).isSameAs(request.getApprovalAmount());
    }

    @DisplayName("요청한 심사 정보가 존재하면 Judgment Entity를 삭제한다.")
    @Test
    void Should_DeletedJudgmentEntity_When_RequestDeleteExistJudgmentInfo() {
        Long targetId = 1L;

        Judgment entity = Judgment.builder()
                .judgmentId(1L)
                .build();

        when(judgmentRepository.save(ArgumentMatchers.any(Judgment.class))).thenReturn(entity);
        when(judgmentRepository.findById(targetId)).thenReturn(Optional.ofNullable(entity));

        judgmentService.delete(targetId);

        assertThat(entity.getIsDeleted()).isSameAs(true);
    }

    @Test
    void Should_ReturnUpdatedResponseOfExistApplicationEntity_When_RequestGrantApprovalAmountOfJudgmentInfo() {
        Long findId = 1L;

        Judgment judgmentEntity = Judgment.builder()
                .name("Member Kim")
                .applicationId(findId)
                .approvalAmount(BigDecimal.valueOf(50000000))
                .build();

        Application applicationEntity = Application.builder()
                .applicationId(findId)
                .approvalAmount(BigDecimal.valueOf(50000000))
                .build();

        when(judgmentRepository.findById(findId)).thenReturn(Optional.ofNullable(judgmentEntity));
        when(applicationRepository.findById(findId)).thenReturn(Optional.ofNullable(applicationEntity));
        when(applicationRepository.save(ArgumentMatchers.any(Application.class))).thenReturn(applicationEntity);

        GrantAmount actual = judgmentService.grant(findId);

        assertThat(actual.getApplicationId()).isSameAs(findId);
        assertThat(actual.getApprovalAmount()).isSameAs(judgmentEntity.getApprovalAmount());
    }
}
