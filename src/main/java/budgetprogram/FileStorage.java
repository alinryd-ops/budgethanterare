package budgetprogram;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {

    void saveFile(Repository<Transaction> repository, String path) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Transaction t : repository.getList()) {
            String line = t.date() + "," + t.category() + "," + t.amount() + "," + t.type();
            lines.add(line);
        }

        Files.write(Paths.get(path), lines);

    }


    Repository<Transaction> readFile(String path) throws IOException {
        if (Files.notExists(Paths.get(path))) {
            return new Repository<>();

        }
        List<String> lines = Files.readAllLines(Paths.get(path));

        return new Repository<>();
    }
}
