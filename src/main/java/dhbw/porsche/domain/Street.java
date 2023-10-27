package dhbw.porsche.domain;

import dhbw.porsche.common.Point2D;

public record Street (float vMax, Point2D start, Point2D end) {
    public boolean isVertical() {
        return start.getX() == end.getX();
    }
}
