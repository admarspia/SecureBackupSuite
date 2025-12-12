package storage;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import config.user_config.storage_config.StorageConfigModel;
import utils.Queues;
import utils.connection.SFTPConnectionHandler;
import exception.connection.ConnectionTestException;
import utils.connection.helpers.CredentialUtils;

public class SFTPStorageReader implements StorageReader {

    @Override
    public void read(StorageConfigModel config, Path downloadDir) throws IOException, InterruptedException {
        Files.createDirectories(downloadDir);

        Session session = null;
        ChannelSftp sftp = null;

        try {
            session = createSession(config);
            sftp = (ChannelSftp) session.openChannel("sftp");
            sftp.connect();

            String remoteDir = config.getPath();
            Vector<ChannelSftp.LsEntry> entries = sftp.ls(remoteDir);

            for (ChannelSftp.LsEntry entry : entries) {
                String name = entry.getFilename();
                if (name.equals(".") || name.equals("..") || !name.endsWith(".enc")) continue;

                Path localFile = downloadDir.resolve(name);
                sftp.get(remoteDir + "/" + name, localFile.toString());
                Queues.ENCRYPTED_QUEUE.put(localFile);
                System.out.println("SFTP file queued: " + localFile.getFileName());
            }

        } catch (Exception ex) {
            throw new IOException("SFTP read failed: " + ex, ex);

        } finally {
            if (sftp != null && sftp.isConnected()) sftp.disconnect();
            if (session != null && session.isConnected()) session.disconnect();
        }

        Queues.ENCRYPTED_QUEUE.put(Queues.POISON); 
    }

    private Session createSession(StorageConfigModel config) throws ConnectionTestException, IOException {
        if (config.getPasswordPath() != null) {
            String pwd = CredentialUtils.readPasswordPath(config.getPasswordPath());
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
}

