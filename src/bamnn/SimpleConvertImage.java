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
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class SimpleConvertImage {

    public static void main(String[] args) throws IOException {
        cargar();
        String dirName = "C:\\";
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
        BufferedImage img = ImageIO.read(new File(dirName, "rose.jpg"));
        ImageIO.write(img, "jpg", baos);
        baos.flush();

        String base64String = Base64.encode(baos.toByteArray());
        baos.close();

        byte[] bytearray = Base64.decode(base64String);

        BufferedImage imag = ImageIO.read(new ByteArrayInputStream(bytearray));
        ImageIO.write(imag, "jpg", new File(dirName, "snap.jpg"));
    }

    public static void cargar() {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("øimages")); //escogemos nuestro archivo para pargarlo

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            FileReader archivos;

            try {
                archivos = new FileReader(file);

                try (BufferedReader lee = new BufferedReader(archivos)) {

                } catch (IOException ex) {
                }
            } catch (FileNotFoundException ex) {
            }
        }
    }
}
