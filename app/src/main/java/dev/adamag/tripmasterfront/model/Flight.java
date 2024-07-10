package dev.adamag.tripmasterfront.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Flight extends BoundaryObject {

    public Flight() {
        super();
    }

    public Flight(String link, String airline, String outboundDeparture, String outboundArrival,
                  String returnDeparture, String returnArrival, String origin, String destination,
                  String price, int adults, int children, String departureDate, String returnDate) {
        super(
                new ObjectIdBoundary("YourSuperApp", link), // Use link as the ID
                "Flight",
                "Flight to " + destination,
                new LocationBoundary(0.0, 0.0),
                true,
                new Date().toString(),
                new CreatedByBoundary(new CreatedByBoundary.UserIdBoundary("YourSuperApp", "user@example.com")),
                new HashMap<String, Object>()
        );

        this.getObjectDetails().put("link", link);
        this.getObjectDetails().put("airline", airline);
        this.getObjectDetails().put("outboundDeparture", outboundDeparture);
        this.getObjectDetails().put("outboundArrival", outboundArrival);
        this.getObjectDetails().put("returnDeparture", returnDeparture);
        this.getObjectDetails().put("returnArrival", returnArrival);
        this.getObjectDetails().put("origin", origin);
        this.getObjectDetails().put("destination", destination);
        this.getObjectDetails().put("price", price);
        this.getObjectDetails().put("adults", adults);
        this.getObjectDetails().put("children", children);
        this.getObjectDetails().put("departureDate", departureDate);
        this.getObjectDetails().put("returnDate", returnDate);
    }

    public static Flight fromJson(Map<String, Object> json) {
        return new Flight(
                (String) json.get("link"),
                (String) json.get("airline"),
                (String) json.get("outboundDeparture"),
                (String) json.get("outboundArrival"),
                (String) json.get("returnDeparture"),
                (String) json.get("returnArrival"),
                (String) json.get("departureAirport"),
                (String) json.get("arrivalAirport"),
                (String) json.get("price"),
                json.get("adults") != null ? ((Number) json.get("adults")).intValue() : 0,
                json.get("children") != null ? ((Number) json.get("children")).intValue() : 0,
                (String) json.get("departureDate"),
                (String) json.get("returnDate")
        );
    }

    public BoundaryObject toBoundaryObject() {
        Map<String, Object> details = new HashMap<>();
        details.put("link", this.getObjectDetails().get("link"));
        details.put("airline", this.getObjectDetails().get("airline"));
        details.put("outboundDeparture", this.getObjectDetails().get("outboundDeparture"));
        details.put("outboundArrival", this.getObjectDetails().get("outboundArrival"));
        details.put("returnDeparture", this.getObjectDetails().get("returnDeparture"));
        details.put("returnArrival", this.getObjectDetails().get("returnArrival"));
        details.put("origin", this.getObjectDetails().get("origin"));
        details.put("destination", this.getObjectDetails().get("destination"));
        details.put("price", this.getObjectDetails().get("price"));
        details.put("adults", this.getObjectDetails().get("adults"));
        details.put("children", this.getObjectDetails().get("children"));
        details.put("departureDate", this.getObjectDetails().get("departureDate"));
        details.put("returnDate", this.getObjectDetails().get("returnDate"));

        return new BoundaryObject(
                new ObjectIdBoundary(this.getObjectId().getSuperapp(), this.getObjectId().getId()),
                "Flight",
                "Flight to " + details.get("destination"),
                new LocationBoundary(0.0, 0.0),
                true,
                new Date().toString(),
                new CreatedByBoundary(new CreatedByBoundary.UserIdBoundary("YourSuperApp", "user@example.com")),
                details
        );
    }
}
