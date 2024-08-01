package RangerCaptain.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FusionGifCreator {
    public static final boolean SHOULD_PROCESS = false;
    private static final String SPRITE_PATH = "F:\\Downloads\\GDRE_tools-v0.6.2-windows\\CB Unpack\\sprites\\";

    public static void process() {
        File frameData = new File("TSCNOutput.log");
        try {
            Scanner scanner = new Scanner(frameData);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().replaceAll(" ","").replaceAll("\"","").replaceAll("Rect2\\(","").replaceAll("\\)","");
                String[] chunks = line.split(",");
                File imageFile = new File(SPRITE_PATH+chunks[0]);
                int passes = (chunks.length-1)/4;
                boolean setTransparent = false;
                BufferedImage spriteSheet = ImageIO.read(imageFile);
                AnimatedGifEncoder age = new AnimatedGifEncoder();
                age.start(chunks[0].replaceAll("\\.png",".gif"));
                age.setRepeat(0);
                age.setDelay(100);
                for (int i = 0 ; i < passes ; i++) {
                    BufferedImage croppedImage = spriteSheet.getSubimage(Integer.parseInt(chunks[1+i*4]), Integer.parseInt(chunks[2+i*4]), Integer.parseInt(chunks[3+i*4]), Integer.parseInt(chunks[4+i*4]));
                    BufferedImage filledImage = new BufferedImage(croppedImage.getWidth(), croppedImage.getHeight(), croppedImage.getType());
                    Image toPaint = bufferedImageToImage(croppedImage);
                    Color unused = getUnusedColor(croppedImage);
                    for (int x = 0 ; x < filledImage.getWidth(); x++) {
                        for (int y = 0 ; y < filledImage.getHeight(); y++) {
                            filledImage.setRGB(x, y, unused.getRGB());
                        }
                    }
                    Graphics2D g2 = filledImage.createGraphics();
                    g2.drawImage(toPaint, 0, 0, null);
                    g2.dispose();
                    if (!setTransparent) {
                        setTransparent = true;
                        age.setTransparent(unused);
                    }
                    //ImageIO.write(filledImage,"png",new File("test"+i+".png"));
                    age.addFrame(filledImage);
                }
                age.finish();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Image bufferedImageToImage(BufferedImage bufferedImage) {
        ImageProducer ip = new FilteredImageSource(bufferedImage.getSource(), new RGBImageFilter() {
            @Override
            public int filterRGB(int x, int y, int rgb) {
                return rgb;
            }
        });
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    private static BufferedImage imageToBufferedImage(Image image) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return bufferedImage;
    }

    private static Color getUnusedColor(BufferedImage bufferedImage) {
        Color testColor = new Color(0xCACBCC);
        int[] pixels = bufferedImage.getRGB( 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());
        for (int i : pixels) {
            if (i == testColor.getRGB()) {
                throw new RuntimeException("Oops");
            }
        }
        return testColor;
    }
}
