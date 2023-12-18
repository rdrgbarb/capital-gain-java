package com.somebank.investments.entities.strategy;

import com.somebank.investments.entities.OperationType;

public class OperationStrategyFactory {
    public static OperationStrategy createStrategy(OperationType operationType) {
        return switch (operationType) {
            case BUY -> new BuyStrategy();
            case SELL -> new SellStrategy();
        };
    }

    private OperationStrategyFactory() {
        throw new IllegalStateException("Utility class");
    }
}