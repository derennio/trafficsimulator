package dhbw.porsche.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Point2D {
    private float x;
    private float y;

    public float distanceTo(Point2D other) {
        return (float) Math.sqrt(Math.pow(other.x - x, 2) + Math.pow(other.y - y, 2));
    }

    public static float dist(Point2D a, Point2D b) {
        return a.distanceTo(b);
    }
}
