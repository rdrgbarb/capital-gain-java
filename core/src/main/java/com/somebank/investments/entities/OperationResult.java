package com.somebank.investments.entities;

public record OperationResult(
        Double weightedAverageCost,
        Double tax,
        Double financialLoss,
        Integer sharesQuantity) {
}