package com.somebank.investments.entities;

import org.junit.jupiter.api.Test;

import static com.somebank.investments.entities.OperationType.BUY;
import static com.somebank.investments.entities.OperationType.SELL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class StockOperationTest {

    @Test
    void executeSimpleBuyOperation_WithoutPreviousOperationResult() {
        // given
        StockOperation operation = StockOperation
                .builder()
                .operation(BUY)
                .quantity(10000)
                .unitCost(20d)
                .build();

        // when
        OperationResult actualResult = operation.calculate(null);

        // then
        OperationResult expectedResult = new OperationResult(
                20d,
                0d,
                0d,
                10000
        );
        assertThat(actualResult, equalTo(expectedResult));
    }

    @Test
    void executeSimpleBuyOperation() {
        // given
        StockOperation operation = StockOperation
                .builder()
                .operation(BUY)
                .quantity(5000)
                .unitCost(25d)
                .build();

        OperationResult previousResult = new OperationResult(
                10d,
                0d,
                0d,
                10000
        );

        // when
        OperationResult actualResult = operation.calculate(previousResult);

        // then
        OperationResult expectedResult = new OperationResult(
                15d,
                0d,
                0d,
                15000
        );
        assertThat(actualResult, equalTo(expectedResult));
    }

    @Test
    void executeProfitableSellOperationThatCostsLessThan20k() {
        // given
        StockOperation operation = StockOperation
                .builder()
                .operation(SELL)
                .unitCost(15d)
                .quantity(50)
                .build();

        OperationResult previousResult = new OperationResult(
                10d,
                0d,
                0d,
                100
        );

        // when
        OperationResult actualResult = operation.calculate(previousResult);

        // then
        OperationResult expectedResult = new OperationResult(
                10d,
                0d,
                0d,
                50
        );
        assertThat(actualResult, equalTo(expectedResult));
    }

    @Test
    void executeSellOperationAtALossThatCostsLessThan20k() {
        // given
        StockOperation operation = StockOperation
                .builder()
                .operation(SELL)
                .unitCost(2d)
                .quantity(5000)
                .build();

        OperationResult previousResult = new OperationResult(
                10d,
                0d,
                0d,
                10000
        );

        // when
        OperationResult actualResult = operation.calculate(previousResult);

        // then
        OperationResult expectedResult = new OperationResult(
                10d,
                0d,
                40000d,
                5000
        );
        assertThat(actualResult, equalTo(expectedResult));
    }

    @Test
    void executeProfitableSellOperationThatCostsLessThan20k_WithPreviousOperationInLoss() {
        // given
        StockOperation operation = StockOperation
                .builder()
                .operation(SELL)
                .unitCost(20d)
                .quantity(300)
                .build();

        OperationResult previousResult = new OperationResult(
                10d,
                0d,
                25000d,
                5000
        );

        // when
        OperationResult actualResult = operation.calculate(previousResult);

        // then
        OperationResult expectedResult = new OperationResult(
                10d,
                0d,
                22000d,
                4700
        );
        assertThat(actualResult, equalTo(expectedResult));
    }

    @Test
    void executeProfitableSellOperationThatCostsLessThan20k_WithPreviousOperationInLoss_ZeroingTheFinalLoss() {
        // given
        StockOperation operation = StockOperation
                .builder()
                .operation(SELL)
                .unitCost(20d)
                .quantity(300)
                .build();

        OperationResult previousResult = new OperationResult(
                10d,
                0d,
                3000d,
                5000
        );

        // when
        OperationResult actualResult = operation.calculate(previousResult);

        // then
        OperationResult expectedResult = new OperationResult(
                10d,
                0d,
                0d,
                4700
        );
        assertThat(actualResult, equalTo(expectedResult));
    }

    @Test
    void executeSellOperationAtALossThatCostsLessThan20k_WithPreviousOperationInLoss() {
        // given
        StockOperation operation = StockOperation
                .builder()
                .operation(SELL)
                .unitCost(2d)
                .quantity(5000)
                .build();

        OperationResult previousResult = new OperationResult(
                10d,
                0d,
                50000d,
                10000
        );

        // when
        OperationResult actualResult = operation.calculate(previousResult);

        // then
        OperationResult expectedResult = new OperationResult(
                10d,
                0d,
                90000d,
                5000
        );
        assertThat(actualResult, equalTo(expectedResult));
    }
}