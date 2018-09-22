package codeit.template.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Pair implements Comparable<Pair>{

    @JsonProperty("pos")
    public long head;
    
    @JsonProperty("distance")
    public long tail;

    public Pair(@JsonProperty("pos") long a, @JsonProperty("distance") long b){
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