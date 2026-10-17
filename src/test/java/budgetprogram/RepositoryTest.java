package budgetprogram;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RepositoryTest {

    @Test
    void newRepositoryIsEmpty(){
        Repository<Transaction> repo = new Repository<>();
        assertEquals(0, repo.size());
    }

    @Test
    void addingItemMakesSizeGrow () {
        Repository<Transaction> repo = new Repository<>();
        Transaction t = new Transaction (LocalDate.of(2026, 10, 14),
                "Mat3", 20, TransactionType.EXPENSE);
        repo.add(t);
        assertEquals(1, repo.size());

    }

    @Test
    void getListReturnsTheItemThatWasAdded() {
        Repository<Transaction> repo = new Repository<>();
        Transaction t = new Transaction(LocalDate.of(2026, 10, 14),
                "Mat", 20, TransactionType.EXPENSE);

        repo.add(t);

        assertEquals(t, repo.getList().get(0));
    }

    @Test
    void addingNullThrowsException() {
        Repository<Transaction> repo = new Repository<>();
        assertThrows(IllegalArgumentException.class, () -> repo.add(null));
    }
}
