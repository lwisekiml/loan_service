package com.example.loan.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ApplicationDTO implements Serializable {

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class Request {
        private String name;

        private String cellPhone;

        private String email;

        private BigDecimal hopeAmount;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class Response {

        private Long applicationId;

        private String name;

        private String cellPhone;

        private String email;

        private BigDecimal hopeAmount;

        private LocalDateTime appliedAt;

        private LocalDateTime contractedAt; //

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class AcceptTerms { // 약관에 동의를 했을 때 해당하는 약관의 id를 list로 묶어서 프론트에서 api쪽으로 던져준다고 가정(약관에 동의 체크를 몇개 했는지)
        List<Long> acceptTermsIds;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class GrantAmount {

        private Long applicationId;

        private BigDecimal approvalAmount;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;
    }
}
