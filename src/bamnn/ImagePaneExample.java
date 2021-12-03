/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bamnn;

/**
 *
 * @author gers
 */
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

public class ImagePaneExample {

    public static void main(String[] args) {
        new ImagePaneExample();
    }

    public ImagePaneExample() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                JFileChooser fileChooser = new JFileChooser(System.getProperty("images")); //escogemos nuestro archivo para pargarlo

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {

                        BufferedImage img = ImageIO.read(file);
                        ImagePane imgPane = new ImagePane();
                        imgPane.setImage(img);
                        imgPane.setRounded(true);
                        imgPane.setBorder(new EmptyBorder(20, 20, 20, 20));

                        JFrame frame = new JFrame("Testing");
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.setLayout(new BorderLayout());
                        frame.add(imgPane);
                        frame.pack();
                        frame.setLocationRelativeTo(null);
                        frame.setVisible(true);

                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }
                }
            }

        }
        );
    }

   
}
