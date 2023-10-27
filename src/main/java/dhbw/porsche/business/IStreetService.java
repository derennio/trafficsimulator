package dhbw.porsche.business;

import dhbw.porsche.domain.IVehicle;

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
}
