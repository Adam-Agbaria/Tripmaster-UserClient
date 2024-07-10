package dev.adamag.tripmasterfront.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Hotel extends BoundaryObject {

    public Hotel() {
        super();
    }

    public Hotel(String city, String checkInDate, String checkOutDate, int adults, int children, int rooms,
                 String hotelName, String url, String price) {
        super(
                new ObjectIdBoundary("YourSuperApp", url), // Use url as the ID
                "Hotel",
                "Hotel stay in " + city,
                new LocationBoundary(0.0, 0.0),
                true,
                new Date().toString(),
                new CreatedByBoundary(new CreatedByBoundary.UserIdBoundary("YourSuperApp", "user@example.com")),
                new HashMap<String, Object>()
        );

        this.getObjectDetails().put("city", city);
        this.getObjectDetails().put("checkInDate", checkInDate);
        this.getObjectDetails().put("checkOutDate", checkOutDate);
        this.getObjectDetails().put("adults", adults);
        this.getObjectDetails().put("children", children);
        this.getObjectDetails().put("rooms", rooms);
        this.getObjectDetails().put("hotelName", hotelName);
        this.getObjectDetails().put("url", url);
        this.getObjectDetails().put("price", price);
    }

    public static Hotel fromJson(Map<String, Object> json) {
        String city = json.get("city") != null ? json.get("city").toString() : null;
        String checkInDate = json.get("checkInDate") != null ? json.get("checkInDate").toString() : null;
        String checkOutDate = json.get("checkOutDate") != null ? json.get("checkOutDate").toString() : null;
        String hotelName = json.get("hotelName") != null ? json.get("hotelName").toString() : null;
        String url = json.get("url") != null ? json.get("url").toString() : null;
        String price = json.get("price") != null ? json.get("price").toString() : null;

        int adults = json.get("adults") != null ? ((Number) json.get("adults")).intValue() : 0;
        int children = json.get("children") != null ? ((Number) json.get("children")).intValue() : 0;
        int rooms = json.get("rooms") != null ? ((Number) json.get("rooms")).intValue() : 0;

        return new Hotel(city, checkInDate, checkOutDate, adults, children, rooms, hotelName, url, price);
    }

    public BoundaryObject toBoundaryObject() {
        Map<String, Object> details = new HashMap<>();
        details.put("city", this.getObjectDetails().get("city"));
        details.put("checkInDate", this.getObjectDetails().get("checkInDate"));
        details.put("checkOutDate", this.getObjectDetails().get("checkOutDate"));
        details.put("adults", this.getObjectDetails().get("adults"));
        details.put("children", this.getObjectDetails().get("children"));
        details.put("rooms", this.getObjectDetails().get("rooms"));
        details.put("hotelName", this.getObjectDetails().get("hotelName"));
        details.put("url", this.getObjectDetails().get("url"));
        details.put("price", this.getObjectDetails().get("price"));

        return new BoundaryObject(
                new ObjectIdBoundary(this.getObjectId().getSuperapp(), this.getObjectId().getId()),
                "Hotel",
                "Hotel stay in " + details.get("city"),
                new LocationBoundary(0.0, 0.0),
                true,
                new Date().toString(),
                new CreatedByBoundary(new CreatedByBoundary.UserIdBoundary("YourSuperApp", "user@example.com")),
                details
        );
    }
}
