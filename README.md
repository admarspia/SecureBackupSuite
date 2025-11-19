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
backup-system/
│
├── src/
│   ├── main/
│   │   ├── App.java
│   │   └── config/
│   │       ├── DbConfig.java
│   │       ├── EncryptionConfig.java
│   │       └── ScheduleConfig.java
│   │
│   ├── packages/
│   │   ├── user/
│   │   ├── backupConfiguration/
│   │   ├── backup/
│   │   ├── restore/
│   │   ├── storage/
│   │   ├── monitoring/
│   │   └── notification/
│   │
│   └── utils/
│       ├── EncryptionUtils.java
│       ├── FileUtils.java
│       └── Logger.java
│
├── tests/
├── README.md
└── .gitignore
```

## 5. Installation

### Prerequisites
- Java 17+
- Maven or Gradle

### Steps
```
git clone https://github.com/admarspia/backup-system.git
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

