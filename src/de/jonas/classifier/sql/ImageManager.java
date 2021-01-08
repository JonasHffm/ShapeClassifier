package de.jonas.classifier.sql;

import de.jonas.classifier.image.ImageParser;
import de.jonas.classifier.main.ShapeClassifier;
import de.jonas.classifier.obj.Shape;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ImageManager {

    public void uploadImage(String imageName, HashMap<String, String> shapePixelMap) {
        StringBuilder shapeStr = new StringBuilder();
        for(String shapeXY : shapePixelMap.keySet()) {
            shapeStr.append(shapeXY).append(";").append(shapePixelMap.get(shapeXY)).append("|");
        }
        //System.out.println(shapeStr.toString());
        ShapeClassifier.initializer.getMySQL().update("INSERT INTO Shapes(image, shape) VALUES ('" + imageName + "', '" + shapeStr.toString() + "')");
    }

    public String getNameFromPixelMap(String pixelMap) {
        ResultSet rs = ShapeClassifier.initializer.getMySQL().getResults("SELECT * FROM Shapes WHERE shape = '" + pixelMap + "'");
        try {
            while (rs.next()) {
                return rs.getString("image");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void loadAllPixelImages() {
        ShapeClassifier.initializer.getData().getShapes().clear();

        ResultSet rs = ShapeClassifier.initializer.getMySQL().getResults("SELECT * FROM Shapes");
        System.out.println("Start loading shapes!");
        try {
            while (rs.next()) {
                System.out.println();
                String name = rs.getString("image");
                String shapeStr = rs.getString("shape");
                String[] shapeStrArr = shapeStr.split("\\|");
                int count = 0;
                System.out.println("Start parsing image... -> " + name);
                HashMap<String, String> pixelMap = new HashMap<>();

                for(int i = 0; i < shapeStrArr.length; i++) {
                    String cords = shapeStrArr[i].split(";")[0];
                    String BorW = shapeStrArr[i].split(";")[1];
                    pixelMap.put(cords, BorW);
                    count++;
                }

                System.out.println("Done!");
                System.out.println("  -> " + name + " -- " + count + " pixels");
                Shape shape = new Shape(name);
                shape.setColorList(pixelMap);
                ShapeClassifier.initializer.getData().getShapes().add(shape);
                System.out.println("Added new shape! -> " + name);
                System.out.println();
            }
            System.out.println("All loaded shapes : " + ShapeClassifier.initializer.getData().getShapes());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void uploadFolderToDatabase() {
        File file = new File("DatabaseDownloads/data");
        if(file.exists()) {
        for(File fileList : file.listFiles()) {
            try {
                System.out.println("Upload Folder -> " + fileList.getName() + " to database...");
                Image image = ImageIO.read(fileList);
                BufferedImage toUpload = new ImageParser().createBlackAndWhiteCopy(image, 200, 200);
                uploadImage(fileList.getName().replace(".png", ""), new ImageParser().parseImageToPixelMap(toUpload));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        }
    }

}
