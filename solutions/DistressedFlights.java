import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeSet;

public class DistressedFlights {


    public static class Flight implements Comparable<Flight> {

        private String planeId;
        private int landingTimestamp; // In seconds
        private boolean isDistressed;

        public Flight(String planeId, String landingTime) {
            this(planeId, landingTime, false);
        }

        public Flight(String planeId, String landingTime, boolean isDistressed) {
            this.planeId = planeId;
            this.isDistressed = isDistressed;

            // Convert to seconds
            int minutes = (landingTime.charAt(2) - '0') * 10 + (landingTime.charAt(3) - '0');
            int hours = (landingTime.charAt(0) - '0') * 10 + (landingTime.charAt(1) - '0');

            landingTimestamp = (hours * 60 + minutes) * 60;
        }

        public Flight(String planeId, int landingTimestamp, boolean isDistressed) {
            this.planeId = planeId;
            this.landingTimestamp = landingTimestamp;
            this.isDistressed = isDistressed;
        }

        public String getPlaneId() {
            return planeId;
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
        public int compareTo(Flight flight) {
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

    public static class Landing {

        private String planeId;
        private String landingTime;
        private String runway;

        public Landing(String planeId, int landingTimestamp, String runway) {
            this.planeId = planeId;
            this.runway = runway;

            int minutes = (int)Math.round((landingTimestamp % (60 * 60)) / 60.0);
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

        @Override
        public String toString() {
            return "Landing{" +
                    "planeId='" + planeId + '\'' +
                    ", landingTime='" + landingTime + '\'' +
                    ", runway='" + runway + '\'' +
                    '}';
        }
    }

    private static class Runway implements Comparable<Runway> {
        public static final int FREE = -1;

        private String runwayId;
        private int freeTime;
        private Flight occupyingFlight;

        public Runway(String runwayId) {
            this.runwayId = runwayId;
            freeTime = FREE;
            occupyingFlight = null;
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

        public Flight getOccupyingFlight() {
            return occupyingFlight;
        }

        public void setOccupyingFlight(Flight occupyingFlight) {
            this.occupyingFlight = occupyingFlight;
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

    private static class Event {
        private enum Type { WAITING_QUEUE, LANDED, LEAVE_RUNWAY };

        private Type type;
        private Flight flight;
        private int timestamp;
        private Runway runway;

        private Event(Type type, Flight flight, int timestamp, Runway runway) {
            this.type = type;
            this.flight = flight;
            this.timestamp = timestamp;
            this.runway = runway;
        }

        public static Event createWaitingEvent(Flight flight, int timestamp) {
            return new Event(Type.WAITING_QUEUE, flight, timestamp, null);
        }

        public static Event createLandedEvent(Flight flight, Runway runway, int timestamp) {
            return new Event(Type.LANDED, flight, timestamp, runway);
        }

        public static Event createLeaveRunwayEvent(Flight flight, Runway runway, int timestamp) {
            return new Event(Type.LEAVE_RUNWAY, flight, timestamp, runway);
        }

        public Type getType() {
            return type;
        }

        public Flight getFlight() {
            return flight;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public Runway getRunway() {
            return runway;
        }

        @Override
        public String toString() {
            return "Event{" +
                    "type=" + type +
                    ", flight=" + flight +
                    ", timestamp=" + timestamp +
                    ", runway=" + runway +
                    '}';
        }
    }

    private Flight[] flights;
    private String[] runways;
    private HashMap<String, Runway> runwayMap;
    private int reserveTime;

    public DistressedFlights(Flight[] flights, String[] runways, int reserveTime) {
        this.flights = flights;
        this.runways = runways;
        this.reserveTime = reserveTime;

        runwayMap = new HashMap<>();
        for (String runwayId : runways) {
            runwayMap.put(runwayId, new Runway(runwayId));
        }
    }

    public ArrayList<Landing> solve() {
        ArrayList<Flight> distressedFlights = new ArrayList<>();
        ArrayList<Flight> normalFlights = new ArrayList<>();

        for (Flight flight : flights) {
            if (flight.isDistressed()) {
                distressedFlights.add(flight);
            } else {
                normalFlights.add(flight);
            }
        }

        // Process the distressed flights first. Knowing their exact landing time will make things easier.
        // No need to worry about distressed flights waiting.
        ArrayList<Flight> updatedDistressedFlights = simulateDistressedFlights(distressedFlights);

        Stack<Event> eventHistory = new Stack<>(); // History of events.
        PriorityQueue<Integer> timestampQueue = new PriorityQueue<>(); // Queue of timestamps of events.
        PriorityQueue<Flight> normalFlightQueue = new PriorityQueue<>(normalFlights); // Queue of normal flights
        ArrayDeque<Flight> distressQueue = new ArrayDeque<>(updatedDistressedFlights); // Queue of distress flights
        ArrayDeque<Flight> waitingQueue = new ArrayDeque<>(); // Queue of flights waiting to land.
        // We may need to remove some particular runways, hence a TreeSet.
        TreeSet<Runway> availableRunways = new TreeSet<>(runwayMap.values()); // TreeSet of available runways
        TreeSet<Runway> occupiedRunways = new TreeSet<>(); // Ditto. TreeSet of occupied runways.
        // We may need to remove some landings from the end, hence a Deque.
        ArrayDeque<Landing> landingQueue = new ArrayDeque<>(); // Deque of plane landings.

        // Initialize timestamp queue
        normalFlights.forEach(flight -> timestampQueue.add(flight.getLandingTimestamp()));
        updatedDistressedFlights.forEach(flight -> timestampQueue.add(flight.getLandingTimestamp()));

        while (!timestampQueue.isEmpty()) {
            int currentTimestamp = timestampQueue.poll();

            // Free runways
            while (!occupiedRunways.isEmpty() && occupiedRunways.first().getFreeTime() <= currentTimestamp) {
                Runway freedRunway = occupiedRunways.pollFirst();
                assert freedRunway != null;
                Flight leavingFlight = freedRunway.getOccupyingFlight();

                freedRunway.setOccupyingFlight(null);
                freedRunway.setFreeTime(Runway.FREE);

                availableRunways.add(freedRunway);

                eventHistory.push(Event.createLeaveRunwayEvent(leavingFlight, freedRunway, currentTimestamp));
            }

            // Check distress flights
            boolean rewindRequired = false;
            while (!distressQueue.isEmpty() && distressQueue.peek().getLandingTimestamp() <= currentTimestamp) {
                if (availableRunways.isEmpty()) {
                    rewindRequired = true;
                    break;
                }

                Flight landingFlight = distressQueue.poll();
                Runway landingRunway = availableRunways.pollFirst();

                assert landingRunway != null;
                landingRunway.setFreeTime(currentTimestamp + reserveTime);
                landingRunway.setOccupyingFlight(landingFlight);

                occupiedRunways.add(landingRunway);

                assert landingFlight != null;
                landingQueue.add(new Landing(landingFlight.getPlaneId(), currentTimestamp,
                        landingRunway.getRunwayId()));

                timestampQueue.add(currentTimestamp + reserveTime);

                eventHistory.add(Event.createWaitingEvent(landingFlight, currentTimestamp));
                eventHistory.add(Event.createLandedEvent(landingFlight, landingRunway, currentTimestamp));
            }

            if (rewindRequired) {
                // Rewind to the last normal flight landing.
                boolean rewindComplete = false;
                while (!rewindComplete) {
                    Event lastEvent = eventHistory.pop();

                    // Rewind the event.
                    switch (lastEvent.getType()) {
                        case LANDED: {
                            // Take it out of the runway and put it back into the waiting queue
                            Runway landedRunway = lastEvent.getRunway();
                            Flight landedFlight = lastEvent.getFlight();

                            occupiedRunways.remove(landedRunway);
                            landedRunway.setOccupyingFlight(null);
                            landedRunway.setFreeTime(Runway.FREE);

                            availableRunways.add(landedRunway);

                            if (!landedFlight.isDistressed()) {
                                // This is the flight we're looking for.
                                landedFlight.setLandingTimestamp(currentTimestamp);
                                timestampQueue.add(currentTimestamp);
                                rewindComplete = true;
                            } else {
                                timestampQueue.add(lastEvent.getTimestamp());
                            }

                            waitingQueue.addFirst(landedFlight);
                            landingQueue.pollLast();

                            break;
                        }
                        case LEAVE_RUNWAY: {
                            // Put it back in the runway.
                            Runway leavingRunway = lastEvent.getRunway();
                            Flight leavingFlight = lastEvent.getFlight();

                            availableRunways.remove(leavingRunway);
                            leavingRunway.setFreeTime(lastEvent.getTimestamp());
                            leavingRunway.setOccupyingFlight(leavingFlight);

                            occupiedRunways.add(leavingRunway);

                            timestampQueue.add(lastEvent.getTimestamp());

                            break;
                        }
                        case WAITING_QUEUE: {
                            // Get it out of the waiting queue.
                            Flight waitingFlight = lastEvent.getFlight();
                            assert waitingFlight == waitingQueue.peekLast();

                            waitingQueue.pollLast();

                            if (waitingFlight.isDistressed()) {
                                distressQueue.addFirst(waitingFlight);
                            } else {
                                normalFlightQueue.add(waitingFlight);
                            }

                            timestampQueue.add(lastEvent.getTimestamp());

                            break;
                        }
                    }
                }

                continue;
            }

            // Push normal flights into waiting list
            while (!normalFlightQueue.isEmpty() && normalFlightQueue.peek().getLandingTimestamp() <= currentTimestamp) {
                Flight waitingFlight = normalFlightQueue.poll();
                waitingQueue.add(waitingFlight);

                eventHistory.add(Event.createWaitingEvent(waitingFlight, currentTimestamp));
            }

            // Land flights on the runways
            while (!availableRunways.isEmpty() && !waitingQueue.isEmpty()
                    && waitingQueue.peekFirst().getLandingTimestamp() <= currentTimestamp) {
                Flight landingFlight = waitingQueue.pollFirst();
                Runway landingRunway = availableRunways.pollFirst();

                assert landingRunway != null;
                landingRunway.setOccupyingFlight(landingFlight);
                landingRunway.setFreeTime(currentTimestamp + reserveTime);

                assert landingFlight != null;
                landingQueue.add(new Landing(landingFlight.getPlaneId(), currentTimestamp,
                        landingRunway.getRunwayId()));

                occupiedRunways.add(landingRunway);

                timestampQueue.add(currentTimestamp + reserveTime);

                eventHistory.add(Event.createLandedEvent(landingFlight, landingRunway, currentTimestamp));
            }
        }

        return new ArrayList<>(landingQueue);
    }

    private ArrayList<Flight> simulateDistressedFlights(ArrayList<Flight> distressedFlights) {
        PriorityQueue<Flight> flightQueue = new PriorityQueue<>(distressedFlights);
        PriorityQueue<Integer> timestampQueue = new PriorityQueue<>();
        PriorityQueue<Runway> availableRunways = new PriorityQueue<>();
        PriorityQueue<Runway> occupiedRunways = new PriorityQueue<>();
        Queue<Flight> waitingQueue = new ArrayDeque<>();

        // Initialization

        // Insert expected landing times into Event Queue
        for (Flight flight : distressedFlights) {
            timestampQueue.add(flight.getLandingTimestamp());
        }

        // Initialize all runways as available
        for (String runwayId : runways) {
            availableRunways.add(new Runway(runwayId));
        }

        ArrayList<Flight> newDistressedFlightTimings = new ArrayList<>();

        while (!timestampQueue.isEmpty()) {
            int currentTimestamp = timestampQueue.poll();

            // Free up runways
            while (!occupiedRunways.isEmpty() && occupiedRunways.peek().getFreeTime() <= currentTimestamp) {
                Runway freedRunway = occupiedRunways.poll();
                assert freedRunway != null;
                freedRunway.setFreeTime(Runway.FREE);
                freedRunway.setOccupyingFlight(null);
                availableRunways.add(freedRunway);
            }

            // Push flights into waiting list
            while (!flightQueue.isEmpty() && flightQueue.peek().getLandingTimestamp() <= currentTimestamp) {
                waitingQueue.add(flightQueue.poll());
            }

            // Poll flights from the waiting list
            while (!availableRunways.isEmpty() && !waitingQueue.isEmpty()) {
                Runway landingRunway = availableRunways.poll();
                Flight landingFlight = waitingQueue.poll();

                landingRunway.setFreeTime(currentTimestamp + reserveTime);
                landingRunway.setOccupyingFlight(landingFlight);

                assert landingFlight != null;
                newDistressedFlightTimings.add(new Flight(landingFlight.planeId, currentTimestamp, true));

                occupiedRunways.add(landingRunway);
                timestampQueue.add(currentTimestamp + reserveTime);
            }
        }

        return newDistressedFlightTimings;
    }

    public static void main(String[] args) {

        Flight[] flights = new Flight[]{
                new Flight("TR123", "0200", true),
                new Flight("SQ255", "0200", true),
                new Flight("TH544", "0155"),
                new Flight("BA123", "0200", true),
                new Flight("VA521", "0230")
        };

        String[] runways = new String[]{"A", "B", "C"};
        int reserveTime = 600;

        DistressedFlights multipleRunways = new DistressedFlights(flights, runways, reserveTime);
        System.out.println(multipleRunways.solve());

    }
}
