package dev.adamag.tripmasterfront.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Password extends BoundaryObject {

    public Password() {
        super();
    }

    public Password(String password) {
        super(
                new ObjectIdBoundary("YourSuperApp", "passwordId"),
                "Password",
                "User password",
                new LocationBoundary(0.0, 0.0),
                ActivatedStatus.ACTIVE,
                new Date().toString(),
                new CreatedByBoundary(new CreatedByBoundary.UserIdBoundary("YourSuperApp", "user@example.com")),
                new HashMap<String, Object>()
        );

        this.getObjectDetails().put("password", password);
    }

    public static Password fromBoundaryObject(BoundaryObject boundaryObject) {
        return new Password(
                (String) boundaryObject.getObjectDetails().get("password")
        );
    }

    public BoundaryObject toBoundaryObject() {
        Map<String, Object> details = new HashMap<>();
        details.put("password", this.getObjectDetails().get("password"));

        return new BoundaryObject(
                new ObjectIdBoundary(this.getObjectId().getSuperapp(), this.getObjectId().getId()),
                "Password",
                "User password",
                new LocationBoundary(0.0, 0.0),
                ActivatedStatus.ACTIVE,
                new Date().toString(),
                new CreatedByBoundary(new CreatedByBoundary.UserIdBoundary()),
                details
        );
    }
}
