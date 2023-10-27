package dhbw.porsche.business.controller;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PIController {
    private final float pVal;
    private final float iVal;
    private float lastInput;

    public float calculate(float errVal, float dt) {
        float integral = lastInput + errVal * dt;
        lastInput = integral;
        return pVal * errVal + iVal * integral;
    }

}
