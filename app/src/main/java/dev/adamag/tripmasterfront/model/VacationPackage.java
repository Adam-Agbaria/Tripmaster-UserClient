package dev.adamag.tripmasterfront.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VacationPackage extends BoundaryObject {

    public VacationPackage() {
        super();
    }

    public VacationPackage(String packageId, String packageName, String destination, String hotelName, int stars, boolean isConnectionFlight, double price, String startDate, String endDate) {
        super(
                new ObjectIdBoundary("YourSuperApp", packageId), // Use packageId as the ID
                "VacationPackage",
                generateAlias(destination, startDate, endDate),
                new LocationBoundary(0.0, 0.0),
                true,
                new Date().toString(),
                new CreatedByBoundary(new CreatedByBoundary.UserIdBoundary("YourSuperApp", "user@example.com")),
                new HashMap<String, Object>()
        );

        this.getObjectDetails().put("packageId", packageId);
        this.getObjectDetails().put("packageName", packageName);
        this.getObjectDetails().put("destination", destination);
        this.getObjectDetails().put("hotelName", hotelName);
        this.getObjectDetails().put("stars", stars);
        this.getObjectDetails().put("isConnectionFlight", isConnectionFlight);
        this.getObjectDetails().put("price", price);
        this.getObjectDetails().put("startDate", startDate);
        this.getObjectDetails().put("endDate", endDate);
    }

    private static String generateAlias(String destination, String startDate, String endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);
            long duration = (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24); // Calculate duration in days
            return destination + " " + duration + " days";
        } catch (ParseException e) {
            e.printStackTrace();
            return destination + " unknown duration";
        }
    }

    public static VacationPackage fromJson(Map<String, Object> json) {
        return new VacationPackage(
                (String) json.get("packageId"),
                (String) json.get("packageName"),
                (String) json.get("destination"),
                (String) json.get("hotelName"),
                json.get("stars") != null ? (int) json.get("stars") : 0,
                json.get("isConnectionFlight") != null ? (boolean) json.get("isConnectionFlight") : false,
                json.get("price") != null ? (double) json.get("price") : 0.0,
                (String) json.get("startDate"),
                (String) json.get("endDate")
        );
    }

    public static VacationPackage fromBoundaryObject(BoundaryObject boundaryObject) {
        return new VacationPackage(
                (String) boundaryObject.getObjectDetails().get("packageId"),
                (String) boundaryObject.getObjectDetails().get("packageName"),
                (String) boundaryObject.getObjectDetails().get("destination"),
                (String) boundaryObject.getObjectDetails().get("hotelName"),
                boundaryObject.getObjectDetails().get("stars") != null ? (int) boundaryObject.getObjectDetails().get("stars") : 0,
                boundaryObject.getObjectDetails().get("isConnectionFlight") != null ? (boolean) boundaryObject.getObjectDetails().get("isConnectionFlight") : false,
                boundaryObject.getObjectDetails().get("price") != null ? (double) boundaryObject.getObjectDetails().get("price") : 0.0,
                (String) boundaryObject.getObjectDetails().get("startDate"),
                (String) boundaryObject.getObjectDetails().get("endDate")
        );
    }

    public BoundaryObject toBoundaryObject() {
        Map<String, Object> details = new HashMap<>();
        details.put("packageId", this.getObjectDetails().get("packageId"));
        details.put("packageName", this.getObjectDetails().get("packageName"));
        details.put("destination", this.getObjectDetails().get("destination"));
        details.put("hotelName", this.getObjectDetails().get("hotelName"));
        details.put("stars", this.getObjectDetails().get("stars"));
        details.put("isConnectionFlight", this.getObjectDetails().get("isConnectionFlight"));
        details.put("price", this.getObjectDetails().get("price"));
        details.put("startDate", this.getObjectDetails().get("startDate"));
        details.put("endDate", this.getObjectDetails().get("endDate"));

        return new BoundaryObject(
                new ObjectIdBoundary(this.getObjectId().getSuperapp(), this.getObjectId().getId()),
                "VacationPackage",
                this.getAlias(),
                new LocationBoundary(0.0, 0.0),
                true,
                new Date().toString(),
                new CreatedByBoundary(new CreatedByBoundary.UserIdBoundary("YourSuperApp", "user@example.com")),
                details
        );
    }

    public String getAlias() {
        return generateAlias(
                (String) this.getObjectDetails().get("destination"),
                (String) this.getObjectDetails().get("startDate"),
                (String) this.getObjectDetails().get("endDate")
        );
    }
}
