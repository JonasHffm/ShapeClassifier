package de.jonas.classifier.utils;

import de.jonas.classifier.obj.Shape;

import java.util.ArrayList;

public class Data {

    private ArrayList<Shape> shapes;

    public Data() {
        this.shapes = new ArrayList<>();
    }

    public ArrayList<Shape> getShapes() {
        return shapes;
    }

    public void setShapes(ArrayList<Shape> shapes) {
        this.shapes = shapes;
    }
}
