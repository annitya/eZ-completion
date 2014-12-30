package Framework.Console;

import Settings.Service;
import com.intellij.openapi.project.Project;
import com.jetbrains.php.config.commandLine.PhpCommandSettings;
import com.jetbrains.php.config.commandLine.PhpCommandSettingsBuilder;

import java.io.*;

abstract public class Command
{
    protected String command;
    protected String result;
    protected Process process;
    protected Project project;

    public Command(String command)
    {
        this.command = command;
    }

    public void setProject(Project project)
    {
        this.project = project;
    }

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
        commandSettings.addArgument("--env=" + getEnvironment());

        return commandSettings;
    }

    protected String readAll(InputStream stream) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String line, result = "";
        while ((line = reader.readLine()) != null) {
            result += line;
        }

        return result;
    }

    public void execute() throws Exception
    {
        process = createCommandSettings().createGeneralCommandLine().createProcess();

        result = readAll(process.getInputStream());
        process.waitFor();
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
