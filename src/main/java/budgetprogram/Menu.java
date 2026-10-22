package budgetprogram;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

public class Menu {
    private static final Logger LOGGER = Logger.getLogger(Menu.class.getName());
    private static final String PATH = "transactions.csv";

    private Repository<Transaction> repository = new Repository<>();
    private final FileStorage storage = new FileStorage();

    public void run() {
        try {
            repository = storage.readFile(PATH);
        } catch (FileStorageException e) {
            LOGGER.severe("Appen startade inte: " + e.getMessage());
            IO.println("Kunde inte lasa filen. Appen avslutas.");
            return;
        }
        LOGGER.info("Appen startad");

        boolean running = true;
        while (running) {
            printMenu();
            running = handleChoice(readChoice());
        }
        LOGGER.info("Appen avslutas");
    }

    private void printMenu() {
        IO.println("Budgethanteraren");
        IO.println("================");
        IO.println("1. Lägg till transaktion");
        IO.println("2. Visa alla transaktioner");
        IO.println("3. Visa saldo och sammanstallning per kategori");
        IO.println("4. Filtrera transaktioner");
        IO.println("5. Spara till fil");
        IO.println("6. Avsluta");
    }

    private String readChoice() {
        return IO.readln("Val: ");
    }

    private boolean handleChoice(String choice) {
        LOGGER.fine("Menyval: " + choice);
        switch (choice) {
            case "1" -> addTransaction();
            case "2" -> showAll();
            case "3" -> showSummary();
            case "4" -> filterTransactions();
            case "5" -> save();
            case "6" -> { return false; }
            default -> {
                LOGGER.warning("Felaktigt menyval: " + choice);
                IO.println("Felaktigt val, valj 1-6.");
            }
        }
        return true;
    }

    private void addTransaction() {
        try {
            LocalDate date = LocalDate.parse(IO.readln("Datum (AAAA-MM-DD): "));
            String category = IO.readln("Kategori: ");
            double amount = Double.parseDouble(IO.readln("Belopp: "));
            TransactionType type =
                    TransactionType.valueOf(IO.readln("Typ (INCOME/EXPENSE): ").toUpperCase());
            repository.add(new Transaction(date, category, amount, type));
            LOGGER.info("Transaktion tillagd");
            IO.println("Transaktionen lades till.");
        } catch (DateTimeParseException | IllegalArgumentException | InvalidTransactionException e) {
            LOGGER.warning("Felaktig inmatning vid ny transaktion: " + e.getMessage());
            IO.println("Felaktig inmatning, ingenting sparades.");
        }
    }

    private void showAll() {
        for (Transaction t : repository.getList()) {
            IO.println(t);
        }
    }

    private void showSummary() {
        BudgetCalculator calculator = new BudgetCalculator(repository.getList());
        IO.println("Saldo: " + calculator.getBalance());
        IO.println("Utgifter per kategori: " + calculator.getExpensesByCategory());
        IO.println("Inkomster per kategori: " + calculator.getIncomesByCategory());
    }

    private void filterTransactions() {
        String answer = IO.readln("Visa 1. utgifter eller 2. inkomster: ");
        TransactionType wanted = answer.equals("1") ? TransactionType.EXPENSE : TransactionType.INCOME;
        if (!answer.equals("1") && !answer.equals("2")) {
            LOGGER.warning("Felaktigt filterval: " + answer);
            IO.println("Felaktigt val.");
            return;
        }
        for (Transaction t : repository.findWhere(tx -> tx.type() == wanted)) {
            IO.println(t);
        }
    }

    private void save() {
        try {
            storage.saveFile(repository, PATH);
            IO.println("Sparat.");
        } catch (FileStorageException e) {
            LOGGER.severe("Sparandet misslyckades: " + e.getMessage());
            IO.println("Kunde inte spara filen.");
        }
    }
}
