package com.example.loan.repository;

import java.util.Optional;

import com.example.loan.domain.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {

    // 대출 신청에 엮여 있는 balance를 찾기 위함
    Optional<Balance> findByApplicationId(Long applicationId);
}

