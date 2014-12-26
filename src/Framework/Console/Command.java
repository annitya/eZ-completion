package Framework.Console;

import com.intellij.openapi.project.Project;

abstract public class Command
{
    protected String command;
    protected String environment = "dev";
    protected String result;
    protected Project project;

    public Command(String command, String environment)
    {
        this.command = command;
        this.environment = environment;
    }

    public void setResult(String result) { this.result = result; }

    public void setProject(Project project) { this.project = project; }

    abstract public void success();

    @Override
    public String toString()
    {
        return command + " --env=" + environment;
    }
}
