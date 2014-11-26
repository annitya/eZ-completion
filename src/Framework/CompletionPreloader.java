package Framework;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.Future;

public class CompletionPreloader implements ProjectComponent
{
    protected Future consoleServicePromise;
    protected static Project currentProject;

    public CompletionPreloader(Project project)
    {
        currentProject = project;
    }

    public void initComponent() {}

    public void disposeComponent() {}

    @NotNull
    public String getComponentName()
    {
        return "Framework.CompletionPreloader";
    }

    public void projectOpened()
    {
        try {
            consoleServicePromise = ApplicationManager.getApplication().executeOnPooledThread(new ConsoleService());
        } catch (Exception ignored) {}
    }

    public CompletionContainer getCompletions()
    {
        try {
            return (CompletionContainer)consoleServicePromise.get();
        } catch (Exception e) {
            return null;
        }
    }

    public static Project getCurrentProject()
    {
        return currentProject;
    }

    public void projectClosed() {}
}
