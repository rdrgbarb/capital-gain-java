package com.somebank.investments.entities.strategy;

import com.somebank.investments.entities.OperationResult;
import com.somebank.investments.entities.StockMarketOperation;
import com.somebank.investments.entities.exceptions.InvalidPreviousResultException;

public class SellStrategy implements OperationStrategy {
    private static final Double THRESHOLD_SELLING_COST = 20000d;
    public static final double PAID_TAX_PERCENTAGE = 0.2;
    private double totalCost;
    private double unitCost;
    private double weightedAverageCost;
    private Integer quantity;
    private Double profit;
    private Double previousFinancialLoss;

    @Override
    public OperationResult calculate(OperationResult previousResult, StockMarketOperation stockMarketOperation) {
        if (invalidPreviousResult(previousResult,stockMarketOperation)) {
            throw new InvalidPreviousResultException();
        }
        this.unitCost = stockMarketOperation.unitCost();
        this.quantity = stockMarketOperation.quantity();
        this.totalCost = quantity * unitCost;
        this.weightedAverageCost = previousResult.weightedAverageCost();
        this.previousFinancialLoss = previousResult.financialLoss();
        this.profit = totalCost - weightedAverageCost * quantity;
        int remainingSharesQuantity = previousResult.sharesQuantity() - quantity;

        return new OperationResult(
                weightedAverageCost,
                calculateTax(),
                calculateFinancialLoss(),
                remainingSharesQuantity
        );
    }

    private boolean invalidPreviousResult(OperationResult previousResult, StockMarketOperation stockMarketOperation) {
        return previousResult==null || stockMarketOperation.quantity() > previousResult.sharesQuantity();
    }

    private Double calculateTax() {
        return totalCost > THRESHOLD_SELLING_COST ? remainingProfitOrZero()*PAID_TAX_PERCENTAGE : 0d;
    }

    private double remainingProfitOrZero() {
        return Math.max(this.profit - previousFinancialLoss,0d);
    }

    private Double calculateFinancialLoss() {
        if (weightedAverageCost > unitCost) {
            return previousFinancialLoss + quantity * weightedAverageCost - totalCost;
        }
        return remainingFinancialLossOrZero();
    }

    private double remainingFinancialLossOrZero() {
        return Math.max(previousFinancialLoss - this.profit, 0d);
    }
}