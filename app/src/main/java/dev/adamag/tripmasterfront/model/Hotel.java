package dev.adamag.tripmasterfront.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Hotel extends BoundaryObject {

    public Hotel() {
        super();
    }

    public Hotel(double lat, double lng, String hotelID, String name, String loc, String starRating, List<String> roomTypes, List<String> features) {
        super(
                new ObjectIdBoundary("YourSuperApp", hotelID),
                "Hotel",
                "Hotel " + name,
                new LocationBoundary(lat, lng),
                true,
                new Date().toString(),
                new CreatedByBoundary(new CreatedByBoundary.UserIdBoundary("YourSuperApp", "user@example.com")),
                new HashMap<String, Object>()
        );

        this.getObjectDetails().put("lat", lat);
        this.getObjectDetails().put("lng", lng);
        this.getObjectDetails().put("hotelID", hotelID);
        this.getObjectDetails().put("name", name);
        this.getObjectDetails().put("loc", loc);
        this.getObjectDetails().put("starRating", starRating);
        this.getObjectDetails().put("roomTypes", roomTypes);
        this.getObjectDetails().put("features", features);
    }

    public static Hotel fromBoundaryObject(BoundaryObject boundaryObject) {
        return new Hotel(
                (Double) boundaryObject.getObjectDetails().get("lat"),
                (Double) boundaryObject.getObjectDetails().get("lng"),
                (String) boundaryObject.getObjectDetails().get("hotelID"),
                (String) boundaryObject.getObjectDetails().get("name"),
                (String) boundaryObject.getObjectDetails().get("loc"),
                (String) boundaryObject.getObjectDetails().get("starRating"),
                (List<String>) boundaryObject.getObjectDetails().get("roomTypes"),
                (List<String>) boundaryObject.getObjectDetails().get("features")
        );
    }
}
