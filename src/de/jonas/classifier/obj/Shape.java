package de.jonas.classifier.obj;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Shape {

    private String imgName;
    private HashMap<String, String> colorList;

    private final int width = 200, height = 200;

    public Shape(String imgName) {
        this.imgName = imgName;
        this.colorList = new HashMap<>();
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public HashMap<String, String> getColorList() {
        return colorList;
    }

    public void setColorList(HashMap<String, String> colorList) {
        this.colorList = colorList;
    }

    public BufferedImage drawImageFromPixelMap() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                String colorType = colorList.get(x + ":" + y);
                if(colorType == null) {
                    image.setRGB(x, y, Color.WHITE.getRGB());
                }else {
                    image.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
        return image;
    }


    /*
        SHAPE STORAGE:

        x:y:BorW|...
     */
}
