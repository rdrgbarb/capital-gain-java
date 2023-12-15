package com.somebank.investments.entities.strategy;

import com.somebank.investments.entities.OperationResult;
import com.somebank.investments.entities.StockOperation;

public class BuyStrategy implements OperationStrategy {
    @Override
    public OperationResult calculate(OperationResult previousResult, StockOperation stockOperation) {
        int sharesQuantity = (previousResult != null)
                ? stockOperation.getQuantity() + previousResult.sharesQuantity()
                : stockOperation.getQuantity();

        Double weightedAverageCost = (previousResult != null)
                ? (previousResult.weightedAverageCost() * previousResult.sharesQuantity() + stockOperation.getUnitCost() * stockOperation.getQuantity()) / sharesQuantity
                : stockOperation.getUnitCost();

        return new OperationResult(
                weightedAverageCost,
                0d,
                0d,
                sharesQuantity
        );
    }
}