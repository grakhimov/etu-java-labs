package javalabs.libraries;

import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;

import javafx.scene.image.*;
import org.jetbrains.annotations.NotNull;

public class Images {
    // Картинка в Blob
    public static Blob imageToMysqlBlob(ImageView photo) throws Exception{
        BufferedImage bImage = SwingFXUtils.fromFXImage(photo.getImage(), null);
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", s);
        byte[] res  = s.toByteArray();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(res);
        s.close();
        return blob;
    }
    // Файл в Blob
    @NotNull
    public static Blob readFileAsMysqlBlob(String filepath) throws Exception{
        File file = new File(filepath);
        byte[] bytesArray = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        int cp = fis.read(bytesArray);
        fis.close();
        return new javax.sql.rowset.serial.SerialBlob(bytesArray);
    }
}
