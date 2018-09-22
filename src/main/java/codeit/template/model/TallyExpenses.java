package codeit.template.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import codeit.template.model.Expense;
import java.util.ArrayList;

public class TallyExpenses {

    @JsonProperty("name")
    private final String name;

    @JsonProperty("persons")
    private final String[] persons;

    @JsonProperty("expenses")
    private final ArrayList<Expense> expenses;

    public TallyExpenses(@JsonProperty("name") String name, @JsonProperty("persons") String[] persons, @JsonProperty("expenses") ArrayList<Expense> expenses) {

        this.name = name;
        this.persons = persons;
        this.expenses = expenses;
    }

    public String[] getPersons() {
        return persons;
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }
}
