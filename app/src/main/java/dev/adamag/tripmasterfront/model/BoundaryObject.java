package dev.adamag.tripmasterfront.model;


import java.util.Map;

public class BoundaryObject {

    private ObjectIdBoundary objectId;
    private String type;
    private String alias;
    private LocationBoundary location;
    private ActivatedStatus active;
    private String creationTimestamp;
    private CreatedByBoundary createdBy;
    private Map<String, Object> objectDetails;


    public BoundaryObject() {
    }

    public BoundaryObject(ObjectIdBoundary objectId, String type, String alias, LocationBoundary location, ActivatedStatus active, String creationTimestamp, CreatedByBoundary createdBy, Map<String, Object> objectDetails) {
        this.objectId = objectId;
        this.type = type;
        this.alias = alias;
        this.location = location;
        this.active = active;
        this.creationTimestamp = creationTimestamp;
        this.createdBy = createdBy;
        this.objectDetails = objectDetails;
    }

    public ObjectIdBoundary getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectIdBoundary objectId) {
        this.objectId = objectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public LocationBoundary getLocation() {
        return location;
    }

    public void setLocation(LocationBoundary location) {
        this.location = location;
    }

    public ActivatedStatus isActive() {
        return active;
    }

    public void setActive(ActivatedStatus active) {
        this.active = active;
    }

    public String getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public CreatedByBoundary getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedByBoundary createdBy) {
        this.createdBy = createdBy;
    }

    public Map<String, Object> getObjectDetails() {
        return objectDetails;
    }

    public void setObjectDetails(Map<String, Object> objectDetails) {
        this.objectDetails = objectDetails;
    }


    public static class ObjectIdBoundary {

        private String superapp;

        private String id;

        public ObjectIdBoundary() {
        }

        public ObjectIdBoundary(String superapp, String id) {
            this.superapp = superapp;
            this.id = id;
        }

        public String getSuperapp() {
            return superapp;
        }

        public void setSuperapp(String superapp) {
            this.superapp = superapp;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class LocationBoundary {
        private double lat = 0.0;
        private double lng = 0.0;

        public LocationBoundary() {
        }

        public LocationBoundary(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }

    public static class CreatedByBoundary {

        private UserIdBoundary userId;

        public CreatedByBoundary() {
        }

        public CreatedByBoundary(UserIdBoundary userId) {
            this.userId = userId;
        }

        public UserIdBoundary getUserId() {
            return userId;
        }

        public void setUserId(UserIdBoundary userId) {
            this.userId = userId;
        }

        public static class UserIdBoundary {

            private String superapp;

            private String email;

            public UserIdBoundary() {
            }

            public UserIdBoundary(String superapp, String email) {
                this.superapp = superapp;
                this.email = email;
            }

            public String getSuperapp() {
                return superapp;
            }

            public void setSuperapp(String superapp) {
                this.superapp = superapp;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }
        }
    }
}
