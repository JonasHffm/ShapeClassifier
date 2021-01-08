package de.jonas.classifier.main;

import de.jonas.classifier.sql.ImageManager;
import de.jonas.classifier.utils.Data;
import de.jonas.classifier.utils.MapManager;
import de.jonas.classifier.utils.MySQL;
import de.jonas.classifier.window.Window;

import java.util.HashMap;

public class ShapeClassifier implements Initializer{

    public static ShapeClassifier initializer;

    private Data data;
    private Window window;
    private MySQL mySQL;

    public static void main(String[] args) {
        initializer = new ShapeClassifier();
        initializer.init();
    }

    @Override
    public void init() {
        initializer.data = new Data();
        initializer.mySQL = new MySQL("localhost", "shapeclassifier", "root", "");

        //create database
            initializer.mySQL.update("CREATE TABLE IF NOT EXISTS Shapes(image VARCHAR(100), shape LONGTEXT)");
        //

        //UPLOAD TO DATABASE -> FOLDER (DatabaseDownloads/data)
        //new ImageManager().uploadFolderToDatabase();

        new ImageManager().loadAllPixelImages();
        System.out.println("All shapes : " + data.getShapes());

        initializer.window = new Window();
    }

    public static ShapeClassifier getInitializer() {
        return initializer;
    }

    public static void setInitializer(ShapeClassifier initializer) {
        ShapeClassifier.initializer = initializer;
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public void setMySQL(MySQL mySQL) {
        this.mySQL = mySQL;
    }
}
