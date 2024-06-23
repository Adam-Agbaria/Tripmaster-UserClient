package dev.adamag.tripmasterfront.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Destination extends BoundaryObject {

    public Destination() {
        super();
    }

    public Destination(double lat, double lng, String name, String country, String weatherInfo, String touristSeason, List<String> attractions) {
        super(
                new ObjectIdBoundary("YourSuperApp", name),
                "Destination",
                "Destination " + name,
                new LocationBoundary(lat, lng),
                true,
                new Date().toString(),
                new CreatedByBoundary(new CreatedByBoundary.UserIdBoundary("YourSuperApp", "user@example.com")),
                new HashMap<String, Object>()
        );

        this.getObjectDetails().put("lat", lat);
        this.getObjectDetails().put("lng", lng);
        this.getObjectDetails().put("name", name);
        this.getObjectDetails().put("country", country);
        this.getObjectDetails().put("weatherInfo", weatherInfo);
        this.getObjectDetails().put("touristSeason", touristSeason);
        this.getObjectDetails().put("attractions", attractions);
    }

    public static Destination fromBoundaryObject(BoundaryObject boundaryObject) {
        return new Destination(
                (Double) boundaryObject.getObjectDetails().get("lat"),
                (Double) boundaryObject.getObjectDetails().get("lng"),
                (String) boundaryObject.getObjectDetails().get("name"),
                (String) boundaryObject.getObjectDetails().get("country"),
                (String) boundaryObject.getObjectDetails().get("weatherInfo"),
                (String) boundaryObject.getObjectDetails().get("touristSeason"),
                (List<String>) boundaryObject.getObjectDetails().get("attractions")
        );
    }
}
