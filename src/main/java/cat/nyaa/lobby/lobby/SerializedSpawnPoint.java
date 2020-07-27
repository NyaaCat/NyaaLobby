package cat.nyaa.lobby.lobby;

import cat.nyaa.nyaacore.configuration.ISerializable;

public class SerializedSpawnPoint implements ISerializable {
    @Serializable
    SerializedLocation center;
    @Serializable
    double radius;

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

    public double getRadius() {
        return radius;
    }
}
