package de.florian.jextensions.io.file;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class SingleInstanceLockTest {

    @AfterEach
    public void after() {
        SingleInstanceLock.getLockFilePath().toFile().delete();
    }

    @Test
    public void testSingleInstanceRunning_whenAnotherInstanceRunning() {
        assertFalse(SingleInstanceLock.getLockFilePath().toFile().exists(), "Lock file should not exist");

        SingleInstanceLock firstInstance = new SingleInstanceLock();
        assertFalse(firstInstance.isSingleInstanceRunning(), "First instance should not detect another instance running.");

        SingleInstanceLock secondInstance = new SingleInstanceLock();
        assertTrue(secondInstance.isSingleInstanceRunning(), "Second instance should detect the first instance running.");
    }

    @Test
    public void testLogger_whenLockFileCreated() {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor)); // reset with System.setOut(System.out);
        new SingleInstanceLock().isSingleInstanceRunning();
        System.out.flush();
        String output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains("file as lock file"), "Log should contain the 'file as lock file' message indicating lock file creation.");
        assertTrue(output.contains("No other instance is running"), "Log should contain the 'No other instance is running' message.");
    }
}