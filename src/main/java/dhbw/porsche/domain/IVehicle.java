package dhbw.porsche.domain;

public interface IVehicle {
    /**
     * Updates the velocity of the vehicle based on the controller's instruction.
     */
    void updateVelocity(float deltaT);

    /**
     * Moves the vehicle based on its velocity.
     */
    void move(float deltaT);

    /**
     * Returns the car's current velocity.
     * @param streetIdx The index of the current street.
     */
    void setStreetIdx(int streetIdx);

    /**
     * Returns the car's current street index.
     * @return The car's current street index.
     */
    int getStreetIdx();

    /**
     * Returns the car's length.
     * @return The car's length.
     */
    int getLength();

    /**
     * Returns the car's current relative position on the street.
     * @return The car's current relative position on the street.
     */
    double getRelPosition();

    /**
     * Sets the car's current relative position on the street.
     * @param relPosition The car's current relative position on the street.
     * @param streetIdx The index of the street.
     */
    IVehicle translocate(double relPosition, int streetIdx);
}
