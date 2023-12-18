package com.somebank.investments.usecases;

import com.somebank.investments.entities.OperationResult;
import com.somebank.investments.entities.PaidTax;
import com.somebank.investments.entities.StockMarketOperation;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ProcessStockMarketOperations {
    public List<PaidTax> execute(List<StockMarketOperation> operations) {
        // Using AtomicReference because of this compiler error:
        // "Variable used in lambda expression should be final or effectively final"
        AtomicReference<OperationResult> operationResult = new AtomicReference<>(new OperationResult(0d, 0d, 0d, 0));
        return operations.stream()
                .map(
                        operation -> {
                            OperationResult previousResult = operationResult.get();
                            operationResult.set(operation.calculate(previousResult));
                            BigDecimal tax = BigDecimal.valueOf(operationResult.get().tax());
                            return new PaidTax(tax);
                        }
                )
                .toList();
    }
}
