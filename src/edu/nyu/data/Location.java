package edu.nyu.data;

public class Location {
  private double latitude;
  private double attitude;

  private Location(double latitude, double attitude) {
    this.latitude = latitude;
    this.attitude = attitude;
  }

  double getLatitude() {
    return latitude;
  }

  void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  double getAttitude() {
    return attitude;
  }

  void setAttitude(double attitude) {
    this.attitude = attitude;
  }

}
