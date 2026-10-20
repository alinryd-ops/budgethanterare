package budgetprogram;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileStorageTest {

    @Test
        void fileDoesNotExist() throws IOException {
        FileStorage storage = new FileStorage();
        Repository<Transaction> result = storage.readFile("non-existing.csv");
        assertEquals(0, result.size());
    }

    @Test
    void readFromCreatedFile(@TempDir Path tempDir) throws IOException {
        Repository<Transaction> repo = new Repository<>();
        repo.add(new Transaction(LocalDate.of(2026, 10, 15),
                "Lön", 500, TransactionType.INCOME));
        repo.add(new Transaction(LocalDate.of(2026, 10, 13),
                "Mat", 100, TransactionType.EXPENSE));

        String path = tempDir.resolve("transactions.csv").toString();
        FileStorage storage = new FileStorage();
        storage.saveFile(repo, path);

        Repository<Transaction> result = storage.readFile(path);
        assertEquals(2, result.size());

    }

    @Test
    void skipsBrokenLines(@TempDir Path tempDir) throws IOException {

        List<String> lines = List.of(
                "2026-10-15,Lön,500.0,INCOME",
                "det här är skräp",
                "2026-10-13,Mat,100.0,EXPENSE"
        );

        String path = tempDir.resolve("broken.csv").toString();
        Files.write(Paths.get(path), lines);
        FileStorage storage = new FileStorage();
        Repository<Transaction> result = storage.readFile(path);

        assertEquals(2, result.size());
    }

    @Test
    void skipsLineWithBadAmount(@TempDir Path tempDir) throws IOException {
        List<String> lines = List.of(
                "2026-10-15,Lön,500.0,INCOME",
                "2026-10-14,Lön,abc,INCOME",
                "2026-10-13,Mat,100.0,EXPENSE"
        );
        String path = tempDir.resolve("badamount.csv").toString();
        Files.write(Paths.get(path), lines);

        FileStorage storage = new FileStorage();
        Repository<Transaction> result = storage.readFile(path);
        assertEquals(2, result.size());
    }

    @Test
    void skipsLineWithBadType(@TempDir Path tempDir) throws IOException {
        List<String> lines = List.of(
                "2026-10-15,Lön,500.0,INCOME",
                "2026-10-14,Lön,500.0,BANAN",
                "2026-10-13,Mat,100.0,EXPENSE"
        );
        String path = tempDir.resolve("badtype.csv").toString();
        Files.write(Paths.get(path), lines);

        FileStorage storage = new FileStorage();
        Repository<Transaction> result = storage.readFile(path);
        assertEquals(2, result.size());
    }

    @Test
    void skipsLineWithTooFewFields(@TempDir Path tempDir) throws IOException {
        List<String> lines = List.of(
                "2026-10-15,Lön,500.0,INCOME",
                "2026-10-14,Lön",
                "2026-10-13,Mat,100.0,EXPENSE"
        );
        String path = tempDir.resolve("fewfields.csv").toString();
        Files.write(Paths.get(path), lines);

        FileStorage storage = new FileStorage();
        Repository<Transaction> result = storage.readFile(path);
        assertEquals(2, result.size());
    }

    @Test
    void readsFieldsInCorrectPlaces(@TempDir Path tempDir) throws IOException {
        List<String> lines = List.of(
                "2026-10-15,Lön,500.0,INCOME",
                "2026-10-13,Mat,100.0,EXPENSE"
        );
        String path = tempDir.resolve("sorting.csv").toString();
        Files.write(Paths.get(path), lines);
        FileStorage storage = new FileStorage();
        Repository<Transaction> result = storage.readFile(path);
        Transaction first = result.getList().get(0);
        Transaction second = result.getList().get(1);
        assertEquals("Lön", first.category());
        assertEquals(500.0, first.amount(), 0.001);
        assertEquals(TransactionType.INCOME, first.type());
        assertEquals(TransactionType.EXPENSE, second.type());
        assertEquals("Mat", second.category());
        assertEquals(100.0, second.amount(), 0.001);

    }

}
