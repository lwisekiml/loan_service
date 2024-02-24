package com.example.loan.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class BalanceDTO implements Serializable {

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class Request {
        private Long applicationId; // 어떤 신청 정보의

        private BigDecimal entryAmount; // 상환인지
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class Response {
        private Long balanceId;

        private Long applicationId;

        private BigDecimal balance; // 잔고
    }
}

