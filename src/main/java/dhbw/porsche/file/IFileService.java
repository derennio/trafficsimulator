package dhbw.porsche.file;

import dhbw.porsche.domain.DataPoint;

import java.io.IOException;

public interface IFileService {
    /**
     * Appends the given data point to the file with the given name.
     * @param dataPoint The data point to append.
     */
    void appendData(DataPoint dataPoint);

    /**
     * Saves the given content to the file with the given name.
     * @param name The name of the file.
     */
    void saveData(String name) throws IOException;
}
