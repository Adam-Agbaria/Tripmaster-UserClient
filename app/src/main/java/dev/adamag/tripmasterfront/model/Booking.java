package dev.adamag.tripmasterfront.model;

import java.util.Date;
import java.util.HashMap;

public class Booking extends BoundaryObject {

    public Booking() {
        super();
    }

    public Booking(String bookingID, String vacationPackageID, String flightID, String hotelID, String totalPrice) {
        super(
                new ObjectIdBoundary("YourSuperApp", bookingID),
                "Booking",
                "Booking " + bookingID,
                new LocationBoundary(0.0, 0.0),
                ActivatedStatus.ACTIVE,
                new Date().toString(),
                new CreatedByBoundary(new CreatedByBoundary.UserIdBoundary("YourSuperApp", "user@example.com")),
                new HashMap<String, Object>()
        );

        this.getObjectDetails().put("bookingID", bookingID);
        this.getObjectDetails().put("vacationPackageID", vacationPackageID);
        this.getObjectDetails().put("flightID", flightID);
        this.getObjectDetails().put("hotelID", hotelID);
        this.getObjectDetails().put("totalPrice", totalPrice);
    }

    public static Booking fromBoundaryObject(BoundaryObject boundaryObject) {
        return new Booking(
                (String) boundaryObject.getObjectDetails().get("bookingID"),
                (String) boundaryObject.getObjectDetails().get("vacationPackageID"),
                (String) boundaryObject.getObjectDetails().get("flightID"),
                (String) boundaryObject.getObjectDetails().get("hotelID"),
                (String) boundaryObject.getObjectDetails().get("totalPrice")
        );
    }
}
