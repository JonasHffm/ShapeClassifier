package de.jonas.classifier.image;

import de.jonas.classifier.main.ShapeClassifier;
import de.jonas.classifier.obj.Shape;
import de.jonas.classifier.sql.ImageManager;
import de.jonas.classifier.utils.MapManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class ImageParser {

    private Image image;
    public HashMap<Shape, Boolean> shapeIsComparing = new HashMap<>();


    public ImageParser(Image image) {
        this.image = image;
    }

    public ImageParser() {
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public HashMap<String, String> parseImageToPixelMap(BufferedImage image) {
        HashMap<String, String> list = new HashMap<>();

        //parse
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                final int clr = image.getRGB(x, y);
                final int red = (clr & 0x00ff0000) >> 16;
                final int green = (clr & 0x0000ff00) >> 8;
                final int blue = clr & 0x000000ff;

                // Color Red get cordinates
                if (red == 0 && green == 0 && blue == 0) {
                    //System.out.println(String.format("Black Color -> Coordinate %d %d", x, y));
                    list.put(x + ":" + y, "B");
                } //else {
                //System.out.println("X: " + x + "Y: " + y + "----->> " + "R: " + red + " G: " + green + " B: " + blue);
                //list.put(x + ":" + y, "W");
                //}
            }
        }
        return list;
    }

    public HashMap<String, String> parseImageToPixelMapBlackAndWhite(BufferedImage image) {
        HashMap<String, String> list = new HashMap<>();

        //parse
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                final int clr = image.getRGB(x, y);
                final int red = (clr & 0x00ff0000) >> 16;
                final int green = (clr & 0x0000ff00) >> 8;
                final int blue = clr & 0x000000ff;

                // Color Red get cordinates
                if (red == 0 && green == 0 && blue == 0) {
                    //System.out.println(String.format("Black Color -> Coordinate %d %d", x, y));
                    list.put(x + ":" + y, "B");
                } else {
                    //System.out.println("X: " + x + "Y: " + y + "----->> " + "R: " + red + " G: " + green + " B: " + blue);
                    list.put(x + ":" + y, "W");
                }
            }
        }
        return list;
    }

    public String parseImageToPixelStr(BufferedImage image) {
        StringBuilder pixelStr = new StringBuilder();

        //parse
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                final int clr = image.getRGB(x, y);
                final int red = (clr & 0x00ff0000) >> 16;
                final int green = (clr & 0x0000ff00) >> 8;
                final int blue = clr & 0x000000ff;

                // Color Red get cordinates
                if (red == 0 && green == 0 && blue == 0) {
                    //System.out.println(String.format("Black Color -> Coordinate %d %d", x, y));
                    //list.put(x + ":" + y, "B");
                    pixelStr.append(x).append(":").append(y).append(";B|");
                }
            }
        }
        return pixelStr.toString();
    }

    public BufferedImage createResizedCopy(Image originalImage,
                                           int scaledWidth, int scaledHeight,
                                           boolean preserveAlpha) {
        System.out.println("resizing...");
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }

    public BufferedImage createBlackAndWhiteCopy(Image originalImage,
                                                 int scaledWidth, int scaledHeight) {
        System.out.println("repainting black and white...");
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g = scaledBI.createGraphics();
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }

    public double getPercentageOfBlack(BufferedImage image) {
        if (image != null) {
            HashMap<String, String> colorList = parseImageToPixelMapBlackAndWhite(image);
            int amountBlack = 0, pixelMax = 0;
            double percentage = 0;
            for (String pixel : colorList.keySet()) {
                pixelMax++;
                if (colorList.get(pixel).equals("B")) {
                    amountBlack++;
                }
            }
            percentage = (double) amountBlack / (double) pixelMax * 100;
            return percentage;
        }
        return 0;
    }

    public HashMap<BufferedImage, Double> compare(HashMap<String, String> colorMap, int maxPixels) {
        HashMap<BufferedImage, Double> comparedMap = new HashMap<>();
        System.out.println();
        System.out.println("Start comparing algorithm...");
        HashMap<String, Integer> overlap = new HashMap<>();
        HashMap<Double, Shape> imgToShape = new HashMap<>();
        for(Shape shape : ShapeClassifier.getInitializer().getData().getShapes()) {
            shapeIsComparing.put(shape, false);

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if(!shapeIsComparing.get(shape)) {
                        shapeIsComparing.put(shape, true);
                        //System.out.println("Comparing shape: " + shape.getImgName());
                        System.out.println();
                        int shapeOverlap = 0;
                        for (String cords : shape.getColorList().keySet()) {
                            for (String inCords : colorMap.keySet()) {
                                if (cords.equals(inCords)) {
                                    if (shape.getColorList().get(cords).equals(colorMap.get(inCords))) {
                                        shapeOverlap++;
                                    }
                                }
                            }
                        }
                        //System.out.println("Done comparing shape: " + shape.getImgName() + "    Shape overlap: " + shapeOverlap + " pixel");
                        overlap.put(shape.getImgName(), shapeOverlap);
                        imgToShape.put((double)overlap.get(shape.getImgName())/maxPixels*100, shape);
                        shapeIsComparing.remove(shape);
                        this.cancel();
                    }
                }
            }, 1, 1);

        }

        while (shapeIsComparing.size() != 0) {
            System.out.println("Processing...");
        }

        //HashMap<String, Integer> sortedOverlap = (HashMap<String, Integer>) new MapManager().sortByComparator(overlap, false);
        for(String shape : overlap.keySet()) {
            comparedMap.put(getShapeFromImage(shape).drawImageFromPixelMap(), (double)overlap.get(shape)/maxPixels*100);
        }

        HashMap<BufferedImage, Double> results = (HashMap<BufferedImage, Double>) new MapManager().sortByComparatorFinal(comparedMap, false);

        System.out.println(results.values().toArray()[0]);
        System.out.println(imgToShape.containsKey((double)results.values().toArray()[0]));
        System.out.println(imgToShape.get((double)results.values().toArray()[0]));
        if(imgToShape.get((double)results.values().toArray()[0]) != null) {
            String type = imgToShape.get((double)results.values().toArray()[0]).getImgName();
            DecimalFormat df = new DecimalFormat("#.##");
            if(type.contains("triangle")) {
                ShapeClassifier.getInitializer().getWindow().getResult().setText("Result: Triangle (" + df.format((double)results.values().toArray()[0]) + "%)");
            }else if(type.contains("square")) {
                ShapeClassifier.getInitializer().getWindow().getResult().setText("Result: Square (" + df.format((double)results.values().toArray()[0]) + "%)");
            }else if(type.contains("circle")) {
                ShapeClassifier.getInitializer().getWindow().getResult().setText("Result: Circle (" + df.format((double)results.values().toArray()[0]) + "%)");
            }
            ShapeClassifier.getInitializer().getWindow().getFrame().repaint();
        }


        return results;
    }


    public Shape getShapeFromImage(String shapename) {
        for(Shape shape : ShapeClassifier.getInitializer().getData().getShapes()) {
            if(shape.getImgName().equalsIgnoreCase(shapename)) {
                return shape;
            }
        }
        return null;
    }

}
