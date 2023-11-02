package dhbw.porsche.business.controller;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PIDController implements IController {
    /**
     * Proportional value.
     */
    private final float pVal;

    /**
     * Integral value.
     */
    private final float iVal;

    /**
     * Derivative value.
     */
    private final float dVal;

    /**
     * The last error value.
     */
    private float lastError;

    /**
     * The last integral value.
     */
    private float integral;

    /**
     * Calculates the output of the controller.
     * @param error The current error (delta dist)
     * @param dt The time difference between the last calculation and the current one
     * @return The output of the controller
     */
    public float calculate(float error, float dt) {
        this.integral += error * dt;
        float derivative = (error - this.lastError) / dt;
        this.lastError = error;

        return this.pVal * error + this.iVal * this.integral + this.dVal * derivative;
    }
}
