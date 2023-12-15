package com.somebank.investments.entities.strategy;

import com.somebank.investments.entities.OperationResult;
import com.somebank.investments.entities.StockOperation;

public class SellStrategy implements OperationStrategy {
    //    private static final Double THRESHOLD_SELLING_COST = 20000d;
    @Override
    public OperationResult calculate(OperationResult previousResult, StockOperation stockOperation) {
        if (previousResult == null) {
            return new OperationResult(0d, 0d, 0d, 0);
        }

        double financialLoss = 0d;
        double weightedAverageCost = previousResult.weightedAverageCost();

        if (weightedAverageCost > stockOperation.getUnitCost()) {
            financialLoss = previousResult.financialLoss() + stockOperation.getQuantity() * weightedAverageCost - stockOperation.getQuantity() * stockOperation.getUnitCost();
        } else {
            double profit = stockOperation.getQuantity() * stockOperation.getUnitCost() - weightedAverageCost * stockOperation.getQuantity();
            profit = profit - previousResult.financialLoss();
            if (profit < 0) {
                financialLoss = profit * -1;
            }
        }

        int sharesQuantity = previousResult.sharesQuantity() - stockOperation.getQuantity();

        return new OperationResult(
                weightedAverageCost,
                0d,
                financialLoss,
                sharesQuantity
        );
    }
}