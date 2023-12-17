package com.somebank.investments.entities;

import com.somebank.investments.entities.exceptions.InvalidPreviousResultException;
import org.junit.jupiter.api.Test;

import static com.somebank.investments.entities.OperationType.BUY;
import static com.somebank.investments.entities.OperationType.SELL;
import static com.somebank.investments.entities.exceptions.InvalidPreviousResultException.ERROR_MESSAGE_INVALID_PREVIOUS_RESULT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StockMarketOperationTest {

    @Test
    void executeSimpleBuyOperation_WithoutPreviousOperationResult() {
        // given
        StockMarketOperation operation = new StockMarketOperation(BUY,20d,10000);

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
        OperationResult previousResult = new OperationResult(
                10d,
                0d,
                0d,
                10000
        );
        StockMarketOperation operation = new StockMarketOperation(
                BUY,
                25d,
                5000
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
        OperationResult previousResult = new OperationResult(
                10d,
                0d,
                0d,
                100
        );
        StockMarketOperation operation = new StockMarketOperation(
                SELL,
                15d,
                50
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
        OperationResult previousResult = new OperationResult(
                10d,
                0d,
                0d,
                10000
        );
        StockMarketOperation operation = new StockMarketOperation(
                SELL,
                2d,
                5000
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
        OperationResult previousResult = new OperationResult(
                10d,
                0d,
                25000d,
                5000
        );
        StockMarketOperation operation = new StockMarketOperation(
                SELL,
                20d,
                300
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
        OperationResult previousResult = new OperationResult(
                10d,
                0d,
                3000d,
                5000
        );
        StockMarketOperation operation = new StockMarketOperation(
                SELL,
                20d,
                300
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
        OperationResult previousResult = new OperationResult(
                10d,
                0d,
                50000d,
                10000
        );
        StockMarketOperation operation = new StockMarketOperation(
                SELL,
                2d,
                5000
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

    @Test
    void executeProfitableSellOperationThatCostsMoreThan20k() {
        // given
        OperationResult previousResult = new OperationResult(
                10d,
                0d,
                0d,
                10000
        );
        StockMarketOperation operation = new StockMarketOperation(
                SELL,
                20d,
                5000
        );

        // when
        OperationResult actualResult = operation.calculate(previousResult);

        // then
        OperationResult expectedResult = new OperationResult(
                10d,
                10000d,
                0d,
                5000
        );
        assertThat(actualResult, equalTo(expectedResult));
    }

    @Test
    void executeProfitableSellOperationThatCostsMoreThan20k_WithPreviousOperationInLoss_AsThereWasProfitLeft_SoTaxesShouldBeCharged() {
        // given
        OperationResult previousResult = new OperationResult(
                10d,
                0d,
                25000d,
                5000
        );
        StockMarketOperation operation = new StockMarketOperation(
                SELL,
                20d,
                3000
        );

        // when
        OperationResult actualResult = operation.calculate(previousResult);

        // then
        OperationResult expectedResult = new OperationResult(
                10d,
                1000d,
                0d,
                2000
        );
        assertThat(actualResult, equalTo(expectedResult));
    }

    @Test
    void executeProfitableSellOperationThatCostsMoreThan20k_WithPreviousOperationInLoss_AsThereWasLossLeft_SoTaxesShouldNotBeCharged_AndLossShouldBeCarriedToTheNextOperation() {
        // given
        OperationResult previousResult = new OperationResult(
                10d,
                0d,
                40000d,
                5000
        );
        StockMarketOperation operation = new StockMarketOperation(
                SELL,
                20d,
                2000
        );

        // when
        OperationResult actualResult = operation.calculate(previousResult);

        // then
        OperationResult expectedResult = new OperationResult(
                10d,
                0d,
                20000d,
                3000
        );
        assertThat(actualResult, equalTo(expectedResult));
    }

    @Test
    void executeProfitableSellOperationThatCostsMoreThan20k_WithPreviousOperationInLoss_WithoutLossOrProfit_TaxesShouldNotBeCharged_AndLossShouldNotBeCarriedToTheNextOperation() {
        // given
        OperationResult previousResult = new OperationResult(
                10d,
                0d,
                20000d,
                3000
        );
        StockMarketOperation operation = new StockMarketOperation(
                SELL,
                20d,
                2000
        );

        // when
        OperationResult actualResult = operation.calculate(previousResult);

        // then
        OperationResult expectedResult = new OperationResult(
                10d,
                0d,
                0d,
                1000
        );
        assertThat(actualResult, equalTo(expectedResult));
    }

    @Test
    void executeSellOperationAtALossThatCostsMoreThan20k_ShouldNotChargeTaxes() {
        // given
        OperationResult previousResult = new OperationResult(
                10d,
                0d,
                0d,
                10000
        );
        StockMarketOperation operation = new StockMarketOperation(
                SELL,
                8d,
                5000
        );

        // when
        OperationResult actualResult = operation.calculate(previousResult);

        // then
        OperationResult expectedResult = new OperationResult(
                10d,
                0d,
                10000d,
                5000
        );
        assertThat(actualResult, equalTo(expectedResult));
    }

    @Test
    void executeSellOperationAtALossThatCostsMoreThan20k_WithPreviousOperationInLoss_ShouldNotChargeTaxes() {
        // given
        OperationResult previousResult = new OperationResult(
                10d,
                0d,
                10000d,
                5000
        );
        StockMarketOperation operation = new StockMarketOperation(
                SELL,
                6d,
                4500
        );

        // when
        OperationResult actualResult = operation.calculate(previousResult);

        // then
        OperationResult expectedResult = new OperationResult(
                10d,
                0d,
                28000d,
                500
        );
        assertThat(actualResult, equalTo(expectedResult));
    }

    @Test
    void executeSellOperationWithNoLossOrProfitThatCostsMoreThan20k() {
        // given
        OperationResult previousResult = new OperationResult(
                15d,
                0d,
                0d,
                15000
        );
        StockMarketOperation operation = new StockMarketOperation(
                SELL,
                15d,
                10000
        );

        // when
        OperationResult actualResult = operation.calculate(previousResult);

        // then
        OperationResult expectedResult = new OperationResult(
                15d,
                0d,
                0d,
                5000
        );
        assertThat(actualResult, equalTo(expectedResult));
    }

    @Test
    void executeSellOperationWithoutPreviousOperation_ShouldThrowException() {
        // given
        StockMarketOperation operation = new StockMarketOperation(
                SELL,
                15d,
                10000
        );

        // when and then
        Exception thrown = assertThrows(InvalidPreviousResultException.class, () -> operation.calculate(null));
        assertThat(thrown.getMessage(), containsString(ERROR_MESSAGE_INVALID_PREVIOUS_RESULT));
    }

    @Test
    void executeSellOperationWithZeroedPreviousOperation() {
        // given
        OperationResult previousResult = new OperationResult(
                0d,
                0d,
                0d,
                0
        );
        StockMarketOperation operation = new StockMarketOperation(
                SELL,
                15d,
                10000
        );

        // when and then
        Exception thrown = assertThrows(InvalidPreviousResultException.class, () -> operation.calculate(previousResult));
        assertThat(thrown.getMessage(), containsString(ERROR_MESSAGE_INVALID_PREVIOUS_RESULT));
    }

}