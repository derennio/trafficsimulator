package dhbw.porsche;

import dhbw.porsche.business.IStreetService;
import dhbw.porsche.business.StreetService;
import dhbw.porsche.file.FileService;
import dhbw.porsche.file.IFileService;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Simulator {
    public IStreetService streetService;
    public IFileService fileService;
    private long lastTick;

    public Simulator() {
        this.fileService = new FileService();
        this.streetService = new StreetService(this.fileService);
        this.lastTick = System.currentTimeMillis();

        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
        service.scheduleAtFixedRate(() -> {
            try {
                this.tick();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }, 0, 10, TimeUnit.MILLISECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                fileService.saveData("data.csv");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, "Shutdown-thread"));
    }

    public void tick() {
        long now = System.currentTimeMillis();

        streetService.getVehicles().forEach(vehicle -> vehicle.move((float) (now - this.lastTick) / 1000));

        this.lastTick = System.currentTimeMillis();
    }
}
