package com.somebank.investments.entrypoints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class CapitalGainOperationsProcessorTest {

    CapitalGainOperationsProcessor processor;
    @BeforeEach
    void setUp() {
        processor = new CapitalGainOperationsProcessor();
    }

    @Test
    void case01_buySellSell_TotalCostLessThan20k() {
        // given
        String operationInput = "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 100},{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 50},{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 50}]";

        // when
        String operationOutput = processor.process(operationInput);

        // then
        String expectedOutput = "[{\"tax\":0.00},{\"tax\":0.00},{\"tax\":0.00}]";
        assertThat(operationOutput, equalTo(expectedOutput));
    }

    @Test
    void case02_buySellSell_TotalCostMoreThan20k_ProfitableOperation() {
        // given
        String operationInput = "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":5.00, \"quantity\": 5000}]";

        // when
        String operationOutput = processor.process(operationInput);

        // then
        String expectedOutput = "[{\"tax\":0.00},{\"tax\":10000.00},{\"tax\":0.00}]";
        assertThat(operationOutput, equalTo(expectedOutput));
    }

    @Test
    void twoLinesOfOperation_Case01PlusCase02() {
        // given
        String operationInput = "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 100},{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 50},{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 50}]\n" +
                "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":5.00, \"quantity\": 5000}]";

        // when
        String operationOutput = processor.process(operationInput);

        // then
        String expectedOutput = "[{\"tax\":0.00},{\"tax\":0.00},{\"tax\":0.00}]\n" +
                "[{\"tax\":0.00},{\"tax\":10000.00},{\"tax\":0.00}]";
        assertThat(operationOutput, equalTo(expectedOutput));
    }

    @Test
    void case03_buySellSell_TotalCostMoreThan20k_OperationWithLossAtFirstAndProfitForLast() {
        // given
        String operationInput = "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"sell\", \"unit-cost\":5.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 3000}]";

        // when
        String operationOutput = processor.process(operationInput);

        // then
        String expectedOutput = "[{\"tax\":0.00},{\"tax\":0.00},{\"tax\":1000.00}]";
        assertThat(operationOutput, equalTo(expectedOutput));
    }

    @Test
    void case04_buyBuySell_TotalCostMoreThan20k_NonProfitableNeitherLossOperation() {
        // given
        String operationInput = "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"buy\", \"unit-cost\":25.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 10000}]";

        // when
        String operationOutput = processor.process(operationInput);

        // then
        String expectedOutput = "[{\"tax\":0.00},{\"tax\":0.00},{\"tax\":0.00}]";
        assertThat(operationOutput, equalTo(expectedOutput));
    }

    @Test
    void case05_buyBuySellSell_TotalCostMoreThan20k_ProfitableOperation() {
        // given
        String operationInput = "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"buy\", \"unit-cost\":25.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 10000},{\"operation\":\"sell\", \"unit-cost\":25.00, \"quantity\": 5000}]";

        // when
        String operationOutput = processor.process(operationInput);

        // then
        String expectedOutput = "[{\"tax\":0.00},{\"tax\":0.00},{\"tax\":0.00},{\"tax\":10000.00}]";
        assertThat(operationOutput, equalTo(expectedOutput));
    }

    @Test
    void case06_buy4sells_TotalCostMoreThan20k_LossAtFirstOperationAndProfitForTheNextOperations() {
        // given
        String operationInput = "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"sell\", \"unit-cost\":2.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000},{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000},{\"operation\":\"sell\", \"unit-cost\":25.00, \"quantity\": 1000}]";

        // when
        String operationOutput = processor.process(operationInput);

        // then
        String expectedOutput = "[{\"tax\":0.00},{\"tax\":0.00},{\"tax\":0.00},{\"tax\":0.00},{\"tax\":3000.00}]";
        assertThat(operationOutput, equalTo(expectedOutput));
    }

    @Test
    void case07_twiceBuy4Sells_TotalCostMoreThan20k_LossAtFirstOperationAndProfitForTheNextOperations() {
        // given
        String operationInput = "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000},{\"operation\":\"sell\", \"unit-cost\":2.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000},{\"operation\":\"sell\", \"unit-cost\":20.00, \"quantity\": 2000},{\"operation\":\"sell\", \"unit-cost\":25.00, \"quantity\": 1000},{\"operation\":\"buy\", \"unit-cost\":20.00, \"quantity\": 10000},{\"operation\":\"sell\", \"unit-cost\":15.00, \"quantity\": 5000},{\"operation\":\"sell\", \"unit-cost\":30.00, \"quantity\": 4350},{\"operation\":\"sell\", \"unit-cost\":30.00, \"quantity\": 650}]";

        // when
        String operationOutput = processor.process(operationInput);

        // then
        String expectedOutput = "[{\"tax\":0.00},{\"tax\":0.00},{\"tax\":0.00},{\"tax\":0.00},{\"tax\":3000.00},{\"tax\":0.00},{\"tax\":0.00},{\"tax\":3700.00},{\"tax\":0.00}]";
        assertThat(operationOutput, equalTo(expectedOutput));
    }

    @Test
    void case08_BuySellBuySell_TotalCostMoreThan20k_ProfitableOperations() {
        // given
        String operationInput = "[{\"operation\":\"buy\", \"unit-cost\":10.00, \"quantity\": 10000}, {\"operation\":\"sell\", \"unit-cost\":50, \"quantity\":10000},{\"operation\":\"buy\", \"unit-cost\":20.00, \"quantity\": 10000}, {\"operation\":\"sell\", \"unit-cost\":50, \"quantity\":10000}]";

        // when
        String operationOutput = processor.process(operationInput);

        // then
        String expectedOutput = "[{\"tax\":0.00},{\"tax\":80000.00},{\"tax\":0.00},{\"tax\":60000.00}]";
        assertThat(operationOutput, equalTo(expectedOutput));
    }

}