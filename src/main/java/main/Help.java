package main;

public class Help {
    public static void display(){
        System.out.println("""
                ============================================================
                                        BACKUP SYSTEM
                ============================================================

                Secure, modular command-line backup utility.

                Designed for reliable data protection using compression,
                encryption, and multiple storage backends.

                ------------------------------------------------------------
                USAGE
                ------------------------------------------------------------
                backup <command> [options]

                ------------------------------------------------------------
                COMMANDS
                ------------------------------------------------------------
                init
                Initialize the backup workspace and configuration files.

                backup
                Run a backup operation using the current configuration.

                restore
                Restore files from an existing backup archive.

                status
                Display information about existing backups and workspace
                health.

                config
                View or modify application configuration.

                help
                Show this help message.

                ------------------------------------------------------------
                BACKUP PIPELINE
                ------------------------------------------------------------
                1. File discovery
                2. Compression (gzip)
                3. Encryption (AES-based)
                4. Storage to target location
                5. Manifest generation and verification

                ------------------------------------------------------------
                SUPPORTED STORAGE TARGETS
                ------------------------------------------------------------
                • Local filesystem
                • External / mounted devices
                • SMB network shares
                • SFTP remote servers

                ------------------------------------------------------------
                SECURITY FEATURES
                ------------------------------------------------------------
                • Encrypted backup archives
                • SHA-256 integrity verification
                • Password-based key derivation
                • Manifest-based recovery

                ------------------------------------------------------------
                WORKSPACE LAYOUT
                ------------------------------------------------------------
                ./backup_workspace/
                ├── config.yaml
                ├── backups/
                ├── manifests/
                └── temp/

                ------------------------------------------------------------
                EXAMPLES
                ------------------------------------------------------------
                Initialize workspace:
                backup init

                Run a backup:
                backup backup

                Restore files:
                backup restore

                Check status:
                backup status

                ------------------------------------------------------------
                EXIT CODES
                ------------------------------------------------------------
                0   Success
                1   Configuration or user error
                2   I/O or storage failure
                3   Cryptographic or integrity failure

                ============================================================
                """
                );
    }
}
