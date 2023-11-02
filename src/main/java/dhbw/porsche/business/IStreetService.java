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
     * Adds a street to the simulatioj.
     * @param street The street to add.
     */
    void addStreet(Street street);

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

    /**
     * Returns the amount of streets.
     * @return The amount of streets.
     */
    int getStreetAmount();

    /**
     * Returns the list of streets.
     * @return The list of streets.
     */
    List<Street> getStreets();

    /**
     * Returns the list of vehicles.
     * @return The list of vehicles.
     */
    List<IVehicle> getVehicles();
}
