package utils.encdecrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;
import java.nio.file.*;
import java.security.SecureRandom;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Base64;

import utils.compdecomp.FileUtils;


public final class EncryptionHandler implements Encryptable {
    private static final int IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH_BITS = 128;

    private final SecretKey key;

    public EncryptionHandler(byte[] keyBytes) {
        Objects.requireNonNull(keyBytes);
        this.key = new SecretKeySpec(keyBytes, "AES");
    }

    public static EncryptionHandler fromBase64Key(String base64Key) {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        return new EncryptionHandler(keyBytes);
    }

    @Override
    public Path encryptFile(Path sourceFile, Path destDir) throws IOException {
        Objects.requireNonNull(sourceFile);
        Objects.requireNonNull(destDir);

        if (!Files.isRegularFile(sourceFile)) {
            throw new IllegalArgumentException("Source must be a file: " + sourceFile);
        }

        try {
            FileUtils.ensureDir(destDir);
        } catch (IOException e) {
            throw new IOException("Cannot create dest dir: " + destDir, e);
        }

        String base = sourceFile.getFileName().toString();
        Path temp = FileUtils.tempFile(destDir, base + "_enc", ".tmp");
        Path target = destDir.resolve(base + ".enc");

        byte[] iv = new byte[IV_LENGTH];
        SecureRandom rnd = new SecureRandom();
        rnd.nextBytes(iv);

        try (InputStream in = Files.newInputStream(sourceFile);
             OutputStream fout = Files.newOutputStream(temp)) {

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);

            fout.write(iv);

            try (java.security.DigestOutputStream ignored = null) {
                try (javax.crypto.CipherOutputStream cos = new javax.crypto.CipherOutputStream(fout, cipher)) {
                    in.transferTo(cos);
                }
            }
        } catch (Exception ex) {
            try { Files.deleteIfExists(temp); } catch (IOException ignored) {}
            throw new IOException("Encryption failed", ex);
        }

        Files.move(temp, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        return target;
    }

    @Override
    public Path decryptFile(Path encryptedFile, Path destDir) throws IOException {
        Objects.requireNonNull(encryptedFile);
        Objects.requireNonNull(destDir);

        if (!Files.isRegularFile(encryptedFile)) {
            throw new IllegalArgumentException("Encrypted file must be a file: " + encryptedFile);
        }

        FileUtils.ensureDir(destDir);

        String name = encryptedFile.getFileName().toString();
        String outName = name.endsWith(".enc") ? name.substring(0, name.length() - 4)  : name ;
        Path temp = FileUtils.tempFile(destDir, outName, ".tmp");
        Path target = destDir.resolve(outName);

        byte[] iv = new byte[IV_LENGTH];

        try (InputStream fin = Files.newInputStream(encryptedFile)) {
            int read = fin.read(iv);
            if (read != IV_LENGTH) throw new IOException("Invalid encrypted file (IV missing)");

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);

            try (javax.crypto.CipherInputStream cis = new javax.crypto.CipherInputStream(fin, cipher);
                 OutputStream out = Files.newOutputStream(temp)) {
                cis.transferTo(out);
            }
        } catch (Exception ex) {
            try { Files.deleteIfExists(temp); } catch (IOException ignored) {}
            throw new IOException("Decryption failed", ex);
        }

        Files.move(temp, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        return target;
    }
}

