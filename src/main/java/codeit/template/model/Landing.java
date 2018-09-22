package codeit.template.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Landing {

    private String planeId;
    private String landingTime;
    private String runway;

    public Landing(String planeId, int landingTimestamp, String runway) {
        this.planeId = planeId;
        this.runway = runway;

        int minutes = (int) Math.round((landingTimestamp % (60 * 60)) / 60.0);
        int hours = landingTimestamp / (60 * 60);

        landingTime = String.format("%02d%02d", hours, minutes);
    }

    public String getPlaneId() {
        return planeId;
    }

    public String getLandingTime() {
        return landingTime;
    }

    public String getRunway() {
        return runway;
    }

    public void setRunway(String runway) {
        this.runway = runway;
    }

    @Override
    public String toString() {
        return "Landing{" +
                "planeId='" + planeId + '\'' +
                ", landingTime='" + landingTime + '\'' +
                ", runway='" + runway + '\'' +
                '}';
    }
}