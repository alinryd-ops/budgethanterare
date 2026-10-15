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


        boolean hasLetter = false;

        for (int i = 0; i < category.length(); i++) {
            if (Character.isLetter(category.charAt(i))) {
                hasLetter = true;
            }
        }

        if (!hasLetter) {
            throw new InvalidTransactionException("Category must have a letter");
        }

        if (date == null) {
            throw new InvalidTransactionException("Date cannot be null");
        }

        if (type == null) {
            throw new InvalidTransactionException("Type cannot be null");
        }

    }

}


