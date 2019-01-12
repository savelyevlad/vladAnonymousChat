package justStuff;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class FileReader {

    public static String fileToString(String path) {
        StringBuilder ans = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new java.io.FileReader(new File(path)));
            String line;
            boolean isFirstIteration = true;
            while ((line = bufferedReader.readLine()) != null) {
                if(isFirstIteration) {
                    isFirstIteration = false;
                }
                else {
                    ans.append('\n');
                }
                ans.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ans.toString();
    }
}
