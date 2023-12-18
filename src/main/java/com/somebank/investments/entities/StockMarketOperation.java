package com.somebank.investments.entities;

import com.google.gson.annotations.SerializedName;
import com.somebank.investments.entities.strategy.OperationStrategyFactory;

public record StockMarketOperation(OperationType operation, @SerializedName("unit-cost") Double unitCost,
                                   Integer quantity) {
    public OperationResult calculate(OperationResult previousResult) {
        return OperationStrategyFactory.createStrategy(operation).calculate(previousResult, this);
    }
}