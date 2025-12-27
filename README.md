# Backup & Recovery System (Java)

A modular, secure, automated backup and recovery system designed for small and medium data environments.  
This system supports scheduled and manual backups, strong encryption (AES + RSA), selective restoration, storage management, monitoring, and failure notifications.

## 1. Overview

With the growth of digital data, organizations face risks of data loss due to hardware failure, human error, malware, or disasters.  
This project implements a simple, reliable, secure, and automated backup solution designed around:

- Automated and manual backups  
- Strong encryption  
- Predictive and real-time monitoring  
- Selective and full data restoration  
- Modular architecture for easy scaling  

This project is implemented in **Java**, following OOP principles and clean package separation.
## 2. Key Features

### ✔ Backup Operations
- Manual backup  
- Scheduled automatic backup  
- Full, incremental, and differential backups  
- Continuous data protection (CDP)

### ✔ Recovery Operations
- Restore full backup  
- Selective restore (files/folders)

### ✔ Storage Management
- Local and network storage support  
- Deduplication  
- Storage usage monitoring

### ✔ Security
- AES encryption for data-at-rest  
- RSA for key exchange  
- Encrypted transfer between modules  
- User authentication and activity logging

### ✔ Monitoring & Alerts
- Real-time backup status  
- Failure detection  
- Notifications  
- Backup logs & reports

## 3. System Architecture

```
+------------------------------------------------+
|                   App.java                     |
+-------------------------+----------------------+
|      Config Layer       |        Utils         |
| (DB, scheduler, crypto) | (encryption, files)  |
+-------------------------+----------------------+
|      Controllers (API layer)                   |
+------------------------------------------------+
| Services (Business Logic: backup, restore...)  |
+------------------------------------------------+
| Models (Data Structures / Entities)            |
+------------------------------------------------+
```

## 4. Project Structure

```
 |-backup-system
     |-pom.xml
     |-backup_workspace
         |-temp
             |-recovery
             |-archive
                 |-manifest.json
             |-recovery_selective
     |-backup_system.log
     |-backup_system.db
     |-src
         |-test
             |-java
                 |-java
                 |-notification
                 |-user
                     |-UserServiceTest.java
                 |-monitoring
                 |-backupConfiguration
                 |-storage
                 |-config
                     |-StorageConfigTest.java
                     |-ScheduleConfigTest.java
                     |-FileConfigTest.java
                 |-backup
                     |-FullBackupTest.java
                 |-restore
         |-main
             |-java
                 |-packages
                     |-user
                         |-UserUI.java
                         |-UserController.java
                         |-UserService.java
                         |-UserModel.java
                     |-storage
                         |-StorageManager.java
                         |-CloudStorageWriter.java
                         |-SFTPStorageWriter.java
                         |-StorageWriter.java
                         |-MountedDeviceWriter.java
                         |-SFTPStorageReader.java
                         |-NFSStorageWriter.java
                         |-LocalStorageReader.java
                         |-SMBStorageWriter.java
                         |-StorageReader.java
                         |-LocalStorageWriter.java
                     |-recovery
                         |-FullRecoveryService.java
                         |-RecoveryController.java
                         |-Recovery.java
                         |-SelectiveRecoveryService.java
                         |-RecoveryModel.java
                         |-Recoveryable.java
                         |-RecoveryServiceResolver.java
                     |-backup
                         |-BackupController.java
                         |-BackupScheduler.java
                         |-PredictiveBackupService.java
                         |-BackupModel.java
                         |-BackupDaemon.java
                         |-Backupable.java
                         |-BackupServiceResolver.java
                         |-Backup.java
                         |-FullBackupService.java
                         |-IncrementalBackupService.java
                 |-config
                     |-user_config
                         |-UserConfigLoader.java
                         |-RootConfig.java
                         |-storage_config
                             |-ConfigService.java
                             |-StorageConfigModel.java
                             |-ConfigValidator.java
                         |-schedule_config
                             |-ConfigService.java
                             |-BackupScheduleConfigModel.java
                             |-ConfigValidator.java
                         |-file_config
                             |-ConfigService.java
                             |-BackupFilesConfigModel.java
                             |-ConfigValidator.java
                     |-DbConfig.java
                     |-YamlLoader.java
                 |-utils
                     |-EncryptionUtils.java
                     |-Logger.java
                     |-compdecomp
                         |-FileUtils.java
                         |-FileHashing.java
                         |-CompressionManager.java
                         |-GzipCompressor.java
                         |-Compressable.java
                     |-connection
                         |-SFTPConnectionHandler.java
                         |-ConnectionTester.java
                         |-NFSConnectionHandler.java
                         |-helpers
                             |-CredentialUtils.java
                             |-NetworkUtils.java
                         |-ConnectionHandler.java
                         |-CloudConnectionHandler.java
                         |-SMBConnectionHandler.java
                         |-ConnectionHandlerResolver.java
                     |-Queues.java
                     |-encdecrypt
                         |-DecryptorService.java
                         |-EncrypterService.java
                         |-EncryptionHandler.java
                         |-Encryptable.java
                         |-EncryptionAdapter.java
                     |-SessionManager.java
                     |-manifest
                         |-ManifestEntry.java
                         |-ManifestDisplay.java
                         |-ManifestBuilder.java
                 |-main
                     |-Help.java
                     |-report
                         |-LogAnalyzer.java
                     |-App.java
                     |-APP_GUI.java
                     |-Welcome.java
                     |-gui
                         |-AppIcon.java
                         |-HandleClose.java
                         |-Header.java
                         |-NavigationManager.java
                         |-StatusView.java
                         |-LogView.java
                         |-WelcomeView.java
                         |-ReportView.java
                         |-HelpView.java
                     |-Launcher.java
                 |-exception
                     |-userservice
                         |-UsernameExistsException.java
                         |-InvalidUsernameException.java
                         |-InvalidPasswordException.java
                         |-EmailExistsException.java
                         |-InvalidEmailFormatException.java
                         |-UserNotFoundException.java
                         |-InvalidCredentialsException.java
                     |-connecion
                         |-ShareNotFoundException.java
                         |-HostUnreachableException.java
                         |-ConnectionTestException.java
                         |-AuthFailedException.java
             |-resources
                 |-schedule.yaml
                 |-styles
                     |-app.css
                 |-10109861.png



```

## 5. Installation

### Prerequisites
- Java 17+
- Maven or Gradle

### Steps
```
git clone https://github.com/admarspia/SecureBackupSuite
cd backup-system
mvn clean install
```

Run:
```
mvn exec:java -Dexec.mainClass="App"
```

## 6. Usage

1. Configure backup  
2. Perform manual backup  
3. Scheduled backup triggers automatically  
4. Restore files  
5. View monitoring status  

## 7. References

- NSBS Backup Architecture  
- Cloud Backup Mapping Study  
- TechTarget Backup Strategies  
- Deduplication Studies  
- Security Best Practices  

## 8. Future Improvements

- Cloud integrations  
- Predictive analytics    
- Plugin-based storage adapters  

## 9. License

Open-source for academic and learning purposes.

