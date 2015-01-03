package Framework.Console;

import com.intellij.openapi.project.Project;
import com.jetbrains.php.config.commandLine.PhpCommandSettings;

public class QueryCommand extends Command
{
    protected String methodFqn;
    protected String queryCode;

    public QueryCommand(String command, Project project)
    {
        super(command, project);
    }

    public void setParameters(String methodFqn, String queryCode)
    {
        this.methodFqn = methodFqn;
        this.queryCode = queryCode;
    }

    @Override
    public void success()
    {
        result = result.trim();
    }

    @Override
    protected PhpCommandSettings createCommandSettings() throws Exception
    {
        PhpCommandSettings commandSettings = super.createCommandSettings();
        commandSettings.addArgument("--methodFqn=" + methodFqn);
        commandSettings.addArgument("--queryCode=" + queryCode);

        return commandSettings;
    }
}
