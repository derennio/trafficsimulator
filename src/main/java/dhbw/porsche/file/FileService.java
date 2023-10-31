package dhbw.porsche.file;

import dhbw.porsche.domain.DataPoint;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileService implements IFileService {
    private List<DataPoint> dataPoints = new ArrayList<>();

    /**
     * Appends the given data point to the file with the given name.
     *
     * @param dataPoint The data point to append.
     */
    @Override
    public synchronized void appendData(DataPoint dataPoint) {
        this.dataPoints.add(dataPoint);
    }

    /**
     * Saves the given content to the file with the given name.
     *
     * @param name The name of the file.
     */
    @Override
    public synchronized void saveData(String name) throws IOException {
        FileWriter fw = new FileWriter(name, true);
        BufferedWriter bw = new BufferedWriter(fw);

        for (DataPoint dataPoint :
             this.dataPoints) {
            bw.write(String.format("%s,%d,%f,%f,%f,%f,%f,%f,%d",
                    dataPoint.id(),
                    dataPoint.time(),
                    dataPoint.velocity(),
                    dataPoint.error(),
                    dataPoint.control(),
                    dataPoint.vMax(),
                    dataPoint.relPosition(),
                    dataPoint.distance(),
                    dataPoint.streetIdx()));
            bw.newLine();
        }
        bw.close();
    }
}
