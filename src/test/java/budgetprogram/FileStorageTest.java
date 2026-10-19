package budgetprogram;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileStorageTest {

    @Test
        void fileDoesNotExist() {
        FileStorage storage = new FileStorage();
        Repository<Transaction> result = storage.readFile("non-existing.csv");
        assertEquals(0, result.size());
    }

}
