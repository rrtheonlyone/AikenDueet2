package codeit.template.model;

public class Pair implements Comparable<Pair>{
    public long head;
    public long tail;

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