import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

public class ImageProcessorClient {
    public static void main(String[] args) throws RemoteException, NotBoundException, IOException {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        String name = "ImageProcessor";
        Registry registry = LocateRegistry.getRegistry(1099);
        ImageProcessorInterface processor = (ImageProcessorInterface) registry.lookup(name);

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(args[0]));
        } catch (IOException e) {
        }
        int w = Integer.parseInt(args[1]);
        int h = Integer.parseInt(args[2]);

        float[] matrix = new float[w*h];
        if (args.length == 4) {
            float f = Float.parseFloat(args[3]);
            Arrays.fill(matrix, f);
        } else {
            for (int i = 0; i < w * h; i++) {
                matrix[i]=Float.parseFloat(args[3+i]);
            }
        }
        byte[] tmp;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "jpg", baos);
        baos.flush();
        tmp = baos.toByteArray();
        baos.close();
        byte[] imageInByte = processor.processImage(tmp, w, h, matrix);
        InputStream in = new ByteArrayInputStream(imageInByte);
        BufferedImage result = ImageIO.read(in);
        try {
            ImageIO.write(result, "jpg", new File(args[0].substring(0, args[0].indexOf('.')) + "blurred.jpg"));
        } catch (IOException e) {
        }
    }
}
