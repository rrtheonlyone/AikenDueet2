package codeit.template.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Map;

import codeit.template.model.Flight;

public class Runway {
    @JsonProperty("Flights")
    private final Flight[] flights;

    @JsonProperty("Static")
    private final Map<String, String> staticStuff;

    public Runway(@JsonProperty("Flights") Flight[] flights, @JsonProperty("Static") Map<String, String> staticStuff) {

        this.flights = flights;
        this.staticStuff = staticStuff;
    }

    public Flight[] getFlights() {
        return flights;
    }

    public Map<String, String> getStaticStuff() {
        return staticStuff;
    }
}
