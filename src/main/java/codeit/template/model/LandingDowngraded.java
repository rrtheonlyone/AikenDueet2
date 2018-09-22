package codeit.template.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class LandingDowngraded {

    private String planeId;
    private String landingTime;

    public LandingDowngraded(String planeId, String landingTime) {
        this.planeId = planeId;
        this.landingTime = landingTime;
    }

    public String getPlaneId() {
        return planeId;
    }

    public String getLandingTime() {
        return landingTime;
    }
}