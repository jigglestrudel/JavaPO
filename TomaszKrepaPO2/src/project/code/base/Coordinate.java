package project.code.base;

import java.io.Serializable;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Coordinate implements Serializable {
    public int x;
    public int y;

    public Coordinate(int nx, int ny)
    {
        this.x = nx;
        this.y = ny;
    }

    public Coordinate sum(Coordinate cord) {
        return new Coordinate(this.x + cord.x, this.y + cord.y);
    }

    public Coordinate multiply(int scale) {
        return  new Coordinate(this.x * scale, this.y * scale);
    }

    public double distanceTo(Coordinate cord)
    {
        return sqrt(pow(this.x-cord.x, 2)+pow(this.y-cord.y, 2));
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coordinate)
        {
            return ((Coordinate) obj).x == x && ((Coordinate) obj).y == y;
        }
        return false;
    }
}
