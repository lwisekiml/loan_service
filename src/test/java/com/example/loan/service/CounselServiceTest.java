package com.example.loan.service;

import com.example.loan.domain.Counsel;
import com.example.loan.dto.CounselDTO.Request;
import com.example.loan.dto.CounselDTO.Response;
import com.example.loan.exception.BaseException;
import com.example.loan.exception.ResultType;
import com.example.loan.repository.CounselRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * CounselService에 대한 서비스 레이어에 대한 비즈니스 로직을 테스트 하는 것이다.
 * 그 서비스 레이어에서 다른 레이어를 호출하는 부분에 대해서 혹은 다른 의존성이 걸려있는 영역에 대해서는 검증할 필요가 없다.
 * 그래서 CounselService 외에 호출되는 부분에 대해서는 이미 성공했다는 가정하에 그쪽에서 반환되는 기대 결과가 들어온다고 가정한다.
 * CounselService에서 구현되어 있는 순수 비즈니스 로직만 검증하는 형식으로 진행할 것이라 목킹 처리
 */
@ExtendWith(MockitoExtension.class)
class CounselServiceTest {

    @InjectMocks
    CounselServiceImpl counselService;

    @Mock
    private CounselRepository counselRepository;

    @Spy // 목킹처리 보단 ModelMapper 말 그대로 각각 다른 오브젝트를 맵핑해주는 유틸성으로 모킹처리 없이 역할 자체를 순수하게 하기 위해서 @Spy로 지정
    private ModelMapper modelMapper;

    @DisplayName("상담 등록을 요청하면 Counsel Entity의 Response를 리턴한다.")
    @Test
    void Should_ReturnResponseOfNewCounselEntity_When_RequestCounsel() {
        Counsel entity = Counsel.builder()
                .name("Member Kim")
                .cellPhone("010-1111-2222")
                .email("mail@abc.de")
                .memo("I hope to get a loan")
                .zipCode("123456")
                .address("Somewhere in Gangnam-gu, Seoul")
                .addressDetail("What Apartment No. 101, 1st floor No. 101")
                .build();

        Request request = Request.builder()
                .name("Member Kim")
                .cellPhone("010-1111-2222")
                .email("mail@abc.de")
                .memo("I hope to get a loan")
                .zipCode("123456")
                .address("Somewhere in Gangnam-gu, Seoul")
                .addressDetail("What Apartment No. 101, 1st floor No. 101")
                .build();

        when(counselRepository.save(ArgumentMatchers.any(Counsel.class))).thenReturn(entity);

        Response actual = counselService.create(request);

        assertThat(actual.getName()).isSameAs(entity.getName());
    }

    @DisplayName("요청한 Counsel ID가 존재할 때 Counsel Entity의 Response를 리턴한다.")
    @Test
    void Should_ReturnResponseOfExistCounselEntity_When_RequestExistCounselId() {
        Long findId = 1L;

        Counsel entity = Counsel.builder()
                .counselId(1L)
                .build();

        // 1번은 있는 엔티티라 가정, 없을 수 도 있으니까 ofNullable로 감싼다.
        when(counselRepository.findById(findId)).thenReturn(Optional.ofNullable(entity));

        Response actual = counselService.get(findId);

        assertThat(actual.getCounselId()).isSameAs(findId);
    }

    @DisplayName("없는 Id에 대한 엔티티 조회 요청을 실패 케이스 테스트")
    @Test
    void Should_ThrowException_When_RequestNotExistCounselId() {
        Long findId = 2L;

        when(counselRepository.findById(findId)).thenThrow(new BaseException(ResultType.SYSTEM_ERROR));

        Assertions.assertThrows(BaseException.class, () -> counselService.get(2L));
    }

    @DisplayName("존재하는 상담 정보를 수정 요청할 때 Update 된 Counsel Entity의 Response를 리턴한다.")
    @Test
    void Should_ReturnUpdatedResponseOfExistCounselEntity_When_RequestUpdateExistCounselInfo() {
        Long findId = 1L;

        Counsel entity = Counsel.builder()
                .counselId(1L)
                .name("Member Kim")
                .build();

        Request request = Request.builder()
                .name("Member Kang")
                .build();

        when(counselRepository.save(ArgumentMatchers.any(Counsel.class))).thenReturn(entity);
        when(counselRepository.findById(findId)).thenReturn(Optional.ofNullable(entity));

        Response actual = counselService.update(findId, request);

        assertThat(actual.getCounselId()).isSameAs(findId);
        assertThat(actual.getName()).isSameAs(request.getName());
    }

    @DisplayName("존재하는 상담 정보를 삭제 요청하면 상담 정보를 삭제한다.")
    @Test
    void Should_DeletedCounselEntity_When_RequestDeleteExistCounselInfo() {
        Long targetId = 1L;

        Counsel entity = Counsel.builder()
                .counselId(1L)
                .build();

        when(counselRepository.save(ArgumentMatchers.any(Counsel.class))).thenReturn(entity);
        when(counselRepository.findById(targetId)).thenReturn(Optional.ofNullable(entity));

        counselService.delete(targetId);

        assertThat(entity.getIsDeleted()).isSameAs(true);
    }
}
