package mapr;

/**
 * Created by CGJ on 14-11-12.
 */
import java.io.Serializable;

public abstract class MapReduceJob<Kin, Vin, Kout, Vout> implements Serializable {

    private String inputFile;

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    abstract public Mapper<Kin, Vin, Kout, Vout> getMapper();

    abstract public Reducer<Kout, Vout, Kout, Vout> getReducer();
}