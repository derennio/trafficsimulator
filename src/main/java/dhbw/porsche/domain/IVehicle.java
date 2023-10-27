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

    /**
     * Returns the car's current velocity.
     * @param streetIdx The index of the current street.
     */
    void setStreetIdx(int streetIdx);
    int getStreetIdx();
    int getLength();
    double getRelPosition();
}
