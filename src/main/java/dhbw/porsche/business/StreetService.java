package dhbw.porsche.business;

import dhbw.porsche.common.Point2D;
import dhbw.porsche.domain.Car;
import dhbw.porsche.domain.IVehicle;
import dhbw.porsche.domain.Street;
import lombok.Getter;

import java.util.List;

@Getter
public class StreetService implements IStreetService {
    private final Street[] streets;
    private List<IVehicle> vehicles;

    public StreetService() {
        this.streets = new Street[] {
                new Street(13.8f, new Point2D(0, 0), new Point2D(0, 100)),
                new Street(36.1f, new Point2D(0, 100), new Point2D(100, 100)),
                new Street(55.6f, new Point2D(100, 100), new Point2D(200, 100)),
                new Street(36.1f, new Point2D(200, 100), new Point2D(200, 0))
        };
        this.vehicles = List.of(
            new Car(this, 2, 2, 2, 40),
            new Car(this, 2, 2, 2, 40),
            new Car(this, 2, 2, 2, 40)
        );
    }

    /**
     * Adds a vehicle to the street.
     *
     * @param vehicle The vehicle to add.
     */
    @Override
    public void addVehicle(IVehicle vehicle) {
        this.vehicles.add(vehicle);
    }

    /**
     * Removes a vehicle from the street.
     *
     * @param vehicle The vehicle to remove.
     */
    @Override
    public void removeVehicle(IVehicle vehicle) {
        this.vehicles.remove(vehicle);
    }

    /**
     * Returns the street with the given id.
     *
     * @param id The id of the street.
     * @return The street with the given id.
     */
    @Override
    public Street getStreetById(int id) {
        return this.streets[id];
    }

	@Override
	public int getStreetAmount() {
        return this.streets.length;
	}
}
