package javalabs.libraries;

import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import javax.swing.text.html.ImageView;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;

import javafx.scene.image.*;

public class Images {
    public static Blob imageToBlob(javafx.scene.image.ImageView photo) throws Exception{
        BufferedImage bImage = SwingFXUtils.fromFXImage(photo.getImage(), null);
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", s);
        byte[] res  = s.toByteArray();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(res);
        s.close();
        return blob;
    }
}
