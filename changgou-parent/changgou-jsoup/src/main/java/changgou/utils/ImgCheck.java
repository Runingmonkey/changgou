package changgou.utils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.util.Iterator;

/**
 * @author mike ling
 * @description  检测图片完整性
 * @date 2021/6/23 11:50
 */
public class ImgCheck {

    private Boolean isJPEG(String fileName) {
        boolean canRead = false;
        try(ImageInputStream iis = ImageIO.createImageInputStream(new File(fileName))){
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("jpg");
            while (readers.hasNext()) {
                ImageReader reader = readers.next();
                reader.setInput(iis);
                reader.read(0);
                canRead = true;
                break;
            }
        }catch (Exception e){

        }
        return canRead;
    }

}
