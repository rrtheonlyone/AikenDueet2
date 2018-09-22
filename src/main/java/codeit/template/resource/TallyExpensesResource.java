package codeit.template.resource;

import codeit.template.solution.TallyExpensesSolution;
import codeit.template.model.Expense;
import codeit.template.model.Transaction;

import codeit.template.model.TallyExpenses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.math.BigInteger;

@RestController
public class TallyExpensesResource {

    @RequestMapping("/tally-expense-test")
    public String hello(){
        return  "Testing tally expenses! yay";
    }

   	@RequestMapping(value = "tally-expense",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, ArrayList<Transaction>> solve(@RequestBody TallyExpenses body) {
        String[] persons = body.getPersons();
        ArrayList<Expense> expenses = body.getExpenses();

    	TallyExpensesSolution tally = new TallyExpensesSolution(persons, expenses);
        ArrayList<Transaction> res = tally.solve();

        HashMap<String, ArrayList<Transaction>> output = new HashMap<>();
        output.put("transactions", res);

        return output;
    }
}
