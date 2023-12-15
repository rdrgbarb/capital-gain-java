package com.somebank.investments.entities;

import com.somebank.investments.entities.strategy.OperationStrategy;
import com.somebank.investments.entities.strategy.OperationStrategyFactory;
import lombok.Getter;

@Getter
public class StockOperation {
    public static final Double THRESHOLD_SELLING_COST = 20000d;
    private final OperationType operation;
    private final Double unitCost;
    private final Integer quantity;
    private final OperationStrategy operationStrategy;

    public StockOperation(OperationType operation, Double unitCost, Integer quantity) {
        this.operation = operation;
        this.unitCost = unitCost;
        this.quantity = quantity;
        this.operationStrategy = OperationStrategyFactory.createStrategy(operation);
    }

    public OperationResult calculate(OperationResult previousResult) {
        return operationStrategy.calculate(previousResult,this);
    }
}