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
        Double financialLoss = 0d;
        int sharesQuantity = 0;
        double totalCost = unitCost*quantity;
        if(BUY.equals(operation)) {
            if(previousResult != null) {
                sharesQuantity = quantity + previousResult.sharesQuantity();
                weightedAverageCost = (previousResult.weightedAverageCost() * previousResult.sharesQuantity() + unitCost * quantity) / sharesQuantity;
            } else {
                sharesQuantity = quantity ;
                weightedAverageCost = unitCost;
            }
        } else if (SELL.equals(operation)) {
            if(previousResult != null) {
                sharesQuantity = previousResult.sharesQuantity() - quantity;
                weightedAverageCost = previousResult.weightedAverageCost();
                if(totalCost < THRESHOLD_SELLING_COST) {
                    tax = 0d;
                }
                if(weightedAverageCost > unitCost) {
                    financialLoss = quantity*weightedAverageCost - quantity*unitCost;
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