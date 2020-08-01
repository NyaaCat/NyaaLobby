package cat.nyaa.lobby.lobby;

import cat.nyaa.nyaacore.configuration.ISerializable;

public class SerializedSpawnPoint implements ISerializable {
    @Serializable
    SerializedLocation center;
    @Serializable
    double radius = 5;

    public SerializedSpawnPoint(){

    }

    public SerializedSpawnPoint(SerializedLocation center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public void setSpawnPoint(SerializedLocation center){
        this.center = center;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public SerializedLocation getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }
}
