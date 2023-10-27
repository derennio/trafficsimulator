package dhbw.porsche.domain;

public interface IVehicle {
    /**
     * Updates the velocity of the vehicle based on the controller's instruction.
     */
    void updateVelocity();

    /**
     * Moves the vehicle based on its velocity.
     */
    void move();
}
