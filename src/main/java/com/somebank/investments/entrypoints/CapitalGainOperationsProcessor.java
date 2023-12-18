package com.somebank.investments.entrypoints;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.somebank.investments.entities.OperationType;
import com.somebank.investments.entities.PaidTax;
import com.somebank.investments.entities.StockMarketOperation;
import com.somebank.investments.entrypoints.gson.OperationTypeAdapter;
import com.somebank.investments.entrypoints.gson.PaidTaxTypeAdapter;
import com.somebank.investments.usecases.ProcessStockMarketOperations;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CapitalGainOperationsProcessor {

    public String process(String operationInput) {
        return operationInput.lines()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(this::calculateTaxes)
                .collect(Collectors.joining("\n"));
    }

    private String calculateTaxes(String operationInputLine) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(OperationType.class,new OperationTypeAdapter())
                .registerTypeAdapter(PaidTax.class, new PaidTaxTypeAdapter())
                .create();
        StockMarketOperation[] operation = gson.fromJson(operationInputLine,StockMarketOperation[].class);
        ProcessStockMarketOperations processStockMarketOperations = new ProcessStockMarketOperations();
        List<PaidTax> taxes = processStockMarketOperations.execute(Arrays.stream(operation).toList());
        return gson.toJson(taxes);
    }
}