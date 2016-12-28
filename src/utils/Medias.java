package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Medias {
    private static Map<String,BufferedImage> IMAGES = new HashMap<>();
    private static Map<String,File> AUDIOS = new HashMap<>();

    static {
        try {
            File f = new File("medias");
            File[] fs = f.listFiles();
            for(File file : fs){
                if(file.getName().endsWith(".ogg") || file.getName().endsWith(".wav")){
                    AUDIOS.put(file.getName(),file);
                }else{
                    IMAGES.put(file.getName(), ImageIO.read(file));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static BufferedImage getImage(String fileName){
        return IMAGES.get(fileName);
    }

    public static File getAudio(String fileName){
        return AUDIOS.get(fileName);
    }
}
