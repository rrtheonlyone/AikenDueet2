package codeit.template.solution;

import java.util.*;

public class CustomerHotelMinDistSolution{
    ArrayList<Long> inputData;
    public CustomerHotelMinDistSolution(ArrayList<Long> inputData){
        this.inputData = inputData;
        Collections.sort(this.inputData);
    }

    public long solve(){
        long minimum = Long.MAX_VALUE;
        for (int i = 0; i < this.inputData.size() - 1; ++i){
            long curr = this.inputData.get(i + 1) - this.inputData.get(i);
            if (curr < minimum){
                minimum = curr;
            }
        }
        return minimum;
    }

    public static void main(String[] args){
        // ???
    }
}

