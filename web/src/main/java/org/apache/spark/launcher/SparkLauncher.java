package org.apache.spark.launcher;

import org.apache.spark.launcher.SparkAppHandle.Listener;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class SparkLauncher {
    public static final String SPARK_MASTER = "spark.master";
    public static final String DEPLOY_MODE = "spark.submit.deployMode";
    public static final String DRIVER_MEMORY = "spark.driver.memory";
    public static final String DRIVER_EXTRA_CLASSPATH = "spark.driver.extraClassPath";
    public static final String DRIVER_EXTRA_JAVA_OPTIONS = "spark.driver.extraJavaOptions";
    public static final String DRIVER_EXTRA_LIBRARY_PATH = "spark.driver.extraLibraryPath";
    public static final String EXECUTOR_MEMORY = "spark.executor.memory";
    public static final String EXECUTOR_EXTRA_CLASSPATH = "spark.executor.extraClassPath";
    public static final String EXECUTOR_EXTRA_JAVA_OPTIONS = "spark.executor.extraJavaOptions";
    public static final String EXECUTOR_EXTRA_LIBRARY_PATH = "spark.executor.extraLibraryPath";
    public static final String EXECUTOR_CORES = "spark.executor.cores";
    static final String PYSPARK_DRIVER_PYTHON = "spark.pyspark.driver.python";
    static final String PYSPARK_PYTHON = "spark.pyspark.python";
    static final String SPARKR_R_SHELL = "spark.r.shell.command";
    public static final String CHILD_PROCESS_LOGGER_NAME = "spark.launcher.childProcLoggerName";
    public static final String NO_RESOURCE = "spark-internal";
    public static final String CHILD_CONNECTION_TIMEOUT = "spark.launcher.childConectionTimeout";
    private static final AtomicInteger COUNTER = new AtomicInteger();
    static final ThreadFactory REDIRECTOR_FACTORY = new NamedThreadFactory("launcher-proc-%d");
    static final Map<String, String> launcherConfig = new HashMap();
    final SparkSubmitCommandBuilder builder;
    File workingDir;
    boolean redirectToLog;
    boolean redirectErrorStream;
    Redirect errorStream;
    Redirect outputStream;

    public static void setConfig(String name, String value) {
        launcherConfig.put(name, value);
    }

    public SparkLauncher() {
        this((Map)null);
    }

    public SparkLauncher(Map<String, String> env) {
        this.builder = new SparkSubmitCommandBuilder();
        if(env != null) {
            this.builder.childEnv.putAll(env);
        }

    }

    public org.apache.spark.launcher.SparkLauncher setJavaHome(String javaHome) {
        CommandBuilderUtils.checkNotNull(javaHome, "javaHome");
        this.builder.javaHome = javaHome;
        return this;
    }

    public org.apache.spark.launcher.SparkLauncher setSparkHome(String sparkHome) {
        CommandBuilderUtils.checkNotNull(sparkHome, "sparkHome");
        this.builder.childEnv.put("SPARK_HOME", sparkHome);
        return this;
    }

    public org.apache.spark.launcher.SparkLauncher setPropertiesFile(String path) {
        CommandBuilderUtils.checkNotNull(path, "path");
        this.builder.setPropertiesFile(path);
        return this;
    }

    public org.apache.spark.launcher.SparkLauncher setConf(String key, String value) {
        CommandBuilderUtils.checkNotNull(key, "key");
        CommandBuilderUtils.checkNotNull(value, "value");
        CommandBuilderUtils.checkArgument(key.startsWith("spark."), "'key' must start with 'spark.'", new Object[0]);
        this.builder.conf.put(key, value);
        return this;
    }

    public org.apache.spark.launcher.SparkLauncher setAppName(String appName) {
        CommandBuilderUtils.checkNotNull(appName, "appName");
        this.builder.appName = appName;
        return this;
    }

    public org.apache.spark.launcher.SparkLauncher setMaster(String master) {
        CommandBuilderUtils.checkNotNull(master, "master");
        this.builder.master = master;
        return this;
    }

    public org.apache.spark.launcher.SparkLauncher setDeployMode(String mode) {
        CommandBuilderUtils.checkNotNull(mode, "mode");
        this.builder.deployMode = mode;
        return this;
    }

    public org.apache.spark.launcher.SparkLauncher setAppResource(String resource) {
        CommandBuilderUtils.checkNotNull(resource, "resource");
        this.builder.appResource = resource;
        return this;
    }

    public org.apache.spark.launcher.SparkLauncher setMainClass(String mainClass) {
        CommandBuilderUtils.checkNotNull(mainClass, "mainClass");
        this.builder.mainClass = mainClass;
        return this;
    }

    public org.apache.spark.launcher.SparkLauncher addSparkArg(String arg) {
        SparkSubmitOptionParser validator = new org.apache.spark.launcher.SparkLauncher.ArgumentValidator(false);
        validator.parse(Arrays.asList(new String[]{arg}));
        this.builder.sparkArgs.add(arg);
        return this;
    }

    public org.apache.spark.launcher.SparkLauncher addSparkArg(String name, String value) {
        SparkSubmitOptionParser validator = new org.apache.spark.launcher.SparkLauncher.ArgumentValidator(true);
        validator.getClass();
        if("--master".equals(name)) {
            this.setMaster(value);
        } else {
            validator.getClass();
            if("--properties-file".equals(name)) {
                this.setPropertiesFile(value);
            } else {
                validator.getClass();
                String[] arr$;
                if("--conf".equals(name)) {
                    arr$ = value.split("=", 2);
                    this.setConf(arr$[0], arr$[1]);
                } else {
                    validator.getClass();
                    if("--class".equals(name)) {
                        this.setMainClass(value);
                    } else {
                        validator.getClass();
                        int len$;
                        int i$;
                        String file;
                        if("--jars".equals(name)) {
                            this.builder.jars.clear();
                            arr$ = value.split(",");
                            len$ = arr$.length;

                            for(i$ = 0; i$ < len$; ++i$) {
                                file = arr$[i$];
                                this.addJar(file);
                            }
                        } else {
                            validator.getClass();
                            if("--files".equals(name)) {
                                this.builder.files.clear();
                                arr$ = value.split(",");
                                len$ = arr$.length;

                                for(i$ = 0; i$ < len$; ++i$) {
                                    file = arr$[i$];
                                    this.addFile(file);
                                }
                            } else {
                                validator.getClass();
                                if("--py-files".equals(name)) {
                                    this.builder.pyFiles.clear();
                                    arr$ = value.split(",");
                                    len$ = arr$.length;

                                    for(i$ = 0; i$ < len$; ++i$) {
                                        file = arr$[i$];
                                        this.addPyFile(file);
                                    }
                                } else {
                                    validator.parse(Arrays.asList(new String[]{name, value}));
                                    this.builder.sparkArgs.add(name);
                                    this.builder.sparkArgs.add(value);
                                }
                            }
                        }
                    }
                }
            }
        }

        return this;
    }

    public org.apache.spark.launcher.SparkLauncher addAppArgs(String... args) {
        this.builder.appArgs.clear();
        String[] arr$ = args;
        int len$ = args.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String arg = arr$[i$];
            CommandBuilderUtils.checkNotNull(arg, "arg");
            this.builder.appArgs.add(arg);
        }
        return this;
    }

    public org.apache.spark.launcher.SparkLauncher addJar(String jar) {
        CommandBuilderUtils.checkNotNull(jar, "jar");
        this.builder.jars.add(jar);
        return this;
    }

    public org.apache.spark.launcher.SparkLauncher addFile(String file) {
        CommandBuilderUtils.checkNotNull(file, "file");
        this.builder.files.add(file);
        return this;
    }

    public org.apache.spark.launcher.SparkLauncher addPyFile(String file) {
        CommandBuilderUtils.checkNotNull(file, "file");
        this.builder.pyFiles.add(file);
        return this;
    }

    public org.apache.spark.launcher.SparkLauncher setVerbose(boolean verbose) {
        this.builder.verbose = verbose;
        return this;
    }

    public org.apache.spark.launcher.SparkLauncher directory(File dir) {
        this.workingDir = dir;
        return this;
    }

    public org.apache.spark.launcher.SparkLauncher redirectError() {
        this.redirectErrorStream = true;
        return this;
    }

    public org.apache.spark.launcher.SparkLauncher redirectError(Redirect to) {
        this.errorStream = to;
        return this;
    }

    public org.apache.spark.launcher.SparkLauncher redirectOutput(Redirect to) {
        this.outputStream = to;
        return this;
    }

    public org.apache.spark.launcher.SparkLauncher redirectError(File errFile) {
        this.errorStream = Redirect.to(errFile);
        return this;
    }

    public org.apache.spark.launcher.SparkLauncher redirectOutput(File outFile) {
        this.outputStream = Redirect.to(outFile);
        return this;
    }

    public org.apache.spark.launcher.SparkLauncher redirectToLog(String loggerName) {
        this.setConf("spark.launcher.childProcLoggerName", loggerName);
        this.redirectToLog = true;
        return this;
    }

    public Process launch() throws IOException {
        Process childProc = this.createBuilder().start();
        if(this.redirectToLog) {
            String loggerName = (String)this.builder.getEffectiveConfig().get("spark.launcher.childProcLoggerName");
            new OutputRedirector(childProc.getInputStream(), loggerName, REDIRECTOR_FACTORY);
        }

        return childProc;
    }

    public SparkAppHandle startApplication(Listener... listeners) throws IOException {
        ChildProcAppHandle handle = LauncherServer.newAppHandle();
        Listener[] arr$ = listeners;
        int len$ = listeners.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Listener l = arr$[i$];
            handle.addListener(l);
        }

        String loggerName = (String)this.builder.getEffectiveConfig().get("spark.launcher.childProcLoggerName");
        ProcessBuilder pb = this.createBuilder();
        if(loggerName == null) {
            String appName = (String)this.builder.getEffectiveConfig().get("spark.launcher.childProcLoggerName");
            if(appName == null) {
                if(this.builder.appName != null) {
                    appName = this.builder.appName;
                } else if(this.builder.mainClass != null) {
                    int dot = this.builder.mainClass.lastIndexOf(".");
                    if(dot >= 0 && dot < this.builder.mainClass.length() - 1) {
                        appName = this.builder.mainClass.substring(dot + 1, this.builder.mainClass.length());
                    } else {
                        appName = this.builder.mainClass;
                    }
                } else if(this.builder.appResource != null) {
                    appName = (new File(this.builder.appResource)).getName();
                } else {
                    appName = String.valueOf(COUNTER.incrementAndGet());
                }
            }

            String loggerPrefix = this.getClass().getPackage().getName();
            loggerName = String.format("%s.app.%s", new Object[]{loggerPrefix, appName});
            pb.redirectErrorStream(true);
        }

        pb.environment().put("_SPARK_LAUNCHER_PORT", String.valueOf(LauncherServer.getServerInstance().getPort()));
        pb.environment().put("_SPARK_LAUNCHER_SECRET", handle.getSecret());

        try {
            handle.setChildProc(pb.start(), loggerName);
            return handle;
        } catch (IOException var7) {
            handle.kill();
            throw var7;
        }
    }

    private ProcessBuilder createBuilder() {
        List<String> cmd = new ArrayList();
        String script = CommandBuilderUtils.isWindows()?"spark-submit.cmd":"spark-submit";
        cmd.add(CommandBuilderUtils.join(File.separator, new String[]{this.builder.getSparkHome(), "bin", script}));
        cmd.addAll(this.builder.buildSparkSubmitArgs());
        Iterator i$;
        if(CommandBuilderUtils.isWindows()) {
            List<String> winCmd = new ArrayList();
            i$ = cmd.iterator();

            while(i$.hasNext()) {
                String arg = (String)i$.next();
                winCmd.add(CommandBuilderUtils.quoteForBatchScript(arg));
            }

            cmd = winCmd;
        }

        ProcessBuilder pb = new ProcessBuilder((String[])cmd.toArray(new String[cmd.size()]));
        i$ = this.builder.childEnv.entrySet().iterator();

        while(i$.hasNext()) {
            Entry<String, String> e = (Entry)i$.next();
            pb.environment().put(e.getKey(), e.getValue());
        }

        if(this.workingDir != null) {
            pb.directory(this.workingDir);
        }

        CommandBuilderUtils.checkState(!this.redirectErrorStream || this.errorStream == null, "Cannot specify both redirectError() and redirectError(...) ", new Object[0]);
        CommandBuilderUtils.checkState(!this.redirectToLog || !this.redirectErrorStream && this.errorStream == null && this.outputStream == null, "Cannot used redirectToLog() in conjunction with other redirection methods.", new Object[0]);
        if(this.redirectErrorStream || this.redirectToLog) {
            pb.redirectErrorStream(true);
        }

        if(this.errorStream != null) {
            pb.redirectError(this.errorStream);
        }

        if(this.outputStream != null) {
            pb.redirectOutput(this.outputStream);
        }

        return pb;
    }

    private static class ArgumentValidator extends SparkSubmitOptionParser {
        private final boolean hasValue;

        ArgumentValidator(boolean hasValue) {
            this.hasValue = hasValue;
        }

        protected boolean handle(String opt, String value) {
            if(value == null && this.hasValue) {
                throw new IllegalArgumentException(String.format("'%s' does not expect a value.", new Object[]{opt}));
            } else {
                return true;
            }
        }

        protected boolean handleUnknown(String opt) {
            return true;
        }

        protected void handleExtraArgs(List<String> extra) {
        }
    }
}
