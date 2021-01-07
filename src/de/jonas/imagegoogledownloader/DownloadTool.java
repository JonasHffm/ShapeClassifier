package de.jonas.imagegoogledownloader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class DownloadTool {

    public static final String TYPE = "Kreis";

    public static void main(String[] args) {

        File file = new File("DatabaseDownloads/circles.txt");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            //Loading from file
            String link = "";
            int count = 1;
            while ((link = reader.readLine()) != null) {
                //Store images

                BufferedImage image = null;
                try {
                    URL url = new URL(link);
                    image = ImageIO.read(url);
                    if(image != null) {
                        if(link.endsWith(".png")) {
                            ImageIO.write(image, "png", new File("DatabaseDownloads/Pics/" + TYPE + "/" + TYPE + count + ".png"));
                        }else if(link.endsWith(".jpg")){
                            ImageIO.write(image, "jpg", new File("DatabaseDownloads/Pics/" + TYPE + "/" + TYPE + count + ".jpg"));
                        }
                    }
                } catch (IOException e) {
                    continue;
                }

                //Count +1
                count++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
        }

    }




}
