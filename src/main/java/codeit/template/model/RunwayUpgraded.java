package codeit.template.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

import codeit.template.model.FlightUpgraded;
import codeit.template.model.StaticFlags;

public class RunwayUpgraded {

    @JsonProperty("Flights")
    private final FlightUpgraded[] flights;

    @JsonProperty("Static")
    private final StaticFlags staticStuff;

    public RunwayUpgraded(@JsonProperty("Flights") FlightUpgraded[] flights, @JsonProperty("Static") StaticFlags staticStuff) {

        this.flights = flights;
        this.staticStuff = staticStuff;
    }

    public FlightUpgraded[] getFlights() {
        return flights;
    }

    public StaticFlags getStaticStuff() {
        return staticStuff;
    }
}
