package dhbw.porsche.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Car implements IVehicle {
    /**
     *  the car's current velocity.
     */
    private float velocity;

    /**
     * The car's maximum positive acceleration.
     */
    private float maxAccel;

    /**
     * The car's maximum brake force.
     */
    private float maxBrake;

    /**
     * The car's maximum velocity.
     */
    private float maxVelocity;

    /**
     * The car's length.
     */
    private float length;

    /**
     * Index of the current street.
     */
    private int streetIdx;

    /**
     * position relative to street length (0.0 - 1.0).
     */
    private double relPosition;

    /**
     * Updates the velocity of the vehicle based on the controller's instruction.
     */
    @Override
    public void updateVelocity() {

    }

    /**
     * Moves the vehicle based on its velocity.
     */
    @Override
    public void move() {

    }
}
