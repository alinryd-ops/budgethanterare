package budgetprogram;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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


}
