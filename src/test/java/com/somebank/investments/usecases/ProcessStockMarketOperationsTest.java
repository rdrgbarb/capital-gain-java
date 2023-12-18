package com.somebank.investments.usecases;

import com.somebank.investments.entities.PaidTax;
import com.somebank.investments.entities.StockMarketOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.somebank.investments.entities.OperationType.BUY;
import static com.somebank.investments.entities.OperationType.SELL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ProcessStockMarketOperationsTest {

    public static final BigDecimal ZERO_BIG_DECIMAL = BigDecimal.valueOf(0d);
    ProcessStockMarketOperations processStockMarketOperations;

    @BeforeEach
    void setUp() {
        this.processStockMarketOperations = new ProcessStockMarketOperations();
    }

    @Test
    void case01_Given_buy_sell_sell_Operations_WithTotalCostLessThan20k_ThenTaxResultEqualTo_zero_zero_zero() {
        // given
        List<StockMarketOperation> operations = new ArrayList<>(
                List.of(
                        new StockMarketOperation(BUY,10d,100),
                        new StockMarketOperation(SELL,15d,50),
                        new StockMarketOperation(SELL,15d,50)
                )
        );

        // when
        List<PaidTax> paidTaxes = this.processStockMarketOperations.execute(operations);

        // then
        List<PaidTax> expectedPaidTaxes = new ArrayList<>(
          List.of(
                  new PaidTax(ZERO_BIG_DECIMAL),new PaidTax(ZERO_BIG_DECIMAL),new PaidTax(ZERO_BIG_DECIMAL)
          )
        );
        assertThat(paidTaxes, equalTo(expectedPaidTaxes));
    }

    @Test
    void case02_Given_buy_sell_sell_Operations_WithProfitAtFirstSellAndLostAtSecondSell_ThenTaxResultEqualTo_zero_money_zero() {
        // given
        List<StockMarketOperation> operations = new ArrayList<>(
                List.of(
                        new StockMarketOperation(BUY,10d,10000),
                        new StockMarketOperation(SELL,20d,5000),
                        new StockMarketOperation(SELL,5d,5000)
                )
        );

        // when
        List<PaidTax> paidTaxes = this.processStockMarketOperations.execute(operations);

        // then
        List<PaidTax> expectedPaidTaxes = new ArrayList<>(
                List.of(
                        new PaidTax(ZERO_BIG_DECIMAL),new PaidTax(BigDecimal.valueOf(10000d)),new PaidTax(ZERO_BIG_DECIMAL)
                )
        );
        assertThat(paidTaxes, equalTo(expectedPaidTaxes));
    }

}
