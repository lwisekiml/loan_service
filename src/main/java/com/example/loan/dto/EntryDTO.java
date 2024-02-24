package com.example.loan.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class EntryDTO implements Serializable {

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class Request {

        private BigDecimal entryAmount; // 승인한 금액을 집행할 것이다.
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class Response {

        private Long entryId;

        private Long applicationId;

        private BigDecimal entryAmount;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;
    }
}
