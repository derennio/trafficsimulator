package dhbw.porsche.domain;

import dhbw.porsche.business.IStreetService;
import dhbw.porsche.business.controller.PIController;
import dhbw.porsche.common.Point2D;
import dhbw.porsche.common.Tuple;
import dhbw.porsche.file.FileService;
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

    private PIController controller = new PIController(0.1f, 0.1f);
    
    /**
     *  the car's current velocity.
     */
    private float velocity;

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
     * Index of the current street.
     */
    private int streetIdx = 0;

    /**
     * position relative to street length (0.0 - 1.0).
     */
    private double relPosition = 0.0d;

    private final int[] seed;

    private int seedIdx;

    public Car(IStreetService streetService, IFileService fileService, float maxAccel, float maxBrake, float maxVelocity, int length, int[] seed, double relAhead, int streetIdx) {
        this.streetService = streetService;
        this.fileService = fileService;
        this.maxAccel = maxAccel;
        this.maxBrake = maxBrake;
        this.maxVelocity = maxVelocity;
        this.length = length;
        this.seed = seed;
        this.seedIdx = 0;
        this.controller = new PIController(0.1f, 0.1f);
        this.relPosition = relAhead;
        this.streetIdx = streetIdx;
    }


    /**
     * Updates the velocity of the vehicle based on the controller's instruction.
     */
    @Override
    public void updateVelocity(float deltaT) {
        float desiredDist = this.velocity * 3.6f / 2 + this.getLength();
        var ahead = lookAhead(desiredDist);
        float error = 0, control = 0, dist = -1;

        if (ahead.isPresent()) {
            var v = ahead.get().t();
            dist = ahead.get().v();
            error = (float)(dist - desiredDist);
            control = this.controller.calculate(error, deltaT);
            if (control > 0) {
                this.velocity += Math.min(control, this.maxAccel * deltaT);
                //this.velocity = Math.min(this.velocity + control, Math.min(this.streetService.getStreetById(streetIdx).vMax(), this.maxVelocity));
            } else {
                this.velocity -= Math.min(-control, this.maxBrake * deltaT);
            }
        } else {
            this.velocity = Math.min(this.velocity + this.maxAccel * deltaT, Math.min(this.streetService.getStreetById(streetIdx).vMax(), this.maxVelocity));
        }

        DataPoint dp = new DataPoint(this.getId(), System.currentTimeMillis(), this.velocity, error, control, this.streetService.getStreetById(streetIdx).vMax(), (float)this.relPosition, dist, this.streetIdx);
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

    private Optional<Tuple<IVehicle, Float>> lookAhead(float targetDist) {
        return lookAhead(this.streetIdx, 0, 0, targetDist);
    }

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
            _distPassed = (float)((1 - this.relPosition) * searchTarget.getLength());
        }

        if (_distPassed >= targetDist) {
            return Optional.empty();
        }

        return lookAhead(nextIdx, _distPassed, nthStreet + 1, targetDist);
    }
}
