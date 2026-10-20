package budgetprogram;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileStorageTest {

    @Test
        void fileDoesNotExist() {
        FileStorage storage = new FileStorage();
        Repository<Transaction> result = storage.readFile("non-existing.csv");
        assertEquals(0, result.size());
    }

    @Test
    void readFromCreatedFile(@TempDir Path tempDir) {
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

}
