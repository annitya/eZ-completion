package Framework.Console;

import Settings.Service;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.jetbrains.php.config.commandLine.PhpCommandSettings;
import com.jetbrains.php.config.commandLine.PhpCommandSettingsBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

abstract public class Command
{
    protected String command;
    protected String result = "";
    protected Process process;
    protected Project project;
    protected Integer expectedResultLength = 0;
    protected ProgressIndicator indicator;
    protected String title;
    protected Boolean useFileCache = false;
    protected Boolean storeResult = false;

    public Command(String command, Project project)
    {
        this.command = command;
        this.project = project;
    }

    public Process getProcess() { return process; }

    public void setTitle(String title) { this.title = title; }

    public void setExpectedResultLength(Integer expectedResultLength) { this.expectedResultLength = expectedResultLength; }

    public void setProgressIndicator(ProgressIndicator indicator) { this.indicator = indicator; }

    public void setUseFileCache(Boolean useFileCache) { this.useFileCache = useFileCache; }

    public void setStoreResult(Boolean storeResult) { this.storeResult = storeResult; }

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
        PhpCommandSettings commandSettings = PhpCommandSettingsBuilder.create(project, false);

        commandSettings.setScript(getConsole());
        commandSettings.addArgument(command);
        commandSettings.addArgument("--no-ansi");

        String environment = getEnvironment().trim();
        if (environment.length() > 0) {
            commandSettings.addArgument("--env=" + getEnvironment());
        }

        return commandSettings;
    }

    protected String readAllTextProgress(InputStream stream) throws IOException, InterruptedException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String currentLine;
        String originalText = indicator.getText();
        StringBuilder temp = new StringBuilder();

        while ((currentLine = reader.readLine()) != null) {
            temp.append(currentLine);
            indicator.setText(currentLine);

            if (!reader.ready()) {
                Thread.sleep(500);
                indicator.setText(originalText);
            }
        }

        return temp.toString();
    }

    protected String readAll(InputStream stream) throws IOException, InterruptedException
    {
        if (expectedResultLength == 0) {
            return readAllTextProgress(stream);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder temp = new StringBuilder();
        int currentChar;

        while (true) {
            currentChar = reader.read();

            if (currentChar == -1) {
                break;
            }

            temp.append((char)currentChar);

            if (expectedResultLength > 0) {
                indicator.setFraction((double)temp.length() / (double)expectedResultLength);
            }
        }

        indicator.setFraction(1);

        return temp.toString();
    }

    public void execute() throws Exception
    {
        if (useFileCache) {
            loadCachedResult();
        }

        if (result != null && result.length() > 0) {
            return;
        }

        createProcess();

        result = readAll(process.getInputStream());
        process.waitFor();
        processExited();

        if (storeResult) {
            cacheResult();
        }
    }

    protected void createProcess() throws Exception
    {
        PhpCommandSettings commandSettings = createCommandSettings();
        if (commandSettings.isRemote()) {
            process = RemoteCommand.createProcess(commandSettings, project);
            indicator.setText(title);
        }
        else {
            GeneralCommandLine generalCommandLine = commandSettings.createGeneralCommandLine();
            process = generalCommandLine.createProcess();
        }
    }

    protected void processExited() throws Exception
    {
        if (process.exitValue() != 0)   {
            throw new Exception(getErrorMessage(result));
        }
    }

    protected String getErrorMessage(String result)
    {
        try {
            String output = readAll(process.getErrorStream());
            if (output.length() == 0) {
                return result;
            }
            return output;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    protected void loadCachedResult()
    {
        if (!ensureDirectory()) {
            return;
        }

        try {
            byte[] encoded = Files.readAllBytes(Paths.get(buildFileName()));
            result = new String(encoded, StandardCharsets.UTF_8);
        }
        catch (Exception ignored) {}
    }

    protected void cacheResult()
    {
        if (result == null || result.length() == 0) {
            return;
        }

        if (!ensureDirectory()) {
            return;
        }

        try {
            PrintWriter out = new PrintWriter(buildFileName());
            out.println(result);
            out.close();
        }
        catch (Exception ignored) {}
    }

    protected Boolean ensureDirectory()
    {
        File directory = new File(buildDirectoryName());
        if (directory.isDirectory()) {
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (directory.isFile()) {
            return false;
        }

        return directory.mkdirs();
    }

    protected String buildDirectoryName()
    {
        return
                PathManager.getPluginsPath() +
                        File.separator +
                        "eZPlugin" +
                        File.separator +
                        project.getLocationHash();
    }

    protected String buildFileName()
    {
        return
                buildDirectoryName() +
                        File.separator +
                        command;
    }
}
