package main;


public class Welcome{
    public static void display() {
        System.out.println("""
                ============================================================
                                        BACKUP SYSTEM
                ============================================================

                Welcome.

                Backup System is a secure, modular command-line application
                designed to protect your data through reliable backups.

                It supports a full backup pipeline:
                • File discovery
                • Compression
                • Encryption
                • Storage to local or remote targets

                ------------------------------------------------------------
                SUPPORTED STORAGE
                ------------------------------------------------------------
                • Local filesystem
                • External / mounted devices
                • SMB network shares
                • SFTP remote servers

                ------------------------------------------------------------
                SECURITY
                ------------------------------------------------------------
                • Encrypted backup archives
                • SHA-256 file integrity verification
                • Manifest-based tracking and recovery

                ------------------------------------------------------------
                USAGE
                ------------------------------------------------------------
                backup <command> [options]

                COMMON COMMANDS
                ------------------------------------------------------------
                init        Initialize workspace and configuration
                backup      Run a backup operation
                restore     Restore files from backup
                status      Show backup status
                config      View or edit configuration
                help        Show help and command details

                ------------------------------------------------------------
                WORKSPACE
                ------------------------------------------------------------
                ./backup_workspace/

                ============================================================
                """);         

    }
}
