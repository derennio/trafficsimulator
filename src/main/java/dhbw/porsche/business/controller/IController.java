package dhbw.porsche.business.controller;

public interface IController {
    /**
     * Calculates the output of the controller.
     * @param error The current error (delta dist)
     * @param dt The time difference between the last calculation and the current one
     * @return The output of the controller
     */
    float calculate(float error, float dt);
}
