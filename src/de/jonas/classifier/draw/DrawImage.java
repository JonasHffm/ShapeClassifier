package de.jonas.classifier.draw;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class DrawImage {

    private HashMap<String, String> colorList;
    private final int width = 200, height = 200;
    private BufferedImage drawnImage;

    private boolean mousePressed = false;

    private JFrame frame;

    private JLabel drawOn;

    public DrawImage(JFrame frame) {
        this.frame = frame;
        this.colorList = new HashMap<>();
        init();
    }

    public void init() {

        drawOn = new JLabel();
        drawOn.setBounds(512, 400, width, height);
        drawOn.setBorder(new LineBorder(Color.GRAY));
        drawOn.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if(mousePressed) {
                    System.out.println(e.getX() + ":" + e.getY());
                    colorList.put(e.getX() + ":" + e.getY(), "B");
                    drawnImage = drawImageFromPixelMap();
                    drawOn.setIcon(new ImageIcon(drawnImage));
                    drawOn.repaint();
                    frame.repaint();
                }
            }
        });
        drawOn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(!mousePressed) {
                    mousePressed = true;
                    System.out.println("ON");
                }else {
                    mousePressed = false;
                    System.out.println("OFF AND RESET");
                    mousePressed = false;
                    drawOn.setIcon(new ImageIcon(drawImageFromPixelMap()));
                    drawOn.repaint();
                    frame.repaint();
                }
            }

        });
        drawOn.setVisible(true);
        this.frame.add(drawOn);
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

    public HashMap<String, String> getColorList() {
        return colorList;
    }

    public void setColorList(HashMap<String, String> colorList) {
        this.colorList = colorList;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage getDrawnImage() {
        return drawnImage;
    }

    public void setDrawnImage(BufferedImage drawnImage) {
        this.drawnImage = drawnImage;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public JLabel getDrawOn() {
        return drawOn;
    }

    public void setDrawOn(JLabel drawOn) {
        this.drawOn = drawOn;
    }
}
