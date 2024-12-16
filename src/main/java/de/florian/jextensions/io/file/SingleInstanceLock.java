package de.florian.jextensions.io.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SingleInstanceLock {
    private static final Logger log = LoggerFactory.getLogger(SingleInstanceLock.class);

    private File lockFile;
    private FileChannel lockFileChannel;
    private FileLock lock;

    private static final String uniquePermanentFileIdentifier = SingleInstanceLock.class.getPackage().getName().replace(".", "_") + "_" + SingleInstanceLock.class.getSimpleName() + ".tmp";
    
    public static Path getLockFilePath(){
        return Paths.get(System.getProperty("java.io.tmpdir"), uniquePermanentFileIdentifier);
    }
    @SuppressWarnings("resource")
    public boolean isSingleInstanceRunning() {
        try {
            lockFile = getLockFilePath().toFile();
            log.info("Using \"" + lockFile + "\" file as lock file!");
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
                deleteLockFile();
            }));
            log.info("No other instance is running, added hook to clean up lockfile when exiting!");
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
    private void deleteLockFile() {
        try {
            lockFile.delete();
        } catch (Exception ignored) {
        }
    }
}
