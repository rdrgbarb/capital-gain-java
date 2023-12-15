package com.somebank.investments.entities;

import lombok.Builder;
import lombok.Data;

import static com.somebank.investments.entities.OperationType.BUY;
import static com.somebank.investments.entities.OperationType.SELL;

@Data
@Builder
public class StockOperation {
    private static final Double THRESHOLD_SELLING_COST = 20000d;
    private OperationType operation;
    private Double unitCost;
    private Integer quantity;

    public OperationResult calculate(OperationResult previousResult) {
        Double weightedAverageCost = 0d;
        double tax = 0d;
        double financialLoss = 0d;
        int sharesQuantity = 0;
        double profit;
        double totalCost = unitCost * quantity;
        if (BUY.equals(operation)) {
            if (previousResult != null) {
                sharesQuantity = quantity + previousResult.sharesQuantity();
                weightedAverageCost = (previousResult.weightedAverageCost() * previousResult.sharesQuantity() + unitCost * quantity) / sharesQuantity;
            } else {
                sharesQuantity = quantity;
                weightedAverageCost = unitCost;
            }
        } else if (SELL.equals(operation) && (previousResult != null)) {
            sharesQuantity = previousResult.sharesQuantity() - quantity;
            weightedAverageCost = previousResult.weightedAverageCost();
            if (totalCost < THRESHOLD_SELLING_COST) {
                tax = 0d;
            }
            if (weightedAverageCost > unitCost) {
                financialLoss = previousResult.financialLoss() + quantity * weightedAverageCost - quantity * unitCost;
            } else {
                profit = quantity * unitCost - weightedAverageCost * quantity;
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