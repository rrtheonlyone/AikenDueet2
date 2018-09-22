package codeit.template.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TwoDinosaurs {

    @JsonProperty("number_of_types_of_food")
    private final int numFood;

    @JsonProperty("calories_for_each_type_for_raphael")
    private final int[] raphael;

    @JsonProperty("calories_for_each_type_for_leonardo")
    private final int[] leonardo;

    @JsonProperty("maximum_difference_for_calories")
    private int maxDiff;


    public TwoDinosaurs(@JsonProperty("number_of_types_of_food") int numFood, @JsonProperty("calories_for_each_type_for_raphael") int[] raphael, @JsonProperty("calories_for_each_type_for_leonardo") int[] leonardo, @JsonProperty("maximum_difference_for_calories") int maxDiff) {

        this.numFood = numFood;
        this.raphael = raphael;
        this.leonardo = leonardo;
        this.maxDiff = maxDiff;
    }


    public int getNumFood() {
        return numFood;
    }

    public int[] getRaphael() {
        return raphael;
    }

    public int[] getLeonardo() {
        return leonardo;
    }

    public int getMaxDiff() {
        return maxDiff;
    } 
}
