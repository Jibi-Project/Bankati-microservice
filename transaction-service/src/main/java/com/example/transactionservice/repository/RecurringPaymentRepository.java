package com.example.transactionservice.repository;


import com.example.transactionservice.model.RecurringPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecurringPaymentRepository extends JpaRepository<RecurringPayment, Long> {

    List<RecurringPayment> findByNextExecutionBeforeAndStatus(LocalDateTime nextExecution, String status);
}
