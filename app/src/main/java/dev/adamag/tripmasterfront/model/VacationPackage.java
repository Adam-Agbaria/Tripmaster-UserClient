package dev.adamag.tripmasterfront.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class VacationPackage extends BoundaryObject {

    public VacationPackage() {
        super();
    }

    public VacationPackage(String packageID, String destination, String price, String duration, List<String> includedServices) {
        super(
                new ObjectIdBoundary("YourSuperApp", packageID),
                "VacationPackage",
                "Vacation Package to " + destination,
                new LocationBoundary(0.0, 0.0),
                ActivatedStatus.ACTIVE,
                new Date().toString(),
                new CreatedByBoundary(new CreatedByBoundary.UserIdBoundary("YourSuperApp", "user@example.com")),
                new HashMap<String, Object>()
        );

        this.getObjectDetails().put("packageID", packageID);
        this.getObjectDetails().put("destination", destination);
        this.getObjectDetails().put("price", price);
        this.getObjectDetails().put("duration", duration);
        this.getObjectDetails().put("includedServices", includedServices);
    }

    public static VacationPackage fromBoundaryObject(BoundaryObject boundaryObject) {
        return new VacationPackage(
                (String) boundaryObject.getObjectDetails().get("packageID"),
                (String) boundaryObject.getObjectDetails().get("destination"),
                (String) boundaryObject.getObjectDetails().get("price"),
                (String) boundaryObject.getObjectDetails().get("duration"),
                (List<String>) boundaryObject.getObjectDetails().get("includedServices")
        );
    }
}
