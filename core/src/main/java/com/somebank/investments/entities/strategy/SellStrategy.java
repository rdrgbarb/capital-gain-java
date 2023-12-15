package com.somebank.investments.entities.strategy;

import com.somebank.investments.entities.OperationResult;
import com.somebank.investments.entities.StockOperation;

import static com.somebank.investments.entities.StockOperation.THRESHOLD_SELLING_COST;

public class SellStrategy implements OperationStrategy {
    @Override
    public OperationResult calculate(OperationResult previousResult, StockOperation stockOperation) {
        Double weightedAverageCost = 0d;
        double tax = 0d;
        double financialLoss = 0d;
        int sharesQuantity = 0;
        double profit;
        double totalCost = stockOperation.getUnitCost() * stockOperation.getQuantity();

        if (previousResult != null) {
            sharesQuantity = previousResult.sharesQuantity() - stockOperation.getQuantity();
            weightedAverageCost = previousResult.weightedAverageCost();
            if (totalCost < THRESHOLD_SELLING_COST) {
                tax = 0d;
            }
            if (weightedAverageCost > stockOperation.getUnitCost()) {
                financialLoss = previousResult.financialLoss() + stockOperation.getQuantity() * weightedAverageCost - stockOperation.getQuantity() * stockOperation.getUnitCost();
            } else {
                profit = stockOperation.getQuantity() * stockOperation.getUnitCost() - weightedAverageCost * stockOperation.getQuantity();
                profit = profit - previousResult.financialLoss();
                if (profit < 0) {
                    financialLoss = profit * -1;
                }
            }
        }
        return new OperationResult(
                weightedAverageCost,
                tax,
                financialLoss,
                sharesQuantity
        );
    }
}
