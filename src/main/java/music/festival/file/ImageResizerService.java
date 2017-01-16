package music.festival.file;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by bryce_fisher on 1/13/17.
 */
@Service
public class ImageResizerService {

    int boundingBoxPixel = 1024;

    public byte[] scaleImage(byte[] bytes) throws IOException {
        InputStream in = new ByteArrayInputStream(bytes);
        BufferedImage bImageFromConvert = ImageIO.read(in);
        BufferedImage scaledInstance = getScaledInstance(bImageFromConvert, boundingBoxPixel, boundingBoxPixel, null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(scaledInstance, "png", out);
        return out.toByteArray();
    }


    public BufferedImage getScaledInstance(final BufferedImage img, final int targetWidth, final int targetHeight,
                                           final Object hint) {
        final int type = BufferedImage.TYPE_INT_ARGB;
        int drawHeight = targetHeight;
        int drawWidth = targetWidth;
        final int imageWidth = img.getWidth();
        final int imageHeight = img.getHeight();
        if ((imageWidth <= targetWidth) && (imageHeight <= targetHeight)) {
            return img;
        }
        final double sar = ((double) imageWidth) / ((double) imageHeight);
        if (sar != 0) {
            final double tar = ((double) targetWidth) / ((double) targetHeight);
            if ((Math.abs(tar - sar) > .001) && (tar != 0)) {
                final boolean isSoureWider = sar > (targetWidth / targetHeight);
                if (isSoureWider) {
                    drawHeight = (int) (targetWidth / sar);
                } else {
                    drawWidth = (int) (targetHeight * sar);
                }
            }
        }
        final BufferedImage result = new BufferedImage(drawWidth, drawHeight, type);
        try {
            final Graphics2D g2 = result.createGraphics();
            try {
                if (hint != null) {
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
                }
                g2.drawImage(img, 0, 0, drawWidth, drawHeight, null);
            } finally {
                g2.dispose();
            }
            return result;
        } finally {
            result.flush();
        }
    }

}
