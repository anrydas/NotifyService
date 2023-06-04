package das.tools.notifier.notify.entitys.img;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Slf4j
public class ImageWH {
    private static ImageWH instance;
    private int w = 400;
    private int h = 300;

    public static ImageWH getInstance() {
        if (instance == null) {
            instance = new ImageWH();
        }
        return instance;
    }

    private ImageWH() {
    }

    public WH calculateFor(File file) {
        try {
            BufferedImage bi = ImageIO.read(file);
            this.w = bi.getWidth();
            this.h = bi.getHeight();
            if (this.w > 1500) this.w = 1500;
            if (this.h > 1100) this.w = 1100;
        } catch (IOException e) {
            log.error("Error loading BufferedImage from {}", file.getAbsoluteFile());
        }
        return new WH(this.w, this.h);
    }
}