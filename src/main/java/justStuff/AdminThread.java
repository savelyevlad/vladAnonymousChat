package justStuff;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class AdminThread implements Runnable {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {

        System.out.println("AdminThread was started");

        String line = scanner.nextLine();

        if(line.equals("/delete stickers")) {
            deleteStickers();
        }
    }

    private void deleteStickers() {
        try {
            File file = new File("stickers");
            FileUtils.deleteDirectory(file);
            if(!file.mkdir()) {
                throw new IOException("directory was not created");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("stickers were deleted!");
    }
}
