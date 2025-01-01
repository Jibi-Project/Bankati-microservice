package com.EcarteService.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Entity
public class ECarte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroCarte;

    @Column(nullable = false)
    private String nomClient;

    @Column(nullable = false)
    private LocalDate dateExpiration;

    @Column(nullable = false)
    private String cvv;

    @Column(unique = true, nullable = false)
    private String emailUtilisateur;


    private Double balance;

    public Double getBalance(){return balance;}
    public void setBalance(double balance){this.balance=balance;}

    public String getCvv() {
        return cvv;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public Long getId() {
        return id;
    }

    public String getNomClient() {
        return nomClient;
    }

    public String getNumeroCarte() {
        return numeroCarte;
    }

    public String getEmailUtilisateur() {
        return emailUtilisateur;
    }

    public void setEmailUtilisateur(String emailUtilisateur) {
        this.emailUtilisateur = emailUtilisateur;
    }
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public void setNumeroCarte(String numeroCarte) {
        this.numeroCarte = numeroCarte;
    }

}
