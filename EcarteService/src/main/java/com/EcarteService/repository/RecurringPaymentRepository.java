package com.EcarteService.repository;

import com.EcarteService.model.RecurringPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecurringPaymentRepository extends JpaRepository<RecurringPayment, Long> {

    List<RecurringPayment> findByStartDateBeforeAndStatus(LocalDate date, String status);

    Optional<RecurringPayment> findBySenderUserIdAndServiceNameAndStatus(Long senderUserId, String serviceName, String status);
}
