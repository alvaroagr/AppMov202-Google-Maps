package com.example.holetrackingapp.model;

/**
 * Object that represents a user's position. Consists of:
 * <br>
 *     double: latitude and longitude, used to create LatLng objects and thus Location and LatLng
 *     objects.
 * <br>
 *     String: user, used to determine if this is the current user's position or not, and thus,
 *     whether or not their marker is different.
 */
public class Position {

    private String user;
    private double lat;
    private double lng;

    public Position() {
    }

//    public Position(double lat, double lng) {
//        this.lat = lat;
//        this.lng = lng;
//    }

    public Position(String user, double lat, double lng) {
        this.user = user;
        this.lat = lat;
        this.lng = lng;
    }

    public String getUser() {
        return user;
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
