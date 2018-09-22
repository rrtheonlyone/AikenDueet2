package codeit.template.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Flight implements Comparable<Flight> {

    @JsonProperty("PlaneId")
    private String planeId;

    @JsonProperty("Time")
    private String planeTiming;

    private int landingTimestamp; // In seconds

    public Flight(@JsonProperty("PlaneId") String planeId, @JsonProperty("Time") String landingTime) {
        this.planeId = planeId;

        // Convert to seconds
        int minutes = (landingTime.charAt(2) - '0') * 10 + (landingTime.charAt(3) - '0');
        int hours = (landingTime.charAt(0) - '0') * 10 + (landingTime.charAt(1) - '0');

        landingTimestamp = (hours * 60 + minutes) * 60;
    }

    public String getPlaneId() {
        return planeId;
    }

    public int getLandingTimestamp() {
        return landingTimestamp;
    }

    @Override
    public int compareTo(Flight flight) {
        if (this.landingTimestamp != flight.landingTimestamp) {
            return Integer.compare(this.landingTimestamp, flight.landingTimestamp);
        } else {
            return this.planeId.compareTo(flight.planeId);
        }
    }

    @Override
    public String toString() {
        return "Flight{" +
                "planeId='" + planeId + '\'' +
                ", landingTimestamp=" + landingTimestamp +
                '}';
    }
}