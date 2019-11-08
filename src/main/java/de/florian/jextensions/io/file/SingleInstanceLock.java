package de.florian.jextensions.io.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

public class SingleInstanceLock {
    private static final Logger log = LoggerFactory.getLogger(SingleInstanceLock.class);

    private File lockFile;
    private FileChannel lockFileChannel;
    private FileLock lock;

    private final String uniquePermanentFileIdentifier = this.getClass().getPackage().getName().replace(".", "_") + "_" + SingleInstanceLock.class.getSimpleName() + ".tmp";

    public boolean isSingleInstanceRunning() {
        try {
            String directory = System.getProperty("java.io.tmpdir");
            lockFile = new File(directory, uniquePermanentFileIdentifier);
            log.debug("Using \"" + lockFile + "\" file as lock file!");
            lockFileChannel = new RandomAccessFile(lockFile, "rw").getChannel();

            try {
                lock = lockFileChannel.tryLock();
            } catch (OverlappingFileLockException e) {
                // already locked
                closeLock();
                return true;
            }

            if (lock == null || !lock.isValid()) {
                closeLock();
                return true;
            }

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                closeLock();
                deleteFile();
            }));
            return false;
        } catch (Exception e) {
            closeLock();
            return true;
        }
    }

    private void closeLock() {
        try {
            lock.release();
        } catch (Exception ignored) {
        }
        try {
            lockFileChannel.close();
        } catch (Exception ignored) {
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void deleteFile() {
        try {
            lockFile.delete();
        } catch (Exception ignored) {
        }
    }
}
