# Code Challenge: Capital Gain 

## How it works?
### Input
This program receives lists, one per line, of stock market operations in JSON format through the standard input (stdin). Each operation in this list contains the following fields:

| Name      | Meaning                                                        |
|-----------|----------------------------------------------------------------|
| operation | If the operation is a buy or sell operation.                   |
| unit-cost | Unit price of the stock in a currency with two decimal places. |
| quantity  | Quantity of traded stocks.                                     |

Example of input:
```
[{"operation":"buy", "unit-cost":10.00, "quantity": 10000},{"operation":"sell", "unit-cost":20.00, "quantity": 5000}]
[{"operation":"buy", "unit-cost":20.00, "quantity": 10000},{"operation":"sell", "unit-cost":10.00, "quantity": 5000}]
```

The operations will be in the order they occurred, meaning the second operation in the list happened after the first, and so on.

Each line is an independent simulation; the program does not retain the state obtained in one line for the others.

The last line of the input will be an empty line.

### Output
For each input line, the program returns a list containing the tax paid for each received operation. The elements of this list are encoded in JSON format, and the output is returned through standard output (stdout). The return is composed of the following field:

| Name | Meaning                                  |
|------|------------------------------------------|
| tax  | The amount of tax paid in one operation. |

The list returned by the program has the same size as the list of processed operations in the input.

For example, if three operations (buy, buy, sell) were processed, the program's return is a list with three values representing the tax paid in each operation.

## Capital Gain Rules

The program handles two types of operations (buy and sell), and it follows the following rules:

1. **Tax Percentage:**
    - The tax percentage paid is 20% on the profit obtained in the operation. The tax is paid when there is a sell operation whose price is higher than the weighted average purchase price.

2. **Calculation of Weighted Average Price:**
    - To determine if the operation resulted in profit or loss, the weighted average price is calculated. When there is a stock purchase, the weighted average price is recalculated using the formula:
      ```markdown
      new-weighted-average = ((current-stock-quantity * current-weighted-average) + (stock-quantity * purchase-value)) / (current-stock-quantity + purchased-stock-quantity)
      ```
        - Example: If you bought 10 shares for $20.00, sold 5, then bought another 5 for $10.00, the weighted average is ((5 x 20.00) + (5 x 10.00)) / (5 + 5) = $15.00.

3. **Loss and Deduction:**
    - Past losses are used to deduct multiple future profits until all losses are deducted.
    - Losses occur when shares are sold for a value lower than the weighted average purchase price. No tax is paid in this case, and the program subtracts the loss from subsequent profits before calculating the tax.

4. **Tax Limit:**
    - No tax is paid if the total value of the operation (unit cost of the share x quantity) is less than or equal to $20,000.00. The total value of the operation (not the profit obtained) is used to determine whether the tax should be paid or not. Any remaining loss is deducted from subsequent profits.

5. **No Tax on Purchase Operations:**
    - No tax is paid on purchase operations.

6. **Assumption about Stock Sales:**
    - It is assumed that no operation will sell more shares than are currently held.
