package budgetprogram;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

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

    @Test
    void listOnlyGrows1StepWhenNewTransactionIsAdded() {
        Repository<Transaction> repo = new Repository<>();
        Transaction t1 = new Transaction(LocalDate.of(2026, 10, 14),
                "Hyra", 20, TransactionType.EXPENSE);
        Transaction t2 = new Transaction(LocalDate.of(2026, 10, 15),
                "Lön", 45, TransactionType.INCOME);

        repo.add(t1);
        repo.add(t2);

        assertEquals(2, repo.size());
        assertEquals(t1, repo.getList().get(0));
        assertEquals(t2, repo.getList().get(1));
    }

    @Test
    void findWhereReturnsOnlyMatching() {
        Repository<Transaction> repo = new Repository<>();
        repo.add(new Transaction(LocalDate.of(2026, 10, 15),
                "Mat", 100, TransactionType.EXPENSE));
        repo.add(new Transaction(LocalDate.of(2026, 10, 14),
                "Lön", 500, TransactionType.INCOME));
        repo.add(new Transaction(LocalDate.of(2026, 10, 13),
                "Hyra", 800, TransactionType.EXPENSE));

        ArrayList<Transaction> result =
                repo.findWhere(t -> t.type() == TransactionType.EXPENSE);

        assertEquals(2, result.size());
    }

    @Test
    void findWhereCanFilterOnDateRange() {
        Repository<Transaction> repo = new Repository<>();
        repo.add(new Transaction(LocalDate.of(2026, 10, 12),
                "Mat", 100, TransactionType.EXPENSE));
        repo.add(new Transaction(LocalDate.of(2026, 10, 14),
                "Hyra", 800, TransactionType.EXPENSE));
        repo.add(new Transaction(LocalDate.of(2026, 10, 16),
                "Lön", 500, TransactionType.INCOME));

        LocalDate from = LocalDate.of(2026, 10, 13);
        LocalDate to = LocalDate.of(2026, 10, 15);

        ArrayList<Transaction> result = repo.findWhere(
                t -> t.date().isAfter(from) && t.date().isBefore(to));

        assertEquals(1, result.size());
        assertEquals("Hyra", result.get(0).category());
    }

    @Test
    void sortedByDateGivesListInDateOrder() {
        Repository<Transaction> repo = new Repository<>();
        repo.add(new Transaction(LocalDate.of(2026, 10, 15),
                "Lön", 500, TransactionType.INCOME));
        repo.add(new Transaction(LocalDate.of(2026, 10, 13),
                "Mat", 100, TransactionType.EXPENSE));
        repo.add(new Transaction(LocalDate.of(2026, 10, 14),
                "Hyra", 800, TransactionType.EXPENSE));

        ArrayList<Transaction> result =
                repo.sortedBy(Comparator.comparing(Transaction::date));

        assertEquals("Mat", result.get(0).category());
        assertEquals("Hyra", result.get(1).category());
        assertEquals("Lön", result.get(2).category());
    }



}
