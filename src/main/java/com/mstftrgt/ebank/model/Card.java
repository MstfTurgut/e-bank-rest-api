package com.mstftrgt.ebank.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "cards")
@Data
@NoArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "account_id")
    private Account account;

    private String cardNumber;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    private LocalDateTime expirationDate;

    private String CVV;

    private String PIN;

    private LocalDateTime creationDate;

    @Enumerated(EnumType.STRING)
    private CardStatus cardStatus;

    public Card(String cardNumber, CardType cardType, LocalDateTime expirationDate, String CVV, String PIN, LocalDateTime creationDate, CardStatus cardStatus) {
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.expirationDate = expirationDate;
        this.CVV = CVV;
        this.PIN = PIN;
        this.creationDate = creationDate;
        this.cardStatus = cardStatus;
    }

    public enum CardType {DEBIT, CREDIT}
    public enum CardStatus {ACTIVE, DISABLED, BLOCKED}
}

