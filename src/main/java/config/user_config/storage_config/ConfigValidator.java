package config.user_config.storage_config;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import exception.connection.ConnectionTestException;


public class ConfigValidator {

    public static void validate(StorageConfigModel m) throws IllegalArgumentException , ConnectionTestException {

        StorageConfigModel.Type type = m.getType();

        String path = m.getPath();
        String mountPoint = m.getMountPoint();
        String host = m.getHost();
        String share = m.getShare();
        String user = m.getUser();
        String passwordPath = m.getPasswordPath();
        String remotePath = m.getRemotePath();
        String localMount = m.getLocalMount();
        String privateKey = m.getPrivateKeyPath(); 
        int port = m.getPort();

        if (type == StorageConfigModel.Type.LOCAL) {
            if (isBlank(path) || !isValidPath(path))
                throw new IllegalArgumentException("Invalid path: " + path);
        } 

        if (type == StorageConfigModel.Type.EXTERNAL || type == StorageConfigModel.Type.PARTITION) {
            if (isBlank(mountPoint) || !isValidPath(mountPoint))
                throw new IllegalArgumentException("Invalid mountPoint: " + mountPoint);
        }

        if (type == StorageConfigModel.Type.SMB) {
            if (isBlank(share))
                throw new IllegalArgumentException("SMB requires a share name.");
        }

        if (type == StorageConfigModel.Type.SMB || type == StorageConfigModel.Type.SFTP) {
            if (isBlank(user))
                throw new IllegalArgumentException("User cannot be empty.");
        }

        if (type == StorageConfigModel.Type.NFS) {
            if (isBlank(remotePath))
                throw new IllegalArgumentException("remotePath is required.");
            if (isBlank(localMount) || !isValidPath(localMount))
                throw new IllegalArgumentException("Invalid localMount: " + localMount);
        }

        if (type == StorageConfigModel.Type.SFTP) {
            if (!isBlank(privateKey) && !isValidPath(privateKey))
                throw new IllegalArgumentException("Invalid privateKey: " + privateKey);
            if (port <= 0)
                throw new IllegalArgumentException("Invalid port: " + port);
        }

        if (type == StorageConfigModel.Type.SMB ||
            type == StorageConfigModel.Type.NFS ||
            type == StorageConfigModel.Type.SFTP) {
            if (isBlank(host))
                throw new IllegalArgumentException("Host cannot be empty.");
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    private static boolean isValidPath(String path) throws ConnectionTestException { 
        return path != null && Files.exists(Paths.get(path));
    }
}

