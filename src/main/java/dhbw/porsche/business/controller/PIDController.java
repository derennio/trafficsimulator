package dhbw.porsche.business.controller;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PIDController {
    private final float kp;
    private final float ki;
    private final float kd;
    private float lastError;
    private float integral;

    public float calculate(float error, float dt) {
        this.integral += error * dt;
        float derivative = (error - this.lastError) / dt;
        this.lastError = error;

        return this.kp * error + this.ki * this.integral + this.kd * derivative;
    }
}
