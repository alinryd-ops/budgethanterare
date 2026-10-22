package budgetprogram;

import java.io.IOException;
import java.util.logging.*;

public class LoggingSetup {
    public static void init() {
        Logger root = Logger.getLogger("");
        for (Handler h : root.getHandlers()) {
            root.removeHandler(h);
        }
        try {
            FileHandler file = new FileHandler("budgetprogram.log", true);
            file.setFormatter(new SimpleFormatter());
            file.setLevel(Level.ALL);
            root.addHandler(file);
            root.setLevel(Level.INFO);
            Logger.getLogger("budgetprogram").setLevel(Level.ALL);
        } catch (IOException e) {
            // Ingen loggfil, och användaren ska inte se loggar ändå.
        }
    }
}
