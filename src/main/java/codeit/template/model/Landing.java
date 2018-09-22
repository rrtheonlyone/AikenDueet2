package codeit.template.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Landing {

    private String PlaneId;
    private String Time;
    private String Runway;

    public Landing(String planeId, int landingTimestamp, String runway) {
        this.PlaneId = planeId;
        this.Runway = runway;

        int minutes = (int) Math.round((landingTimestamp % (60 * 60)) / 60.0);
        int hours = landingTimestamp / (60 * 60);

        Time = String.format("%02d%02d", hours, minutes);
    }

    @JsonProperty("PlaneId")
    public String getPlaneId() {
        return PlaneId;
    }

    @JsonProperty("Time")
    public String getLandingTime() {
        return Time;
    }

    @JsonProperty("Runway")
    public String getRunway() {
        return Runway;
    }

    public void setRunway(String runway) {
        this.Runway = Runway;
    }

    @Override
    public String toString() {
        return "Landing{" +
                "planeId='" + PlaneId + '\'' +
                ", landingTime='" + Time + '\'' +
                ", runway='" + Runway + '\'' +
                '}';
    }
}