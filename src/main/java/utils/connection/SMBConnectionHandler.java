package utils.connection;

import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;

import java.io.IOException;
import java.util.Objects;

import utils.connection.helpers.*;
import exception.connection.*;
import config.user_config.storage_config.StorageConfigModel;

public class SMBConnectionHandler implements ConnectionHandler {

    private static final int PORT = 445;
    private static final int CONNECT_TIMEOUT_MS = 5000;

    @Override
    public boolean test(StorageConfigModel config)  {
        try {
            SMBClient client = openConnection(
                    config.getHost(),
                    config.getUser(),
                    config.getPasswordPath() != null ? CredentialUtils.readPasswordPath(config.getPasswordPath()) : null,
                    config.getShare()
            );
            return closeConnection(client);
        } catch (Exception ex) {
            return false;
        }
    }

    public static SMBClient openConnection(String host, String username, String password, String share)
        throws IOException, Exception , HostUnreachableException {

        Objects.requireNonNull(host, "host is required");
        Objects.requireNonNull(share, "share is required");

        if (!NetworkUtils.isPortOpen(host, PORT, CONNECT_TIMEOUT_MS))
            throw new HostUnreachableException(host);

        SMBClient client = new SMBClient();

        try (Connection connection = client.connect(host)) {

            AuthenticationContext ac = new AuthenticationContext(
                    username != null ? username : "",
                    password != null ? password.toCharArray() : new char[0],
                    null
            );

            Session session = connection.authenticate(ac);

            try (DiskShare disk = (DiskShare) session.connectShare(share)) {
                disk.list("");  // test accessibility
                return client;
            } catch (Exception ex) {
                throw new Exception("Couldn't open share " + share + " on host " + host, ex);
            }

        } catch (IOException ex) {
            throw new Exception("Couldn't connect to remote SMB server", ex);
        } catch (Exception ex) {
            throw new Exception("Authentication failed", ex);
        }
    }

    public static boolean closeConnection(SMBClient client)  {
        try {
            if (client != null) client.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    } 
}

