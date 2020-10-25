package com.example.holetrackingapp.model;

/**
 * Object that represents a hole. Consists of:
 * <br>
 *     id: The only thing that differentiates holes from one another, as it's possible for two
 *     holes to be in the same coordinates, for a user to have reported many holes, and to be or
 *     not confirmed.
 * <br>
 *     lat & lng: The hole's coordinates, used to create Markers.
 * <br>
 *     author: The user that reported the hole. Used to check if the current user is allowed to
 *     confirm a hole or not.
 * <br>
 *     isConfirmed: Whether or not the hole has been confirmed by a user other than the author.
 */
public class Hole {

    private String id;
    private double lat;
    private double lng;
    private String author;
    private boolean isConfirmed;

    public Hole() {
    }

    public Hole(String id, double lat, double lng, String author) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.author = author;
        this.isConfirmed = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }
}
