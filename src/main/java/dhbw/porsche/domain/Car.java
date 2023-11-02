package dhbw.porsche.domain;

import dhbw.porsche.business.IStreetService;
import dhbw.porsche.business.controller.IController;
import dhbw.porsche.common.Point2D;
import dhbw.porsche.common.Tuple;
import dhbw.porsche.file.IFileService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Car implements IVehicle {
    /**
     * The car's id.
     */
    private UUID id = UUID.randomUUID();

    /**
    * The street repository and service.
    */
    private final IStreetService streetService;

    /**
     * The file service.
     */
    private final IFileService fileService;

    /**
     * The car's controller.
     */
    private final IController controller;

    /**
     * The car's maximum positive acceleration.
     */
    private final float maxAccel;

    /**
     * The car's maximum brake force.
     */
    private final float maxBrake;

    /**
     * The car's maximum velocity.
     */
    private final float maxVelocity;

    /**
     * The car's length.
     */
    private final int length;
    
    /**
     *  the car's current velocity.
     */
    private float velocity;

    /**
     * Index of the current street.
     */
    private int streetIdx = 0;

    /**
     * position relative to street length (0.0 - 1.0).
     */
    private double relPosition = 0.0d;

    /**
     * The seed for the random number generator.
     */
    private int[] seed = generateSeed();

    /**
     * The index of the current seed.
     */
    private int seedIdx;

    /**
     * Whether the controller is currently being overridden.
     */
    private boolean overrideActive;

    /**
     * The controller output to override.
     */
    private float controlOverride;

    /**
     * Updates the velocity of the vehicle based on the controller's instruction.
     */
    @Override
    public void updateVelocity(float deltaT) {
        float desiredDist = this.velocity * 3.6f / 2 + this.getLength();
        var ahead = lookAhead(desiredDist);
        float error = 0, control = 0;

        if (ahead.isPresent()) {
            var v = ahead.get().t();
            var dist = ahead.get().v();
            error = (dist - desiredDist);

            if (this.overrideActive) {
                control = this.controlOverride;
            } else {
                control = this.controller.calculate(error, deltaT);
            }

            if (control > 0) {
                this.velocity += Math.min(control, this.maxAccel * deltaT);
            } else {
                this.velocity -= Math.min(-control, this.maxBrake * deltaT);
            }
        } else {
            if (this.overrideActive) {
                if (this.controlOverride > 0) {
                    this.velocity += Math.min(this.controlOverride, this.maxAccel * deltaT);
                } else {
                    this.velocity -= Math.min(-this.controlOverride, this.maxBrake * deltaT);
                }
            } else {
                this.velocity = Math.min(this.velocity + this.maxAccel * deltaT, Math.min(this.streetService.getStreetById(streetIdx).vMax(), this.maxVelocity));
            }
        }

        DataPoint dp = new DataPoint(this.getId(), System.currentTimeMillis(), this.velocity, error, control, this.streetService.getStreetById(streetIdx).vMax(), (float)this.relPosition, 187, this.streetIdx);
        this.fileService.appendData(dp);
    }

    /**
     * Moves the vehicle based on its velocity.
     */
    @Override
    public void move(float deltaT) {
        this.updateVelocity(deltaT);

        Street street = streetService.getStreetById(streetIdx);
        relPosition += (velocity * deltaT) / street.getLength();

        if (relPosition >= 1.0d) {
            relPosition = 0.0d;
            Point2D end = street.end();

            var options = this.streetService.getStreets()
                    .stream()
                    .filter(
                            s -> s.start().getX() == end.getX() && s.start().getY() == end.getY())
                    .toArray();

            if (options.length == 0) {
                this.streetService.removeVehicle(this);
                return;
            }

            this.streetIdx = this.streetService.getStreets().indexOf(options[this.seed[this.seedIdx++] % options.length]);
        }
    }

    /**
     * Returns the car's current velocity.
     *
     * @param streetIdx The index of the current street.
     */
    @Override
    public void setStreetIdx(int streetIdx) {
        this.streetIdx = streetIdx;
    }

    /**
     * Sets the car's current relative position on the street.
     *
     * @param relPosition The car's current relative position on the street.
     * @param streetIdx   The index of the street.
     */
    @Override
    public IVehicle translocate(double relPosition, int streetIdx) {
        this.relPosition = relPosition;
        this.streetIdx = streetIdx;
        return this;
    }

    /**
     * Overrides controller output in order to offer simulation variations.
     * @param active Whether the override is active.
     * @param control The controller output to override.
     * @return The vehicle.
     */
    @Override
    public IVehicle overrideController(boolean active, float control) {
        this.overrideActive = active;
        this.controlOverride = control;
        return this;
    }

    /**
     * Overrides controller output in order to offer simulation variations.
     *
     * @param active Whether the override is active.
     * @return The vehicle.
     */
    @Override
    public IVehicle overrideController(boolean active) {
        this.overrideActive = active;
        return this;
    }

    /**
     * Generates a seed for the car.
     * @return The seed.
     */
    private static int[] generateSeed() {
        int[] seed = new int[10];
        for (int i = 0; i < seed.length; i++) {
            seed[i] = (int)(Math.random() * 10);
        }
        return seed;
    }

    private Optional<Tuple<IVehicle, Float>> lookAhead(float targetDist) {
        return lookAhead(this.streetIdx, 0, 0, targetDist);
    }

    /**
     * Looks ahead on the street to find the next vehicle.
     * @param streetIdx The index of the current street.
     * @param distPassed The distance passed while searching for the next vehicle.
     * @param nthStreet The number of streets passed while searching for the next vehicle.
     * @param targetDist The desired distance to maintain between vehicles.
     * @return The next vehicle and the distance to it.
     */
    private Optional<Tuple<IVehicle, Float>> lookAhead(int streetIdx, float distPassed, int nthStreet, float targetDist) {
        var searchTarget = this.streetService.getStreetById(this.streetIdx);

        var vehiclesOnStreet = this.streetService.getVehicles().stream().filter(v -> v.getStreetIdx() == streetIdx).toArray(IVehicle[]::new);
        for (IVehicle v :
             vehiclesOnStreet) {
            if (nthStreet == 0) {
                if (this.getRelPosition() < v.getRelPosition()) {
                    double streetDist = (v.getRelPosition() - this.getRelPosition()) * searchTarget.getLength();
                    if (streetDist < targetDist) {
                        return Optional.of(new Tuple<>(v, (float)streetDist));
                    }
                }
            } else {
                double streetDist = v.getRelPosition() * searchTarget.getLength() + distPassed;
                if (streetDist < targetDist) {
                    return Optional.of(new Tuple<>(v, (float)streetDist));
                }
            }
        }

        var options = this.streetService.getStreets().stream().filter(s -> s.start().getX() == searchTarget.end().getX() && s.start().getY() == searchTarget.end().getY()).toArray(Street[]::new);

        if (options.length == 0) {
            return Optional.empty();
        }

        var nextIdx = this.streetService.getStreets().indexOf(options[this.seed[(this.seedIdx + nthStreet + 1)] % options.length]);
        float _distPassed = distPassed + searchTarget.getLength();
        if (nthStreet == 0) {
            _distPassed = (float) (1 - this.relPosition) * searchTarget.getLength();
        }

        if (_distPassed >= targetDist) {
            return Optional.empty();
        }

        return lookAhead(nextIdx, _distPassed, nthStreet + 1, targetDist);
    }
}
