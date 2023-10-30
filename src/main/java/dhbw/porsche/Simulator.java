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
        this.lastTick = System.currentTimeMillis();

        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
        service.scheduleAtFixedRate(() -> {
            try {
                this.tick();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }, 0, 10, TimeUnit.MILLISECONDS);
    }

    public void tick() {
        long now = System.currentTimeMillis();

        streetService.getVehicles().forEach(vehicle -> vehicle.move((float) (now - this.lastTick) / 1000));

        this.lastTick = System.currentTimeMillis();
    }
}
