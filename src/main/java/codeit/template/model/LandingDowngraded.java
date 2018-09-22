package codeit.template.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class LandingDowngraded {

    private String PlaneId;
    private String Time;

    public LandingDowngraded(String planeId, String landingTime) {
        this.PlaneId = planeId;
        this.Time = landingTime;
    }

    @JsonProperty("PlaneId")
    public String getPlaneId() {
        return PlaneId;
    }

    @JsonProperty("Time")
    public String getLandingTime() {
        return Time;
    }
}