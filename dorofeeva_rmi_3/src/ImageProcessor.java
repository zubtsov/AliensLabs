import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

public class ImageProcessor implements ImageProcessorInterface {
    @Override
    public byte[] processImage(byte[] in, int w, int h, float[] matrix) throws IOException, RemoteException {
        System.out.println("Arguments passed...");
        InputStream input = new ByteArrayInputStream(in);
        BufferedImage inputImage = ImageIO.read(input);
//        BufferedImageOp op = new ConvolveOp(new Kernel(w, h, matrix));

        //Processing
//        WritableRaster rast = inputImage.getRaster();
//        rast.setPixel(1,1, new int[] {0,0,0});
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        int[] rgbs = new int[width*height];
        inputImage.getRGB(0, 0, width, height, rgbs, 0, width);

        System.out.println("Size: " + rgbs.length);

        int rgbs2[] = Arrays.copyOf(rgbs, rgbs.length);
        for (int i = 1; i < height-1; i++) {
            for (int j = 1; j < width-1; j++) {
                int r=0,g=0,b=0;
                for (int k = -h / 2; k <= h / 2; k++) {
                    for (int l = -w / 2; l <= w / 2; l++) {
                        Color c = new Color(rgbs[(i+k)*width+j+l]);
                        r+=c.getRed()*matrix[(k+h/2)*w + l + w/2];
                        g+=c.getGreen()*matrix[(k+h/2)*w + l + w/2];
                        b+=c.getBlue()*matrix[(k+h/2)*w + l + w/2];
                    }
                }
                rgbs2[i*width + j] = 0xFF000000 | ((r << 16) & 0x00FF0000) | ((g << 8) & 0x0000FF00) | (b & 0x000000FF);
            }
        }

        inputImage.setRGB(0, 0, width, height, rgbs2, 0, width);

        BufferedImage outputImage = inputImage; //op.filter(inputImage, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(outputImage, "jpg", baos);
        baos.flush();
        byte[] imageInByte;
        imageInByte = baos.toByteArray();
        baos.close();
        return imageInByte;
    }

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        String name = "ImageProcessor";
        ImageProcessor imageProcessor = new ImageProcessor();
        ImageProcessorInterface stub = null;
        try {
            stub = (ImageProcessorInterface) UnicastRemoteObject.exportObject(imageProcessor, 0);
        } catch (RemoteException e) {
            System.out.println("Cannot get stub");
        }
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(1099);
            registry.rebind(name, stub);
        } catch (RemoteException e) {
            System.out.println("Cannot locate or rebind registry");
        }
        System.out.println("ImageProcessor is ready...");
    }
}
