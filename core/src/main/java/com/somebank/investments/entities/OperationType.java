package com.somebank.investments.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperationType {

    BUY("buy"),
    SELL("sell");

    private final String value;

    // Enum Pattern Matching to translate from string to enum
    public static OperationType fromString(String value) {
        return switch (value) {
            case "buy" -> BUY;
            case "sell" -> SELL;
            default -> throw new IllegalArgumentException("Type not supported: " + value);
        };
    }
}
