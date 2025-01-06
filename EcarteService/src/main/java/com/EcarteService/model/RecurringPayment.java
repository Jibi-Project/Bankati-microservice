package com.EcarteService.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class RecurringPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderUserId;
    private Double amount;
    private String serviceName;
    private String frequency; // e.g., "Mensuel", "Hebdomadaire", "Quotidien"
    private LocalDate startDate;// Changed to LocalDate
    private LocalDate endDate;

    private String status; // e.g., "Active", "Cancelled"

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }


    public String getStatus() {
        return status;
    }

    public Long getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(Long senderUserId) {
        this.senderUserId = senderUserId;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
