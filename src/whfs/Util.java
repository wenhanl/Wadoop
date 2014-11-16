package whfs;

import config.Config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by wenhanl on 14-11-11.
 */
public class Util {
    public static void splitFile(String inputPath, String outputPath) {
        try {
            // Run split command to split file
            String splitCommand = "split -l" + +Config.BLOCK_SIZE + " ";
            splitCommand += inputPath + " ";
            splitCommand += outputPath;
            Process splitProcess = Runtime.getRuntime().exec(splitCommand);

            // Wait until this process finished running
            try {
                splitProcess.waitFor();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

            splitProcess.destroy();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void clearFiles(ArrayList<File> files) {
        for (File file : files) {
            file.delete();
        }
    }
}
