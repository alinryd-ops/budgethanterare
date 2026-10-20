package budgetprogram;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
        Repository<Transaction> repo = new Repository<>();



        for (String line : lines) {
            String[] parts = line.split(",");

            try {
                LocalDate date = LocalDate.parse(parts[0]);
                String category = parts[1];
                double amount = Double.parseDouble(parts[2]);
                TransactionType type = TransactionType.valueOf(parts[3]);
                Transaction t = new Transaction(date, category, amount, type);
                repo.add(t);
            } catch (DateTimeParseException | IllegalArgumentException |
                     ArrayIndexOutOfBoundsException e ) {}
        }
        return repo;
    }
}
