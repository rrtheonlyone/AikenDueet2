package codeit.template.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class FlightUpgraded implements Comparable<FlightUpgraded> {
        
        @JsonProperty("PlaneId")
        private String planeId;

        @JsonProperty("Time")
        private String planeTiming;

        private int landingTimestamp; // In seconds

        @JsonProperty("Distressed")
        private String isStristressed;

        private boolean isDistressed;

        // public FlightUpgraded(String planeId, String landingTime) {
        //     this(planeId, landingTime, false);
        // }

        public FlightUpgraded( @JsonProperty("PlaneId") String planeId, @JsonProperty("Time") String landingTime,  @JsonProperty("Distressed") String isStristressed) {
            this.planeId = planeId;
            this.isStristressed = isStristressed;
            this.planeTiming = landingTime;
            if (isStristressed == null || isStristressed.length() == 0) {
                isDistressed = false;
            } else {
                isDistressed = Boolean.parseBoolean(isStristressed);
            }


            // Convert to seconds
            int minutes = (landingTime.charAt(2) - '0') * 10 + (landingTime.charAt(3) - '0');
            int hours = (landingTime.charAt(0) - '0') * 10 + (landingTime.charAt(1) - '0');

            landingTimestamp = (hours * 60 + minutes) * 60;
        }

        // public FlightUpgraded(String planeId, int landingTimestamp, boolean isDistressed) {
        //     this.planeId = planeId;
        //     this.landingTimestamp = landingTimestamp;
        //     this.isDistressed = isDistressed;
        // }

        public String getPlaneId() {
            return planeId;
        }

        public String getPlaneTiming() {
            return planeTiming;
        }

        public int getLandingTimestamp() {
            return landingTimestamp;
        }

        public void setLandingTimestamp(int landingTimestamp) {
            this.landingTimestamp = landingTimestamp;
        }

        public boolean isDistressed() {
            return isDistressed;
        }

        @Override
        public int compareTo(FlightUpgraded flight) {
            if (this.landingTimestamp != flight.landingTimestamp) {
                return Integer.compare(this.landingTimestamp, flight.landingTimestamp);
            } else if (this.isDistressed != flight.isDistressed) {
                if (this.isDistressed) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                return this.planeId.compareTo(flight.planeId);
            }
        }

        @Override
        public String toString() {
            return "Flight{" +
                    "planeId='" + planeId + '\'' +
                    ", landingTimestamp=" + landingTimestamp +
                    ", isDistressed=" + isDistressed +
                    '}';
        }
    }