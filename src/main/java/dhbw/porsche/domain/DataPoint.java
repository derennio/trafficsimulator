package dhbw.porsche.domain;

import java.util.UUID;

public record DataPoint(
        UUID id,
        long time,
        float velocity,
        float error,
        float control,
        float vMax,
        float relPosition,
        float distance,
        int streetIdx) { }
