package config.user_config.storage_config;

public class StorageConfigModel {

    public static enum Type { LOCAL, EXTERNAL, PARTITION, SMB, NFS, SFTP, CLOUD }

    private Type type;
    private String path;
    private String mountPoint;
    private String host;
    private String share;
    private String user;
    private String passwordPath;
    private String remotePath;
    private String localMount;
    private String privateKeyPath;
    private String provider;
    private String bucket;
    private String accessKey;
    private String secretKey;
    private String passPhrase;
    private int port;
    private String encryptionKey;

    public void setType(Type type) { this.type = type; }
    public void setPath(String path) { this.path = path; }
    public void setMountPoint(String mountPoint) { this.mountPoint = mountPoint; }
    public void setHost(String host) { this.host = host; }
    public void setShare(String share) { this.share = share; }
    public void setUser(String user) { this.user = user; }
    public void setPasswordPath(String passwordPath) { this.passwordPath = passwordPath; }
    public void setRemotePath(String remotePath) { this.remotePath = remotePath; }
    public void setLocalMount(String localMount) { this.localMount = localMount; }
    public void setPrivateKeyPath(String privateKeyPath) { this.privateKeyPath = privateKeyPath; } 
    public void setProvider(String provider) { this.provider = provider; }
    public void setBucket(String bucket) { this.bucket = bucket; }
    public void setAccessKey(String accessKey) { this.accessKey = accessKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
    public void setPassPhrase(String passPhrase) { this.passPhrase = passPhrase; }
    public void setPort(int port) { this.port = port; }
    public void setEncryptionKey(String encryptionKey) { this.encryptionKey = encryptionKey; }

    public Type getType() { return type; }
    public String getPath() { return path; }
    public String getMountPoint() { return mountPoint; }
    public String getHost() { return host; }
    public String getShare() { return share; }
    public String getUser() { return user; }
    public String getPasswordPath() { return passwordPath; }
    public String getRemotePath() { return remotePath; }
    public String getLocalMount() { return localMount; }
    public String getPrivateKeyPath() { return privateKeyPath; }
    public String getProvider() { return provider; }
    public String getBucket() { return bucket; }
    public String getAccessKey() { return accessKey; }
    public String getSecretKey() { return secretKey; }
    public String getPassPhrase() { return passPhrase; }
    public int getPort() { return port; }
    public String getEncryptionKey() { return encryptionKey; }

    @Override
    public String toString() {
        return "StorageConfigModel{" +
                "type=" + type +
                ", path='" + path + '\'' +
                ", mountPoint='" + mountPoint + '\'' +
                ", host='" + host + '\'' +
                ", share='" + share + '\'' +
                ", user='" + user + '\'' +
                ", passwordPath='" + passwordPath + '\'' +
                ", remotePath='" + remotePath + '\'' +
                ", localMount='" + localMount + '\'' +
                ", privateKeyPath='" + privateKeyPath + '\'' +
                ", provider='" + provider + '\'' +
                ", bucket='" + bucket + '\'' +
                ", accessKey='" + accessKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", passPhrase='" + passPhrase + '\'' +
                ", port=" + port +
                '}';
    }
}

