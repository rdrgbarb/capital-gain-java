package com.somebank.investments.entities.strategy;

import com.somebank.investments.entities.OperationResult;
import com.somebank.investments.entities.StockMarketOperation;

public class BuyStrategy implements OperationStrategy {
    @Override
    public OperationResult calculate(OperationResult previousResult, StockMarketOperation stockMarketOperation) {
        int sharesQuantity = (previousResult != null)
                ? stockMarketOperation.quantity() + previousResult.sharesQuantity()
                : stockMarketOperation.quantity();

        Double weightedAverageCost = (previousResult != null)
                ? (previousResult.weightedAverageCost() * previousResult.sharesQuantity() + stockMarketOperation.unitCost() * stockMarketOperation.quantity()) / sharesQuantity
                : stockMarketOperation.unitCost();

        return new OperationResult(
                weightedAverageCost,
                0d,
                0d,
                sharesQuantity
        );
    }
}