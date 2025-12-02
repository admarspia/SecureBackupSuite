package utils.connection.helpers;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import exception.connection.HostUnreachableException;

public class NetworkUtils {

    private NetworkUtils() {}

    public static void validateHostReachable(String host, int timeout) throws HostUnreachableException {
        try {
            InetAddress.getByName(host); 
        } catch (UnknownHostException ex) {
            throw new HostUnreachableException(host, ex);
        }

        try (Socket s = new Socket()) {
            s.connect(new InetSocketAddress(host, 2222), timeout);
        } catch (IOException ex) {
            throw new HostUnreachableException(host, ex);
        }
    }

    public static boolean isPortOpen(String host, int port, int timeout) {
        try (Socket s = new Socket()) {
            s.connect(new InetSocketAddress(host, port), timeout);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}

