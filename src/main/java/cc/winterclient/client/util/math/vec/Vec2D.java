package cc.winterclient.client.util.math.vec;

public class Vec2D {


    private final double x,z;

    public Vec2D(double x, double z) {
        this.x = x;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getZ() {
        return z;
    }

    public Vec2D add(double _x, double _z){
        return new Vec2D(x+_x, z+_z);
    }

}
