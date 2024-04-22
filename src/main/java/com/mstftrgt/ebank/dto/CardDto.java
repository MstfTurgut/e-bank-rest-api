package com.mstftrgt.ebank.dto;

import com.mstftrgt.ebank.model.Card;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    private String id;
    private String cardNumber;
    private Card.CardType cardType;
    private LocalDateTime expirationDate;
    private String CVV;
    private Card.CardStatus cardStatus;
}
