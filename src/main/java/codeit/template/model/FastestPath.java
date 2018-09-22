package codeit.template.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

public class FastestPath {

    @JsonProperty("data")
    private final String[] dataArray;

    @JsonProperty("sender")
    private final String source;

    @JsonProperty("recipient")
    private final String destination;

    public FastestPath(@JsonProperty("data") String[] dataArray, @JsonProperty("sender") String source, @JsonProperty("recipient") String destination) {

        this.dataArray = dataArray;
        this.source = source;
        this.destination = destination;
    }

    public String[] getData() {
        return dataArray;
    }

    public String getSrc() {
        return source;
    }

    public String getDest() {
        return destination;
    }
}
