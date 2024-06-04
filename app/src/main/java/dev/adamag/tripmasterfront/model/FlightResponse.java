package dev.adamag.tripmasterfront.model;

import java.io.Serializable;
import java.util.List;

public class FlightResponse implements Serializable {
    private List<String> airlines;
    private List<String> urls;

    public FlightResponse() {}

    public FlightResponse(List<String> airlines, List<String> urls) {
        this.airlines = airlines;
        this.urls = urls;
    }

    public List<String> getAirlines() {
        return airlines;
    }

    public void setAirlines(List<String> airlines) {
        this.airlines = airlines;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
