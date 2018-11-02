//package ssh;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Map;
//import org.apache.commons.exec.CommandLine;
//import org.apache.commons.exec.ExecuteException;
//import org.apache.commons.exec.ExecuteResultHandler;
//import org.apache.commons.exec.ExecuteStreamHandler;
//import org.apache.commons.exec.ExecuteWatchdog;
//import org.apache.commons.exec.Executor;
//import org.apache.commons.exec.ProcessDestroyer;
//import org.apache.commons.exec.PumpStreamHandler;
//import org.apache.commons.exec.launcher.CommandLauncher;
//import org.apache.commons.exec.launcher.CommandLauncherFactory;
//
//
///**
// * WILL change from org.apache.commons.exec.DefaultExecutor
// */
//public class JschDefaultExecutor implements Executor {
//
//    private ExecuteStreamHandler streamHandler = new PumpStreamHandler();
//    private File workingDirectory = new File(".");
//    private ExecuteWatchdog watchdog;
//    private int[] exitValues = new int[0];
//    private final CommandLauncher launcher = CommandLauncherFactory.createVMLauncher();
//    private ProcessDestroyer processDestroyer;
//    private Thread executorThread;
//    private IOException exceptionCaught = null;
//
//    public JschDefaultExecutor() {
//    }
//
//    public ExecuteStreamHandler getStreamHandler() {
//        return this.streamHandler;
//    }
//
//    public void setStreamHandler(ExecuteStreamHandler streamHandler) {
//        this.streamHandler = streamHandler;
//    }
//
//    public ExecuteWatchdog getWatchdog() {
//        return this.watchdog;
//    }
//
//    public void setWatchdog(ExecuteWatchdog watchDog) {
//        this.watchdog = watchDog;
//    }
//
//    public ProcessDestroyer getProcessDestroyer() {
//        return this.processDestroyer;
//    }
//
//    public void setProcessDestroyer(ProcessDestroyer processDestroyer) {
//        this.processDestroyer = processDestroyer;
//    }
//
//    public File getWorkingDirectory() {
//        return this.workingDirectory;
//    }
//
//    public void setWorkingDirectory(File dir) {
//        this.workingDirectory = dir;
//    }
//
//    public int execute(CommandLine command) throws ExecuteException, IOException {
//        return this.execute(command, (Map) null);
//    }
//
//    public int execute(CommandLine command, Map<String, String> environment) throws ExecuteException, IOException {
//        if (this.workingDirectory != null && !this.workingDirectory.exists()) {
//            throw new IOException(this.workingDirectory + " doesn't exist.");
//        } else {
//            return this.executeInternal(command, environment, this.workingDirectory, this.streamHandler);
//        }
//    }
//
//    public void execute(CommandLine command, ExecuteResultHandler handler) throws ExecuteException, IOException {
//        this.execute(command, (Map) null, handler);
//    }
//
//    public void execute(final CommandLine command, final Map<String, String> environment, final ExecuteResultHandler handler)
//        throws ExecuteException, IOException {
//        if (this.workingDirectory != null && !this.workingDirectory.exists()) {
//            throw new IOException(this.workingDirectory + " doesn't exist.");
//        } else {
//            Runnable runnable = new Runnable() {
//                public void run() {
//                    int exitValue = -559038737;
//
//                    try {
//                        exitValue = executeInternal(command, environment, workingDirectory, streamHandler);
//                        handler.onProcessComplete(exitValue);
//                    } catch (ExecuteException var3) {
//                        handler.onProcessFailed(var3);
//                    } catch (Exception var4) {
//                        handler.onProcessFailed(new ExecuteException("Execution failed", exitValue, var4));
//                    }
//
//                }
//            };
//            this.executorThread = this.createThread(runnable, "Exec Default Executor");
//            this.getExecutorThread().start();
//        }
//    }
//
//    public void setExitValue(int value) {
//        this.setExitValues(new int[] {value});
//    }
//
//    public void setExitValues(int[] values) {
//        this.exitValues = values == null ? null : (int[]) ((int[]) values.clone());
//    }
//
//    public boolean isFailure(int exitValue) {
//        if (this.exitValues == null) {
//            return false;
//        } else if (this.exitValues.length == 0) {
//            return this.launcher.isFailure(exitValue);
//        } else {
//            int[] arr$ = this.exitValues;
//            int len$ = arr$.length;
//
//            for (int i$ = 0; i$ < len$; ++i$) {
//                int exitValue2 = arr$[i$];
//                if (exitValue2 == exitValue) {
//                    return false;
//                }
//            }
//
//            return true;
//        }
//    }
//
//    protected Thread createThread(Runnable runnable, String name) {
//        return new Thread(runnable, name);
//    }
//
//    protected Process launch(CommandLine command, Map<String, String> env, File dir) throws IOException {
//        if (this.launcher == null) {
//            throw new IllegalStateException("CommandLauncher can not be null");
//        } else if (dir != null && !dir.exists()) {
//            throw new IOException(dir + " doesn't exist.");
//        } else {
//            return this.launcher.exec(command, env, dir);
//        }
//    }
//
//    protected Thread getExecutorThread() {
//        return this.executorThread;
//    }
//
//    private void closeProcessStreams(Process process) {
//        try {
//            process.getInputStream().close();
//        } catch (IOException var5) {
//            this.setExceptionCaught(var5);
//        }
//
//        try {
//            process.getOutputStream().close();
//        } catch (IOException var4) {
//            this.setExceptionCaught(var4);
//        }
//
//        try {
//            process.getErrorStream().close();
//        } catch (IOException var3) {
//            this.setExceptionCaught(var3);
//        }
//
//    }
//
//    private int executeInternal(CommandLine command, Map<String, String> environment, File dir, ExecuteStreamHandler streams) throws IOException {
//        this.setExceptionCaught((IOException) null);
//        Process process = this.launch(command, environment, dir);
//
//        try {
//            streams.setProcessInputStream(process.getOutputStream());
//            streams.setProcessOutputStream(process.getInputStream());
//            streams.setProcessErrorStream(process.getErrorStream());
//        } catch (IOException var29) {
//            process.destroy();
//            throw var29;
//        }
//
//        streams.start();
//
//        int var7;
//        try {
//            if (this.getProcessDestroyer() != null) {
//                this.getProcessDestroyer().add(process);
//            }
//
//            if (this.watchdog != null) {
//                this.watchdog.start(process);
//            }
//
//            int exitValue = -559038737;
//
//            try {
//                exitValue = process.waitFor();
//            } catch (InterruptedException var27) {
//                process.destroy();
//            } finally {
//                Thread.interrupted();
//            }
//
//            if (this.watchdog != null) {
//                this.watchdog.stop();
//            }
//
//            try {
//                streams.stop();
//            } catch (IOException var26) {
//                this.setExceptionCaught(var26);
//            }
//
//            this.closeProcessStreams(process);
//            if (this.getExceptionCaught() != null) {
//                throw this.getExceptionCaught();
//            }
//
//            if (this.watchdog != null) {
//                try {
//                    this.watchdog.checkException();
//                } catch (IOException var24) {
//                    throw var24;
//                } catch (Exception var25) {
//                    throw new IOException(var25.getMessage());
//                }
//            }
//
//            if (this.isFailure(exitValue)) {
//                throw new ExecuteException("Process exited with an error: " + exitValue, exitValue);
//            }
//
//            var7 = exitValue;
//        } finally {
//            if (this.getProcessDestroyer() != null) {
//                this.getProcessDestroyer().remove(process);
//            }
//
//        }
//
//        return var7;
//    }
//
//    private void setExceptionCaught(IOException e) {
//        if (this.exceptionCaught == null) {
//            this.exceptionCaught = e;
//        }
//
//    }
//
//    private IOException getExceptionCaught() {
//        return this.exceptionCaught;
//    }
//}