package utils;

import java.nio.file.Path;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public final class Queues {
    public static final BlockingQueue<Path> COMPRESSED_QUEUE = new ArrayBlockingQueue<>(256);
    public static final BlockingQueue<Path> ENCRYPTED_QUEUE = new ArrayBlockingQueue<>(256);
    public static final BlockingQueue<Path> DECRYPTED_QUEUE = new ArrayBlockingQueue<>(256);
    public static final Path POISON = Path.of("__POISON__");
}

