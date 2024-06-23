package dev.adamag.tripmasterfront.model;

import java.util.List;

public class FlightResponse {
    private List<Flight> flights;

    public FlightResponse() {}

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
}
