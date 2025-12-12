package storage;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.IOException;
import java.nio.file.Path;

import config.user_config.storage_config.StorageConfigModel;
import utils.connection.SFTPConnectionHandler;
import exception.connection.ConnectionTestException;

public class SFTPStorageWriter implements StorageWriter {

    @Override
    public void write(StorageConfigModel config, Path encryptedFile) throws IOException {
        Session session = null;
        ChannelSftp sftp = null;

        try {
            session = createSession(config);
            System.out.println("seession created");
            try {
            sftp = (ChannelSftp) session.openChannel("sftp");
            
            sftp.connect();

            } catch (Exception ex){
                System.out.println("Failed to open channel: " + ex );
                throw ex;
            }
            System.out.println("channel opened");
            String remoteDir = config.getPath(); 
            ensureRemoteDirectory(remoteDir, sftp);

            String remoteFile = remoteDir + "/" + encryptedFile.getFileName().toString();

            sftp.put(encryptedFile.toString(), remoteFile, ChannelSftp.OVERWRITE);
            System.out.println("Uploaded: " + remoteFile);

        } catch (Exception ex) {
            System.out.println("SFTP upload error: " + ex);
            throw new RuntimeException("Failed to upload via SFTP: " + ex, ex);

        } finally {
            if (sftp != null && sftp.isConnected()) sftp.disconnect();
            if (session != null && session.isConnected()) session.disconnect();
        }
    }

    private Session createSession(StorageConfigModel config) throws ConnectionTestException, IOException {
        if (config.getPasswordPath() != null) {
            String pwd = utils.connection.helpers.CredentialUtils.readPasswordPath(config.getPasswordPath());
            return SFTPConnectionHandler.getSessionByPassword(
                    config.getUser(), config.getHost(), config.getPort(), pwd
            );
        } else {
            return SFTPConnectionHandler.getSessionByPrivateKeyPassKey(
                    config.getUser(),
                    config.getHost(),
                    config.getPrivateKeyPath(),
                    config.getPassPhrase(),
                    config.getPort()
            );
        }
    }

    private void ensureRemoteDirectory(String remoteDir, ChannelSftp sftp) throws SftpException {
        String[] parts = remoteDir.split("/");
        String current = "";
        for (String p : parts) {
            if (p.isBlank()) continue;
            current += "/" + p;
            try {
                sftp.cd(current);
            } catch (SftpException ex) {
                sftp.mkdir(current);
            }
        }
    }
}

