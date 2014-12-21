package Framework;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.Future;

public class CompletionPreloader implements ProjectComponent
{
    protected Future consoleServicePromise;
    protected Project project;
    protected eZCompletionContributor currentContributor;

    public eZCompletionContributor getCurrentContributor()
    {
        return currentContributor;
    }

    public void setCurrentContributor(eZCompletionContributor currentContributor)
    {
        this.currentContributor = currentContributor;
    }

    public CompletionPreloader(Project project)
    {
        this.project = project;
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
            createConsoleServicePromise();
        } catch (Exception ignored) {}
    }

    protected void createConsoleServicePromise()
    {
        consoleServicePromise = ApplicationManager.getApplication().executeOnPooledThread(new ConsoleService(project));
    }

    public CompletionContainer getCompletions(Boolean refresh)
    {
        try {
            if (refresh) {
                createConsoleServicePromise();
            }

            return (CompletionContainer)consoleServicePromise.get();
        } catch (Exception e) {
            return null;
        }
    }

    public void projectClosed() {}
}
