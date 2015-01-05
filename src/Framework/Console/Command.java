package Framework.Console;

import Settings.Service;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.remote.RemoteSdkCredentials;
import com.jetbrains.php.config.commandLine.PhpCommandSettings;
import com.jetbrains.php.config.commandLine.PhpCommandSettingsBuilder;
import com.jetbrains.php.remote.PhpRemoteProcessUtil;
import com.jetbrains.php.remote.interpreter.PhpRemoteSdkAdditionalData;

import java.io.*;

abstract public class Command
{
    protected String command;
    protected String result;
    protected Process process;
    protected Project project;
    protected Boolean async = false;

    public Command(String command, Project project)
    {
        this.command = command;
        this.project = project;
    }

    public void setAsync(Boolean async)
    {
        this.async = async;
    }

    public Process getProcess() { return process; }

    abstract public void success();

    @Override
    public String toString()
    {
        return command;
    }

    protected String getConsole() throws Exception
    {
        String selectedExecutable = Service.getInstance(project).getExecutable();
        String path = project.getBaseDir().getCanonicalPath() + File.separator + selectedExecutable;
        if (!new File(path).isFile()) {
            throw new Exception("Unable to locate console-executable");
        }

        return path;
    }

    public String getEnvironment()
    {
        return Service.getInstance(project).getEnvironment();
    }

    protected PhpCommandSettings createCommandSettings() throws Exception
    {
        PhpCommandSettings commandSettings = PhpCommandSettingsBuilder.create(project);

        commandSettings.setScript(getConsole());
        commandSettings.addArgument(command);
        commandSettings.addArgument("--no-ansi");
        commandSettings.addArgument("--env=" + getEnvironment());

        return commandSettings;
    }

    protected String readAll(InputStream stream) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String line, result = "";
        while ((line = reader.readLine()) != null) {
            result += line.trim();
        }

        return result;
    }

    public void execute(ProgressIndicator indicator, String title) throws Exception
    {
        PhpCommandSettings commandSettings = createCommandSettings();
        GeneralCommandLine generalCommandLine = commandSettings.createGeneralCommandLine();

        if (commandSettings.isRemote()) {
            PhpRemoteSdkAdditionalData remoteSdkAdditionalData = (PhpRemoteSdkAdditionalData)commandSettings.getAdditionalData();
            if (remoteSdkAdditionalData == null) {
                throw new Exception("Unable to fetch remote sdk-data!");
            }

            RemoteSdkCredentials remoteSdkCredentials = remoteSdkAdditionalData.getRemoteSdkCredentials(false);
            process = PhpRemoteProcessUtil.createRemoteProcess(project, remoteSdkCredentials, generalCommandLine, true);
            indicator.setText(title);
        }
        else {
            process = generalCommandLine.createProcess();
        }

        result = readAll(process.getInputStream());
        process.waitFor();

        if (async) {
            return;
        }
        if (process.exitValue() != 0) {
            throw new Exception(getErrorMessage());
        }
    }

    protected String getErrorMessage()
    {
        if (result != null && result.length() > 0) {
            return result;
        }

        try {
            return readAll(process.getErrorStream());
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}
