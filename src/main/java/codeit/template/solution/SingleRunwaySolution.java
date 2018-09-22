package codeit.template.solution;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

import codeit.template.model.Flight;
import codeit.template.model.Landing;

public class SingleRunwaySolution {

    private static class Runway implements Comparable<Runway> {

        public static final int FREE = -1;

        private String runwayId;
        private int freeTime;

        public Runway(String runwayId) {
            this.runwayId = runwayId;
            freeTime = FREE;
        }

        public String getRunwayId() {
            return runwayId;
        }

        public int getFreeTime() {
            return freeTime;
        }

        public void setFreeTime(int freeTime) {
            this.freeTime = freeTime;
        }

        @Override
        public int compareTo(Runway runway) {
            if (this.freeTime != runway.freeTime) {
                return Integer.compare(this.freeTime, runway.freeTime);
            } else {
                return this.runwayId.compareTo(runway.runwayId);
            }
        }

        @Override
        public String toString() {
            return "Runway{" +
                    "runwayId='" + runwayId + '\'' +
                    ", freeTime=" + freeTime +
                    '}';
        }
    }

    private Flight[] flights;
    private String[] runways;
    private PriorityQueue<Runway> availableRunways;
    private PriorityQueue<Runway> occupiedRunways;
    private ArrayDeque<Flight> landingQueue;
    private int reserveTime;

    public SingleRunwaySolution(Flight[] flights, int reserveTime) {
        this.flights = flights;
        this.runways = new String[]{"A"};
        this.reserveTime = reserveTime;

        Arrays.sort(flights);

        availableRunways = new PriorityQueue<>();
        occupiedRunways = new PriorityQueue<>();
        landingQueue = new ArrayDeque<>();
    }

    public ArrayList<Landing> solve() {
        for (String runway : runways) {
            availableRunways.add(new Runway(runway));
        }

        PriorityQueue<Integer> eventQueue = new PriorityQueue<>();
        for (Flight flight : flights) {
            landingQueue.add(flight);
            eventQueue.add(flight.getLandingTimestamp());
        }

        ArrayList<Landing> output = new ArrayList<>();
        while (!eventQueue.isEmpty()) {
            int currentTimestamp = eventQueue.poll();

            // Free the runways
            while (!occupiedRunways.isEmpty() && occupiedRunways.peek().getFreeTime() <= currentTimestamp) {
                Runway freedRunway = occupiedRunways.poll();
                assert freedRunway != null;
                freedRunway.setFreeTime(Runway.FREE);

                availableRunways.add(freedRunway);
            }

            // Land the planes if available
            while (!availableRunways.isEmpty()) {
                if (landingQueue.isEmpty() || landingQueue.peek().getLandingTimestamp() > currentTimestamp) {
                    break;
                }

                Runway landingRunway = availableRunways.poll();
                Flight landingFlight = landingQueue.poll();

                assert landingFlight != null;
                assert landingRunway != null;

                output.add(new Landing(landingFlight.getPlaneId(), currentTimestamp, landingRunway.getRunwayId()));

                landingRunway.setFreeTime(currentTimestamp + reserveTime);
                occupiedRunways.add(landingRunway);
                eventQueue.add(currentTimestamp + reserveTime);
            }
        }

        return output;
    }

    public static void main(String[] args) {
        Flight[] flights = new Flight[]{
                new Flight("TR123", "0200"),
                new Flight("SQ255", "0210"),
                new Flight("TH544", "0155"),
                new Flight("BA123", "0212"),
                new Flight("VA521", "0230")
        };
        int reserveTime = 600;

        SingleRunwaySolution singleRunway = new SingleRunwaySolution(flights, reserveTime);
        System.out.println(singleRunway.solve());
    }

}
