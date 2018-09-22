import java.awt.MediaTracker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import codeit.template.model.Expense;

public class TallyExpenses {

    public static class Transaction {
        private String from;
        private String to;
        private double amount;

        public Transaction(String from, String to, double amount) {
            this.from = from;
            this.to = to;
            this.amount = amount;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public double getAmount() {
            return amount;
        }

        @Override
        public String toString() {
            return "Transaction{" +
                    "from='" + from + '\'' +
                    ", to='" + to + '\'' +
                    ", amount=" + amount +
                    '}';
        }
    }

    public static final double EPSILON = 1e-8;

    private String[] persons;
    private ArrayList<Expense> expenses;

    private double totalExpenses;
    private HashMap<String, Double> moneyPaidMap;

    public TallyExpenses(String[] persons, ArrayList<Expense> expenses) {
        this.persons = persons;
        this.expenses = expenses;

        totalExpenses = 0;
        moneyPaidMap = new HashMap<>();
    }

    public ArrayList<Transaction> solve() {
        for (Expense expense : expenses) {
            double totalAmount = expense.getAmount();
            String paidBy = expense.getPaidBy();
            String[] excluded = expense.getExcluded();
            int numPeople = persons.length - excluded.length;

            double amountPerPerson = totalAmount / numPeople;

            totalExpenses += amountPerPerson;
            helperAddMoneyPaid(paidBy, totalAmount);

            for (String excludedPerson : excluded) {
                helperAddMoneyPaid(excludedPerson, amountPerPerson);
            }
        }

        HashMap<String, Double> moneyToReceiveMap = new HashMap<>();
        for (Map.Entry<String, Double> entry : moneyPaidMap.entrySet()) {
            String name = entry.getKey();
            double moneyToReceive = entry.getValue() - totalExpenses;

            if (moneyToReceive > EPSILON) {
                moneyToReceiveMap.put(name, moneyToReceive);
            }
        }

        ArrayList<Transaction> transactions = new ArrayList<>();
        for (String sender : persons) {
            double moneyOwed = totalExpenses - helperGetMoneyPaid(sender);

            if (moneyOwed < EPSILON) {
                continue;
            }

            while (moneyOwed > EPSILON) {
                if (moneyToReceiveMap.size() == 0) {
                    break;
                }

                Map.Entry<String, Double> recipientEntry = moneyToReceiveMap.entrySet().iterator().next();
                String recipient = recipientEntry.getKey();
                double moneyToReceive = recipientEntry.getValue();

                double moneyToPay = Math.min(moneyToReceive, moneyOwed);

                moneyOwed -= moneyToPay;
                moneyToReceive -= moneyToPay;

                if (moneyToReceive < EPSILON) {
                    moneyToReceiveMap.remove(recipient);
                } else {
                    moneyToReceiveMap.put(recipient, moneyToReceive);
                }

                transactions.add(new Transaction(sender, recipient, Math.round(100 * moneyToPay) / 100.0));
            }
        }

        return transactions;
    }

    private void helperAddMoneyPaid(String name, double amount) {
        if (moneyPaidMap.containsKey(name)) {
            moneyPaidMap.put(name, moneyPaidMap.get(name) + amount);
        } else {
            moneyPaidMap.put(name, amount);
        }
    }

    private double helperGetMoneyPaid(String name) {
        if (moneyPaidMap.containsKey(name)) {
            return moneyPaidMap.get(name);
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        String[] persons = {"Alice", "Bob", "Claire", "David"};
        ArrayList<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(60, "Bob", new String[]{"Claire", "David"}));
        expenses.add(new Expense(100, "Claire"));
        expenses.add(new Expense(80, "David"));
        expenses.add(new Expense(40, "David"));

        TallyExpenses tallyExpenses = new TallyExpenses(persons, expenses);
        System.out.println(tallyExpenses.solve());
    }
}
