package dhbw.porsche.business;

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
    private final Street[] streets;
    private List<IVehicle> vehicles;

    public StreetService(IFileService fileService) {
        this.streets = new Street[] {
                new Street(13.8f, new Point2D(0, 0), new Point2D(0, 1000)),
                new Street(36.1f, new Point2D(0, 1000), new Point2D(1000, 1000)),
                new Street(13f, new Point2D(1000, 1000), new Point2D(1000, 2000)),
                new Street(13f, new Point2D(1000, 1000), new Point2D(2000, 1000)),
                new Street(13f, new Point2D(2000, 1000), new Point2D(2000, 0))
        };
        this.vehicles = new ArrayList<>();
        this.vehicles.add(new Car(this, fileService, .68f, 5, 100, 40, generateSeed(), 0, 1));
        this.vehicles.add(new Car(this, fileService, 8.68f, 5, 100, 40, generateSeed(), .03, 1));
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

    @Override
    public List<Street> getStreets() {
        return List.of(this.streets);
    }

    @Override
    public List<IVehicle> getVehicles() {
        return this.vehicles;
    }

    private int[] generateSeed() {
        int[] seed = new int[10];
        for (int i = 0; i < seed.length; i++) {
            seed[i] = (int)(Math.random() * 10);
        }
        return seed;
    }
}
