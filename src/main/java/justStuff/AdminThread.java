package justStuff;

import botStuff.Bot;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class AdminThread implements Runnable {

    private final Scanner scanner = new Scanner(System.in);
    private final Bot bot;

    public AdminThread(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void run() {

        System.out.println("AdminThread was started");

        String line = scanner.nextLine();

        if(line.equals("/delete stickers")) {
            deleteStickers();
        }
        else if(line.substring(0, 6).equals("/adsimg")) {
            // TODO: ads
        }
        else if(line.substring(0, 3).equals("/ads")) {
            // TODO: ads
        }
        else if(line.substring(0, 4).equals("/send")) {
            // TODO: this shit
            bot.sendAll(line.substring(5, line.length() - 1));
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
