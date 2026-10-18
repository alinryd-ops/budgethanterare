package budgetprogram;

import java.util.ArrayList;
import java.util.List;

public class BudgetCalculator {


    private List<Transaction> transactions;

    public BudgetCalculator(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public double getBalance () {
        double sum = 0.0;

        for (Transaction transaction : transactions) {
            if (transaction.type() == TransactionType.INCOME) {
                sum = sum + transaction.amount();
            } else {
                sum = sum - transaction.amount();
            }
        }
        return sum;
    }

    public double getExpenses() {
        double sum = 0.0;
        for (Transaction transaction : transactions) {
            if (transaction.type() == TransactionType.EXPENSE) {
                sum = sum + transaction.amount();
            }
        }
        return sum;
    }

    public double getIncomes() {
        double sum = 0.0;
        for (Transaction transaction : transactions) {
            if (transaction.type() == TransactionType.INCOME) {
                sum = sum + transaction.amount();
            }
        }
        return sum;
    }

}
