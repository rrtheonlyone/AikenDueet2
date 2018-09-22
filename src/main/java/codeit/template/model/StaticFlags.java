package codeit.template.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StaticFlags {

    @JsonProperty("Runways")
    public String[] head;
    
    @JsonProperty("ReserveTime")
    public String tail;

    public StaticFlags(@JsonProperty("Runways") String[] a, @JsonProperty("ReserveTime") String b){
        this.head = a;
        this.tail = b;
    }
}