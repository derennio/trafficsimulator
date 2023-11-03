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
     * Returns the car's current velocity.
     * @return The car's current velocity.
     */
    float getVelocity();

    /**
     * Sets the car's current relative position on the street.
     * @param relPosition The car's current relative position on the street.
     * @param streetIdx The index of the street.
     * @return The vehicle.
     */
    IVehicle translocate(double relPosition, int streetIdx);

    /**
     * Overrides controller output in order to offer simulation variations.
     * @param active Whether the override is active.
     * @param control The controller output to override.
     * @return The vehicle.
     */
    IVehicle overrideController(boolean active, float control);

    /**
     * Overrides controller output in order to offer simulation variations.
     * @param active Whether the override is active.
     * @return The vehicle.
     */
    IVehicle overrideController(boolean active);
}
