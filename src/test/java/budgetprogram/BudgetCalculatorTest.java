package budgetprogram;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BudgetCalculatorTest {

    @Test
    void balanceOfEmptyListIsZero() {
        BudgetCalculator calc = new BudgetCalculator(new ArrayList<>());

        assertEquals(0, calc.getBalance(), 0.001);
    }

    @Test
    void balanceWithOneIncomeEqualsTheIncome() {
        List<Transaction> list = new ArrayList<>();
        list.add(new Transaction(LocalDate.of(2026, 10, 15),
                "Lön", 10000, TransactionType.INCOME));

        BudgetCalculator calc = new BudgetCalculator(list);

        assertEquals(10000.0, calc.getBalance(), 0.001);
    }

    @Test
    void balanceIsIncomeMinusExpenseTestNegativeBalance() {
        List<Transaction> list = new ArrayList<>();
        list.add(new Transaction(LocalDate.of(2026, 10, 15),
                "Lön", 1, TransactionType.INCOME));
        list.add(new Transaction(LocalDate.of(2026, 10, 14),
                "Hyra", 20, TransactionType.EXPENSE));

        BudgetCalculator calc = new BudgetCalculator(list);

        assertEquals(-19, calc.getBalance(), 0.001);
    }

    @Test
    void balanceIsIncomeMinusExpenseTestPositiveBalance() {
        List<Transaction> list = new ArrayList<>();
        list.add(new Transaction(LocalDate.of(2026, 10, 15),
                "Lön", 45, TransactionType.INCOME));
        list.add(new Transaction(LocalDate.of(2026, 10, 14),
                "Hyra", 20, TransactionType.EXPENSE));

        BudgetCalculator calc = new BudgetCalculator(list);

        assertEquals(25, calc.getBalance(), 0.001);
    }

    @Test
    void expensesOnlyCountsExpenses() {
        List<Transaction> list = new ArrayList<>();
        list.add(new Transaction(LocalDate.of(2026, 10, 15),
                "Lön", 100, TransactionType.INCOME));
        list.add(new Transaction(LocalDate.of(2026, 10, 14),
                "Hyra", 20, TransactionType.EXPENSE));
        list.add(new Transaction(LocalDate.of(2026, 10, 13),
                "Mat", 30, TransactionType.EXPENSE));

        BudgetCalculator calc = new BudgetCalculator(list);

        assertEquals(50, calc.getExpenses(), 0.001);
    }

    @Test
    void incomesOnlyCountsIncomes() {
        List<Transaction> list = new ArrayList<>();
        list.add(new Transaction(LocalDate.of(2026, 10, 15),
                "Lön", 100, TransactionType.INCOME));
        list.add(new Transaction(LocalDate.of(2026, 10, 14),
                "Bidrag", 87, TransactionType.INCOME));
        list.add(new Transaction(LocalDate.of(2026, 10, 13),
                "Hyra", 30, TransactionType.EXPENSE));

        BudgetCalculator calc = new BudgetCalculator(list);

        assertEquals(187, calc.getIncomes(), 0.001);
    }

    @Test
    void expensesByCategorySumsSameCategory() {
        List<Transaction> list = new ArrayList<>();
        list.add(new Transaction(LocalDate.of(2026, 10, 15),
                "Mat", 100, TransactionType.EXPENSE));
        list.add(new Transaction(LocalDate.of(2026, 10, 14),
                "Mat", 50, TransactionType.EXPENSE));

        BudgetCalculator calc = new BudgetCalculator(list);

        Map<String, Double> result = calc.getExpensesByCategory();

        assertEquals(150, result.get("Mat"), 0.001);
    }

    @Test
    void expensesByCategoryIgnoresIncomes() {
        List<Transaction> list = new ArrayList<>();
        list.add(new Transaction(LocalDate.of(2026, 10, 15),
                "Mat", 100, TransactionType.EXPENSE));
        list.add(new Transaction(LocalDate.of(2026, 10, 14),
                "Mat", 50, TransactionType.EXPENSE));
        list.add(new Transaction(LocalDate.of(2026, 10, 13),
                "Mat", 500, TransactionType.INCOME));

        BudgetCalculator calc = new BudgetCalculator(list);

        Map<String, Double> result = calc.getExpensesByCategory();

        assertEquals(150, result.get("Mat"), 0.001);
    }

}
