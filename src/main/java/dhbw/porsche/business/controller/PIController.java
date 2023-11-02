package dhbw.porsche.business.controller;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PIController implements IController {
    /**
     * Proportional value.
     */
    private final float pVal;

    /**
     * Integral value.
     */
    private final float iVal;

    /**
     * The last input value.
     */
    private float lastInput;

    /**
     * Calculates the output of the controller.
     * @param error The current error (delta dist)
     * @param dt The time difference between the last calculation and the current one
     * @return The output of the controller
     */
    public float calculate(float error, float dt) {
        float integral = lastInput + error * dt;
        lastInput = integral;
        return pVal * error + iVal * integral;
    }

}
