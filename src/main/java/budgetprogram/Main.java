package budgetprogram;

public class Main {
    public static void main(String[] args) {
        LoggingSetup.init();
        new Menu().run();
    }
}
