package budgetprogram;

import java.time.LocalDate;

public record Transaction(LocalDate date,
                          String category,
                          double amount,
                          TransactionType type){

    public Transaction {
        if (amount <= 0) {
                throw new InvalidTransactionException("Amount must be more than 0");

        }

        if (category == null || category.isBlank()) {
            throw new InvalidTransactionException("Category cannot be empty");
        }

    }

}

