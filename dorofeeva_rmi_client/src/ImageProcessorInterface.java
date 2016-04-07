import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ImageProcessorInterface extends Remote {
    byte[] processImage(byte[] in, int w, int h, float[] matrix) throws RemoteException, IOException;
    int[] test(int[] ints) throws  RemoteException;
}
