package codeit.template.solution;

import codeit.template.model.Pair;

import java.util.*;

public class CustomerHotelMinCampSolution {

    // This is your input, Rahul.
    // This stores a list of customers, represented by pairs.
    // Head of each pair is pos, tail of each pair is dist
    ArrayList<Pair> customers;

    public CustomerHotelMinCampSolution(ArrayList<Pair> input){
        this.customers = input;
    }

    public int solve(){

        for (Pair i : customers) {
            System.out.println(i.head + "   " + i.tail);
        }


        ArrayList<Pair> distRange = new ArrayList<>();
        for (Pair i : customers){
            long start = i.head - i.tail;
            long end = i.head + i.tail;
            Pair range = new Pair(start, end);
            distRange.add(range);
        }

        Collections.sort(distRange);

        int currCust = 0;
        int counter = 0;
        long currlow = Long.MIN_VALUE;
        while (currCust < distRange.size()){
            Pair customer = distRange.get(currCust);
            if (customer.head > currlow){
                counter += 1;
                currlow = customer.tail;
                System.out.println(counter);
            }
            currCust += 1;
        }

        return counter;
    }

    public static void main(String[] args){
        Scanner s = new Scanner(System.in);
        ArrayList<Pair> cust = new ArrayList<>();
        for (int i = 0; i < 4; ++i){
            int pos = s.nextInt();
            int dis = s.nextInt();
            cust.add(new Pair(pos, dis));
        }
        CustomerHotelMinCampSolution next = new CustomerHotelMinCampSolution(cust);
        System.out.println(next.solve());
    }
}
