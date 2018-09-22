import java.util.*;

public class CustomerHotelMinCamps{
    static class Pair implements Comparable<Pair>{
        long head;
        long tail;

        public Pair(long a, long b){
            this.head = a;
            this.tail = b;
        }

        public int compareTo (Pair other){
            if (this.head == other.head){
                return Long.compare(this.tail, other.tail);
            }
            return Long.compare(this.head, other.head);
        }
    }


    // This is your input, Rahul.
    // This stores a list of customers, represented by pairs.
    // Head of each pair is pos, tail of each pair is dist
    ArrayList<Pair> customers;

    public CustomerHotelMinCamps(ArrayList<Pair> input){
        this.customers = input;
    }

    public int solve(){
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
        CustomerHotelMinCamps next = new CustomerHotelMinCamps(cust);
        System.out.println(next.solve());
    }
}
