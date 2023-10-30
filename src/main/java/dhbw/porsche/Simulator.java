package dhbw.porsche;

import dhbw.porsche.business.StreetService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Simulator {
    public StreetService streetService;
    private long lastTick;

    public Simulator() {
        streetService = new StreetService();

        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
        service.scheduleAtFixedRate(this::tick, 0, 10, TimeUnit.MILLISECONDS);
    }

    public void tick() {
        long now = System.currentTimeMillis();

        streetService.getVehicles().forEach(vehicle -> {
            vehicle.move((now - this.lastTick) * 1000);
        });

        this.lastTick = System.currentTimeMillis();
    }
}
