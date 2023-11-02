package dhbw.porsche.business;

import dhbw.porsche.business.controller.PIController;
import dhbw.porsche.common.Point2D;
import dhbw.porsche.domain.Car;
import dhbw.porsche.domain.IVehicle;
import dhbw.porsche.domain.Street;
import dhbw.porsche.file.IFileService;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class StreetService implements IStreetService {
    /**
     * The streets.
     */
    private List<Street> streets;

    /**
     * The vehicles.
     */
    private List<IVehicle> vehicles;

    public StreetService(IFileService fileService) {
        this.streets = new ArrayList<>();
        this.streets.add(new Street(13.8f, new Point2D(0, 0), new Point2D(0, 1000)));
        this.streets.add(new Street(36.1f, new Point2D(0, 1000), new Point2D(1000, 1000)));
        this.streets.add(new Street(13f, new Point2D(1000, 1000), new Point2D(1000, 2000)));
        this.streets.add(new Street(13f, new Point2D(1000, 1000), new Point2D(2000, 1000)));
        this.streets.add(new Street(13f, new Point2D(2000, 1000), new Point2D(2000, 0)));

        this.vehicles = new ArrayList<>();
        this.vehicles.add(new Car(this, fileService, new PIController(0.5f, 0.1f), 8.68f, 5, 100, 63));
        this.vehicles.add(new Car(this, fileService, new PIController(0.5f, 0.1f), 8.68f, 5, 100, 63).translocate(0.03, 1));
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
     * Adds a street to the simulation.
     *
     * @param street The street to add.
     */
    @Override
    public void addStreet(Street street) {
        this.streets.add(street);
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
        return this.streets.get(id);
    }

    /**
     * Returns the amount of streets.
     * @return The amount of streets.
     */
	@Override
	public int getStreetAmount() {
        return this.streets.size();
	}

    /**
     * Returns the list of streets.
     * @return The list of streets.
     */
    @Override
    public List<Street> getStreets() {
        return this.streets;
    }

    /**
     * Returns the list of vehicles.
     * @return The list of vehicles.
     */
    @Override
    public List<IVehicle> getVehicles() {
        return this.vehicles;
    }
}
