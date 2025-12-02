package utils.connection;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.util.Objects;

import utils.connection.helpers.CredentialUtils;
import utils.connection.helpers.NetworkUtils;
import exception.connection.*;
import config.user_config.storage_config.StorageConfigModel;

public class SFTPConnectionHandler implements ConnectionHandler {

    private static final int DEFAULT_PORT = 22;
    private static final int CONNECT_TIMEOUT_MS = 5000;

    @Override
    public boolean test(StorageConfigModel config) {
        try {
            String password;
            Session session = null;

            if (config.getPasswordPath() != null ){
                password = CredentialUtils.readPasswordPath(config.getPasswordPath());
                session = getSessionByPassword(config.getUser(), config.getHost(), config.getPort(), password);
            }
            else 

                session = getSessionByPrivateKeyPassKey(config.getUser(), config.getHost(), config.getPrivateKeyPath(),config.getPassPhrase(), config.getPort());
           
            session.disconnect();
   
            return true;
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
            return false;
        }
    }

    public static Session getSessionByPassword(String username, String host, int port, String password)
            throws HostUnreachableException, ConnectionTestException {
            try {
                Objects.requireNonNull(host, "host is required");

                JSch jsch = new JSch();
                int realPort = (port <= 0) ? DEFAULT_PORT : port;

                NetworkUtils.validateHostReachable(host, CONNECT_TIMEOUT_MS);

                if (!NetworkUtils.isPortOpen(host, realPort, CONNECT_TIMEOUT_MS)) {
                    throw new HostUnreachableException(host + "here");
                }

                Session session = jsch.getSession(username != null ? username : "", host, realPort);
                session.setPassword(password);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect(CONNECT_TIMEOUT_MS);
                return session;

            } catch (JSchException ex) {
                throw new ConnectionTestException("Failed to connect to host: " + host, ex);
            }
    }

    public static Session getSessionByPrivateKeyPassKey( String username, String host, String privateKeyPath, String passPhrase, int port) 
            throws ConnectionTestException, HostUnreachableException {

            try {
                Objects.requireNonNull(username, "username can't be empty");
                Objects.requireNonNull(host, "host can't be empty");
                Objects.requireNonNull(privateKeyPath, "private key path can't be empty");

                int realPort = port <= 0 ? DEFAULT_PORT : port;

                NetworkUtils.validateHostReachable(host, CONNECT_TIMEOUT_MS);
                if (!NetworkUtils.isPortOpen(host, realPort, CONNECT_TIMEOUT_MS)) {
                    throw new HostUnreachableException(host);
                }

                if (!CredentialUtils.fileExists(privateKeyPath)) {
                    throw new ConnectionTestException("Private key file path doesn't exist");
                }

                JSch jsch = new JSch();

                if (passPhrase == null) {
                    jsch.addIdentity(privateKeyPath);
                } else {
                    jsch.addIdentity(privateKeyPath, passPhrase);
                }
                Session session = jsch.getSession(username, host, realPort);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect(CONNECT_TIMEOUT_MS);

                return session;

            } catch (JSchException ex) {
                throw new ConnectionTestException("Couldn't create session with host: " + host + ":\t" + ex, ex);
            } 
    }

    public static ChannelExec openExecChannel(Session session) throws ConnectionTestException {
        try {
            Objects.requireNonNull(session, "session is required");
            return (ChannelExec) session.openChannel("exec");
        } catch (JSchException ex) {
            throw new ConnectionTestException("Failed to open exec channel.", ex);
        }
    }
}

