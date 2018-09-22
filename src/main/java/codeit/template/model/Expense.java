package codeit.template.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Expense {

    @JsonProperty("category")
    public String category;

    @JsonProperty("amount")
    public double amount;

    @JsonProperty("paidBy")
    private String paidBy;

    @JsonProperty("exclude")
    private String[] excluded;

    // public Expense(@JsonProperty("amount") double amount,  @JsonProperty("paidBy") String paidBy) {
    //     this(amount, paidBy, null);
    // }

    public Expense( @JsonProperty("amount") double amount,  @JsonProperty("paidBy") String paidBy,  @JsonProperty("exclude") String[] excluded) {
        this.amount = amount;
        this.paidBy = paidBy;

        if (excluded == null) {
            excluded = new String[0];
        }
        this.excluded = excluded;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public String[] getExcluded() {
        return excluded;
    }
}