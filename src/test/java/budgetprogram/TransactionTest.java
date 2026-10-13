package budgetprogram;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {


    @Test
    void transactionMinimumWith001IsAccepted() {
        //Created Transaction
        Transaction t = new Transaction(LocalDate.of(2026, 10, 14),
                "Candy", 0.01, TransactionType.EXPENSE);
        //Check if transaction is 0.01
        assertEquals(0.01, t.amount(), 0.001);
    }

    @Test
    void transaction0ThrowsInvalidTransaction() {
        //Created Transaction
        assertThrows(InvalidTransactionException.class, () -> {
            new Transaction(LocalDate.of(2026, 10, 14),
                    "Candy", 0, TransactionType.EXPENSE);
        });
    }


    @Test
    void transactionNegativeAmountThrowsInvalidTransaction(){
        assertThrows(InvalidTransactionException.class, () -> {
            new Transaction(LocalDate.of(2026, 10, 14),
                    "Candy", -2, TransactionType.EXPENSE);
        });
    }

}
