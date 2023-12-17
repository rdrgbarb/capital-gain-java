package com.somebank.investments.entities.strategy;

import com.somebank.investments.entities.OperationResult;
import com.somebank.investments.entities.StockMarketOperation;

public interface OperationStrategy {
    OperationResult calculate(OperationResult previousResult, StockMarketOperation stockMarketOperation);
}