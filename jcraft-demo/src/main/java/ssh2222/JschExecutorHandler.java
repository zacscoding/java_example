package ssh2222;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.ProcessDestroyer;
import org.apache.commons.exec.PumpStreamHandler;

/**
 * @author zacconding
 * @Date 2018-11-07
 * @GitHub : https://github.com/zacscoding
 */
public class JschExecutorHandler implements Executor {

    private ExecuteStreamHandler streamHandler;

    /**
     * monitoring of long running processes
     */
    private ExecuteWatchdog watchdog;

    /**
     * the exit values considered to be successful
     */
    private int[] exitValues;

    /**
     * optional cleanup of started processes
     */
    private ProcessDestroyer processDestroyer;

    /**
     * the first exception being caught to be thrown to the caller
     */
    private IOException exceptionCaught;

    private Process process;

    public JschExecutorHandler(Process process) {
        this.process = process;
        this.streamHandler = new PumpStreamHandler();
        this.exitValues = new int[0];
        this.exceptionCaught = null;
    }

    @Override
    public void setExitValue(int value) {
        this.setExitValues(new int[]{value});
    }

    @Override
    public void setExitValues(int[] values) {
        this.exitValues = values == null ? null : (int[]) values.clone();
    }

    @Override
    public boolean isFailure(int exitValue) {
        if (this.exitValues == null) {
            return false;
        } else if (this.exitValues.length == 0) {
            // return this.launcher.isFailure(exitValue);
        } else {
            for (final int exitValue2 : this.exitValues) {
                if (exitValue2 == exitValue) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public ExecuteStreamHandler getStreamHandler() {
        return this.streamHandler;
    }

    @Override
    public void setStreamHandler(ExecuteStreamHandler executeStreamHandler) {
        this.streamHandler = executeStreamHandler;
    }

    @Override
    public ExecuteWatchdog getWatchdog() {
        return watchdog;
    }

    @Override
    public void setWatchdog(ExecuteWatchdog executeWatchdog) {
        this.watchdog = executeWatchdog;
    }

    @Override
    public ProcessDestroyer getProcessDestroyer() {
        return processDestroyer;
    }

    @Override
    public void setProcessDestroyer(ProcessDestroyer processDestroyer) {
        this.processDestroyer = processDestroyer;
    }

    @Override
    public File getWorkingDirectory() {
        throw new UnsupportedOperationException("Not supported getWorkingDirectory()");
    }

    @Override
    public void setWorkingDirectory(File file) {
        throw new UnsupportedOperationException("Not supported setWorkingDirectory()");
    }

    @Override
    public int execute(CommandLine commandLine) throws ExecuteException, IOException {
        return 0;
    }

    @Override
    public int execute(CommandLine commandLine, Map<String, String> map) throws ExecuteException, IOException {
        return 0;
    }

    @Override
    public void execute(CommandLine commandLine, ExecuteResultHandler executeResultHandler)
        throws ExecuteException, IOException {

    }

    @Override
    public void execute(CommandLine commandLine, Map<String, String> map, ExecuteResultHandler executeResultHandler)
        throws ExecuteException, IOException {

    }

    private int executeInternal(final ExecuteStreamHandler streams) throws IOException {

        try {
            streams.setProcessInputStream(process.getOutputStream());
            streams.setProcessOutputStream(process.getInputStream());
            streams.setProcessErrorStream(process.getErrorStream());
        } catch (final IOException e) {
            process.destroy();
            throw e;
        }

        streams.start();

        try {

            // add the process to the list of those to destroy if the VM exits
            if (this.getProcessDestroyer() != null) {
                this.getProcessDestroyer().add(process);
            }

            // associate the watchdog with the newly created process
            if (watchdog != null) {
                watchdog.start(process);
            }

            int exitValue = Executor.INVALID_EXITVALUE;

            try {
                exitValue = process.waitFor();
            } catch (final InterruptedException e) {
                process.destroy();
            } finally {
                // see http://bugs.sun.com/view_bug.do?bug_id=6420270
                // see https://issues.apache.org/jira/browse/EXEC-46
                // Process.waitFor should clear interrupt status when throwing InterruptedException
                // but we have to do that manually
                Thread.interrupted();
            }

            if (watchdog != null) {
                watchdog.stop();
            }

            try {
                streams.stop();
            } catch (final IOException e) {
                setExceptionCaught(e);
            }

            closeProcessStreams(process);

            if (getExceptionCaught() != null) {
                throw getExceptionCaught();
            }

            if (watchdog != null) {
                try {
                    watchdog.checkException();
                } catch (final IOException e) {
                    throw e;
                } catch (final Exception e) {
                    // Java 1.5 does not support public IOException(String message, Throwable cause)
                    IOException ioe = new IOException(e.getMessage());
                    ioe.initCause(e);
                    throw ioe;
                }
            }

            if (this.isFailure(exitValue)) {
                throw new ExecuteException("Process exited with an error: " + exitValue, exitValue);
            }

            return exitValue;
        } finally {
            // remove the process to the list of those to destroy if the VM exits
            if (this.getProcessDestroyer() != null) {
                this.getProcessDestroyer().remove(process);
            }
        }
    }

    private void setExceptionCaught(final IOException e) {
        if (this.exceptionCaught == null) {
            this.exceptionCaught = e;
        }
    }

    /**
     * Close the streams belonging to the given Process.
     *
     * @param process the <CODE>Process</CODE>.
     */
    private void closeProcessStreams(final Process process) {
        try {
            process.getInputStream().close();
        } catch (final IOException e) {
            setExceptionCaught(e);
        }

        try {
            process.getOutputStream().close();
        } catch (final IOException e) {
            setExceptionCaught(e);
        }

        try {
            process.getErrorStream().close();
        } catch (final IOException e) {
            setExceptionCaught(e);
        }
    }

    /**
     * Get the first IOException being thrown.
     *
     * @return the first IOException being caught
     */
    private IOException getExceptionCaught() {
        return this.exceptionCaught;
    }
}
