package dev.adamag.tripmasterfront.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Notification extends BoundaryObject {

    public Notification() {
        super();
    }

    public Notification(String email, String message) {
        super(
                new ObjectIdBoundary("YourSuperApp", null), // ID will be null initially
                "Notification",
                "Notification for " + email,
                new LocationBoundary(0.0, 0.0),
                true,
                new Date().toString(),
                new CreatedByBoundary(new CreatedByBoundary.UserIdBoundary("YourSuperApp", "system@example.com")),
                new HashMap<String, Object>()
        );

        this.getObjectDetails().put("email", email);
        this.getObjectDetails().put("message", message);
    }

    public String getEmail() {
        return (String) this.getObjectDetails().get("email");
    }

    public void setEmail(String email) {
        this.getObjectDetails().put("email", email);
    }

    public String getMessage() {
        return (String) this.getObjectDetails().get("message");
    }

    public void setMessage(String message) {
        this.getObjectDetails().put("message", message);
    }

    public static Notification fromJson(Map<String, Object> json) {
        return new Notification(
                (String) json.get("email"),
                (String) json.get("message")
        );
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("email", this.getEmail());
        map.put("message", this.getMessage());
        return map;
    }
}
