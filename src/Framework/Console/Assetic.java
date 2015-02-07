package Framework.Console;

import com.intellij.openapi.project.Project;

public class Assetic extends Command
{
    public Assetic(String command, Project project)
    {
        super(command, project);
    }

    @Override
    public void success() {}

    @Override
    protected void processExited() throws Exception {}
}
