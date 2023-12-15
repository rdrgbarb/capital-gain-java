package com.somebank.investments.entities.strategy;

import com.somebank.investments.entities.OperationResult;
import com.somebank.investments.entities.StockOperation;

public class BuyStrategy implements OperationStrategy {
    @Override
    public OperationResult calculate(OperationResult previousResult, StockOperation stockOperation) {
        Double weightedAverageCost;
        double tax = 0d;
        double financialLoss = 0d;
        int sharesQuantity;
        if (previousResult != null) {
            sharesQuantity = stockOperation.getQuantity() + previousResult.sharesQuantity();
            weightedAverageCost = (previousResult.weightedAverageCost() * previousResult.sharesQuantity() + stockOperation.getUnitCost() * stockOperation.getQuantity()) / sharesQuantity;
        } else {
            sharesQuantity = stockOperation.getQuantity();
            weightedAverageCost = stockOperation.getUnitCost();
        }
        return new OperationResult(
                weightedAverageCost,
                tax,
                financialLoss,
                sharesQuantity
        );
    }
}
