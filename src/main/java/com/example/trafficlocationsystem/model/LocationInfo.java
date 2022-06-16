package com.example.trafficlocationsystem.model;

public class LocationInfo {
    private final String locationId;
    private final String locationName;
    private final String locationAddress;
    private final String locationLatitude;
    private final String locationLongitude;
    private final String locationDescription;
    private final String locationImage;

    public LocationInfo(Builder builder) {
        this.locationId = builder.locationId;
        this.locationName = builder.locationName;
        this.locationAddress = builder.locationAddress;
        this.locationLatitude = builder.locationLatitude;
        this.locationLongitude = builder.locationLongitude;
        this.locationDescription = builder.locationDescription;
        this.locationImage = builder.locationImage;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public String getLocationLatitude() {
        return locationLatitude;
    }

    public String getLocationLongitude() {
        return locationLongitude;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public String getLocationImage() {
        return locationImage;
    }


    public static final class Builder {
        private String locationId;
        private String locationName;
        private String locationAddress;
        private String locationLatitude;
        private String locationLongitude;
        private String locationDescription;
        private String locationImage;

        private Builder() {
        }

        public static Builder aLocationInfo() {
            return new Builder();
        }

        public Builder locationId(String locationId) {
            this.locationId = locationId;
            return this;
        }

        public Builder locationName(String locationName) {
            this.locationName = locationName;
            return this;
        }

        public Builder locationAddress(String locationAddress) {
            this.locationAddress = locationAddress;
            return this;
        }

        public Builder locationLatitude(String locationLatitude) {
            this.locationLatitude = locationLatitude;
            return this;
        }

        public Builder locationLongitude(String locationLongitude) {
            this.locationLongitude = locationLongitude;
            return this;
        }

        public Builder locationDescription(String locationDescription) {
            this.locationDescription = locationDescription;
            return this;
        }

        public Builder locationImage(String locationImage) {
            this.locationImage = locationImage;
            return this;
        }

        public LocationInfo build() {
           return new LocationInfo(this);
        }
    }
}
