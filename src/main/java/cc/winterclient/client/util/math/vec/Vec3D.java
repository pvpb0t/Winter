package cc.winterclient.client.util.math.vec;

public class Vec3D {

    private final double x,y,z;

    public Vec3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Vec3D add(double _x, double _y, double _z){
        return new Vec3D(x+_x, y+_y, z+_z);
    }
}
