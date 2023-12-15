package com.somebank.investments.entities.strategy;

import com.somebank.investments.entities.OperationResult;
import com.somebank.investments.entities.StockOperation;

public interface OperationStrategy {
    OperationResult calculate(OperationResult previousResult, StockOperation stockOperation);
}