package dhbw.porsche.business;

import dhbw.porsche.domain.IVehicle;
import dhbw.porsche.domain.Street;

import java.util.List;

public interface IStreetService {
    /**
     * Adds a vehicle to the street.
     * @param vehicle The vehicle to add.
     */
    void addVehicle(IVehicle vehicle);

    /**
     * Removes a vehicle from the street.
     * @param vehicle The vehicle to remove.
     */
    void removeVehicle(IVehicle vehicle);

    /**
     * Returns the street with the given id.
     * @param id The id of the street.
     * @return The street with the given id.
     */
    Street getStreetById(int id);
    int getStreetAmount();

    List<Street> getStreets();

    List<IVehicle> getVehicles();
}
