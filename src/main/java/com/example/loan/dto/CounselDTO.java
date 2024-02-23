package com.example.loan.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CounselDTO implements Serializable {

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class Request {

        private String name;

        private String cellPhone;

        private String email;

        private String memo;

        private String address;

        private String addressDetail;

        private String zipCode;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class Response {

        private Long counselId; // 실제로 entity가 생성되었는지

        private String name;

        private String cellPhone;

        private String email;

        private String memo;

        private String address;

        private String addressDetail;

        private String zipCode;

        private LocalDateTime appliedAt; // 신청 일자

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;
    }
}
