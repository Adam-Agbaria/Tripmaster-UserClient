package dev.adamag.tripmasterfront.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Flight extends BoundaryObject {

    public Flight() {
        super();
    }

    public Flight(String flightID, String airline, String departureTime, String arrivalTime, String origin, String destination, String price, int adults, int children) {
        super(
                new ObjectIdBoundary("YourSuperApp", flightID),
                "Flight",
                "Flight to " + destination,
                new LocationBoundary(0.0, 0.0),
                ActivatedStatus.ACTIVE,
                new Date().toString(),
                new CreatedByBoundary(new CreatedByBoundary.UserIdBoundary("YourSuperApp", "user@example.com")),
                new HashMap<String, Object>()
        );

        this.getObjectDetails().put("flightID", flightID);
        this.getObjectDetails().put("airline", airline);
        this.getObjectDetails().put("departureTime", departureTime);
        this.getObjectDetails().put("arrivalTime", arrivalTime);
        this.getObjectDetails().put("origin", origin);
        this.getObjectDetails().put("destination", destination);
        this.getObjectDetails().put("price", price);
        this.getObjectDetails().put("adults", adults);
        this.getObjectDetails().put("children", children);
    }

    public static Flight fromBoundaryObject(BoundaryObject boundaryObject) {
        return new Flight(
                (String) boundaryObject.getObjectDetails().get("flightID"),
                (String) boundaryObject.getObjectDetails().get("airline"),
                (String) boundaryObject.getObjectDetails().get("departureTime"),
                (String) boundaryObject.getObjectDetails().get("arrivalTime"),
                (String) boundaryObject.getObjectDetails().get("origin"),
                (String) boundaryObject.getObjectDetails().get("destination"),
                (String) boundaryObject.getObjectDetails().get("price"),
                (int) boundaryObject.getObjectDetails().get("adults"),
                (int) boundaryObject.getObjectDetails().get("children")
        );
    }

    public BoundaryObject toBoundaryObject() {
        Map<String, Object> details = new HashMap<>();
        details.put("flightID", this.getObjectDetails().get("flightID"));
        details.put("airline", this.getObjectDetails().get("airline"));
        details.put("departureTime", this.getObjectDetails().get("departureTime"));
        details.put("arrivalTime", this.getObjectDetails().get("arrivalTime"));
        details.put("origin", this.getObjectDetails().get("origin"));
        details.put("destination", this.getObjectDetails().get("destination"));
        details.put("price", this.getObjectDetails().get("price"));
        details.put("adults", this.getObjectDetails().get("adults"));
        details.put("children", this.getObjectDetails().get("children"));

        return new BoundaryObject(
                new ObjectIdBoundary(this.getObjectId().getSuperapp(), this.getObjectId().getId()),
                "Flight",
                "Flight to " + details.get("destination"),
                new LocationBoundary(0.0, 0.0),
                ActivatedStatus.ACTIVE,
                new Date().toString(),
                new CreatedByBoundary(new CreatedByBoundary.UserIdBoundary("YourSuperApp", "user@example.com")),
                details
        );
    }
}
