package de.jonas.classifier.window;

import de.jonas.classifier.draw.DrawImage;
import de.jonas.classifier.image.ImageParser;
import de.jonas.classifier.main.ShapeClassifier;
import de.jonas.classifier.obj.Shape;
import de.jonas.classifier.sql.ImageManager;
import de.jonas.classifier.utils.MapManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

public class Window {

    //Frame
    private JFrame frame;
    private JFileChooser fileChooser;
    private BufferedImage toParse;

    //Image
    private String uploadedImageName;
    private HashMap<String, String> pixelMap;

    public Window() {
        open();
    }

    public void open() {
        this.frame = new JFrame();
        this.frame.setTitle("Shape Classifier");
        this.frame.setSize(800, 700);
        this.frame.setLayout(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addElements(this.frame);

        /*
        JLabel label = new JLabel();
        label.setSize(200, 200);
        label.setBorder(new LineBorder(Color.BLACK));
        Shape shape = ShapeClassifier.getInitializer().getData().getShapes().get(0);
        BufferedImage image = shape.drawImageFromPixelMap();
        ImageIcon icon = new ImageIcon(image);
        label.setIcon(icon);
        frame.add(label);
         */


        this.frame.setVisible(true);
    }

    public void addElements(JFrame frame) {

        DrawImage drawImage = new DrawImage(frame);


        //create file chooser
        this.fileChooser = new JFileChooser();
        //

        //text ares -> parse status text
        JLabel previewHeader = new JLabel("Parsed Image Status:");
        previewHeader.setBounds(512, 320, 200, 25);
        frame.add(previewHeader);

        JLabel previewParsedImg = new JLabel("No image parsed yet!");
        previewParsedImg.setBounds(512, 345, 250, 30);
        previewParsedImg.setBorder(new LineBorder(Color.GRAY));
        frame.add(previewParsedImg);
        //

        JButton loadFromDatabase = new JButton();
        loadFromDatabase.setBounds(612, 290, 150, 25);
        loadFromDatabase.setText("Load Database");
        loadFromDatabase.setLayout(null);
        loadFromDatabase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ImageManager().loadAllPixelImages();
            }
        });
        frame.add(loadFromDatabase);

        //add text
        JLabel selectedImgText = new JLabel("Ausgewähltes Bild:");
        selectedImgText.setBounds(550, 10, 200, 50);
        frame.add(selectedImgText);
        //

        //preview pane
        JLabel previewImgPane = new JLabel();
        previewImgPane.setBounds(512, 50, 200, 200);
        previewImgPane.setBorder(new LineBorder(Color.BLACK));
        frame.add(previewImgPane);
        //

        //add file choose button
        JButton chooseButton = new JButton();
        chooseButton.setBounds(10, 10, 100, 50);
        chooseButton.setText("Choose File");
        chooseButton.setLayout(null);
        chooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showOpenDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();

                    if (file.getName().endsWith(".png") || file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg")) {
                        System.out.println("Selected file to parse: " + file.getName());
                        uploadedImageName = file.getName();

                        try {
                            Image getImg = ImageIO.read(file);
                            toParse = new ImageParser().createBlackAndWhiteCopy(getImg, 200, 200);
                            ImageIcon icon = new ImageIcon(toParse);
                            System.out.println("setting up preview...");
                            previewImgPane.setIcon(icon);
                            frame.repaint();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }

                    } else {
                        JOptionPane.showMessageDialog(frame, "The selected file is no image!");
                    }

                }
            }
        });
        frame.add(chooseButton);
        //

        //add parse button
        JButton parseButton = new JButton();
        parseButton.setBounds(512, 260, 100, 25);
        parseButton.setText("Parse Image");
        parseButton.setLayout(null);
        parseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(toParse != null) {
                    pixelMap = new ImageParser().parseImageToPixelMap(toParse);
                    DecimalFormat format = new DecimalFormat("#.##");
                    previewParsedImg.setText("Done! Black amount: " + format.format(new ImageParser().getPercentageOfBlack(toParse)) + "%");
                    previewParsedImg.setBorder(new LineBorder(Color.GREEN));
                }else {
                    JOptionPane.showMessageDialog(frame, "No image selected!");
                }
            }
        });
        frame.add(parseButton);
        //

        //add database button
        JButton uploadDatabaseButton = new JButton();
        uploadDatabaseButton.setBounds(612, 260, 150, 25);
        uploadDatabaseButton.setText("Upload Database");
        uploadDatabaseButton.setLayout(null);
        uploadDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(pixelMap != null) {
                    new ImageManager().uploadImage(uploadedImageName, pixelMap);
                    JOptionPane.showMessageDialog(frame, "Upload done!");
                }else {
                    JOptionPane.showMessageDialog(frame, "No parsed image loaded!");
                }
            }
        });
        frame.add(uploadDatabaseButton);
        //

        //compare zone :)
        JLabel compareOne = new JLabel();
        compareOne.setBounds(10, 130, 200, 200);
        compareOne.setBorder(new LineBorder(Color.BLACK));
        frame.add(compareOne);
        JLabel underOne = new JLabel();
        underOne.setBounds(10, 330, 200, 25);
        underOne.setBorder(new LineBorder(Color.GRAY));
        underOne.setText("1. Übereinstimmung: n/A");
        frame.add(underOne);

        JLabel compareTwo = new JLabel();
        compareTwo.setBounds(230, 130, 200, 200);
        compareTwo.setBorder(new LineBorder(Color.BLACK));
        frame.add(compareTwo);
        JLabel underTwo = new JLabel();
        underTwo.setBounds(230, 330, 200, 25);
        underTwo.setBorder(new LineBorder(Color.GRAY));
        underTwo.setText("2. Übereinstimmung: n/A");
        frame.add(underTwo);

        JLabel compareThree = new JLabel();
        compareThree.setBounds(10, 380, 200, 200);
        compareThree.setBorder(new LineBorder(Color.BLACK));
        frame.add(compareThree);
        JLabel underThree = new JLabel();
        underThree.setBounds(10, 580, 200, 25);
        underThree.setBorder(new LineBorder(Color.GRAY));
        underThree.setText("3. Übereinstimmung: n/A");
        frame.add(underThree);

        JLabel compareFour = new JLabel();
        compareFour.setBounds(230, 380, 200, 200);
        compareFour.setBorder(new LineBorder(Color.BLACK));
        frame.add(compareFour);
        JLabel underFour = new JLabel();
        underFour.setBounds(230, 580, 200, 25);
        underFour.setBorder(new LineBorder(Color.GRAY));
        underFour.setText("4. Übereinstimmung: n/A");
        frame.add(underFour);

        ///Compare Button
        JButton compareCurrentUploaded = new JButton();
        compareCurrentUploaded.setBounds(10, 625, 100, 25);
        compareCurrentUploaded.setText("Classify!");
        compareCurrentUploaded.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(pixelMap != null) {
                    HashMap<BufferedImage, Double> sortedMap = new ImageParser().compare(pixelMap, pixelMap.size());
                    int count = 0;
                    for(BufferedImage image : sortedMap.keySet()) {
                        System.out.println("Setting up panels for image: " + count);
                        DecimalFormat decimalFormat = new DecimalFormat("#.#");
                        if(count == 0) {
                            compareOne.setIcon(new ImageIcon(image));
                            underOne.setText("1. Übereinstimmung: " + decimalFormat.format(sortedMap.get(image)) + "%");
                        }else if(count == 1) {
                            compareTwo.setIcon(new ImageIcon(image));
                            underTwo.setText("2. Übereinstimmung: " + decimalFormat.format(sortedMap.get(image)) + "%");
                        }else if(count == 2) {
                            compareThree.setIcon(new ImageIcon(image));
                            underThree.setText("3. Übereinstimmung: " + decimalFormat.format(sortedMap.get(image)) + "%");
                        }else if(count == 3) {
                            compareFour.setIcon(new ImageIcon(image));
                            underFour.setText("4. Übereinstimmung: " + decimalFormat.format(sortedMap.get(image)) + "%");
                        }
                        count++;
                    }

                    frame.repaint();
                }else {
                    JOptionPane.showMessageDialog(frame, "No parsed image loaded!");
                }
            }
        });
        frame.add(compareCurrentUploaded);
        ///

        //

        JButton drawnImageButton = new JButton();
        drawnImageButton.setBounds(512, 625, 100, 25);
        drawnImageButton.setText("Copy");
        drawnImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(drawImage.getDrawnImage() != null) {
                    previewImgPane.setIcon(new ImageIcon(drawImage.getDrawnImage()));
                    pixelMap = drawImage.getColorList();
                    toParse = drawImage.getDrawnImage();
                }
            }
        });
        frame.add(drawnImageButton);

        JButton drawnImageButtonClear = new JButton();
        drawnImageButtonClear.setBounds(622, 625, 100, 25);
        drawnImageButtonClear.setText("Clear");
        drawnImageButtonClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawImage.getColorList().clear();
                drawImage.getDrawOn().setIcon(null);
            }
        });
        frame.add(drawnImageButtonClear);
    }

}
