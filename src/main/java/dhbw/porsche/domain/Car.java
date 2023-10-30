package dhbw.porsche.domain;

import dhbw.porsche.business.IStreetService;
import dhbw.porsche.business.controller.PIController;
import dhbw.porsche.common.Point2D;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Car implements IVehicle {    
    /**
    * The street repository and service.
    */
    private final IStreetService streetService;

    private PIController controller;
    
    /**
     *  the car's current velocity.
     */
    private float velocity;

    /**
     * The car's maximum positive acceleration.
     */
    private final float maxAccel;

    /**
     * The car's maximum brake force.
     */
    private final float maxBrake;

    /**
     * The car's maximum velocity.
     */
    private final float maxVelocity;

    /**
     * The car's length.
     */
    private final int length;

    /**
     * Index of the current street.
     */
    private int streetIdx;

    /**
     * position relative to street length (0.0 - 1.0).
     */
    private double relPosition = 0.0d;


    /**
     * Updates the velocity of the vehicle based on the controller's instruction.
     */
    @Override
    public void updateVelocity(float deltaT) {
        controller.calculate(0.0f, deltaT);
    }

    /**
     * Moves the vehicle based on its velocity.
     */
    @Override
    public void move(float deltaT) {
        Street street = streetService.getStreetById(streetIdx);
        this.velocity = 5f;

        relPosition += (velocity * deltaT) / street.getLength();
        System.out.println(relPosition);
    }

    /**
     * Returns the car's current velocity.
     *
     * @param streetIdx The index of the current street.
     */
    @Override
    public void setStreetIdx(int streetIdx) {
        this.streetIdx = streetIdx;
    }
}
