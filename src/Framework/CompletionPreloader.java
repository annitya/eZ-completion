package Framework;

import Framework.Console.Command;
import Framework.Console.ConsoleService;
import Framework.Console.RefreshCompletions;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class CompletionPreloader implements ProjectComponent
{
    protected Project project;
    protected eZCompletionContributor currentContributor;
    protected CompletionContainer completions;

    public CompletionPreloader(Project project)
    {
        this.project = project;
    }

    public static CompletionPreloader getInstance(Project project)
    {
        return project.getComponent(CompletionPreloader.class);
    }

    public CompletionContainer getCurrentCompletions() { return completions; }

    public void initComponent() {}

    public void disposeComponent() {}

    @NotNull
    public String getComponentName()
    {
        return "Framework.CompletionPreloader";
    }

    public void attachContributor(eZCompletionContributor contributor)
    {
        currentContributor = contributor;
        // Completions are not yet registered, but they are available.
        if (!currentContributor.hasCompletions() && completions != null) {
            currentContributor.registerCompletions(completions);
        }
    }

    public void projectOpened()
    {
        try {
            fetchCompletions();
        } catch (Exception ignored) {}
    }

    public void fetchCompletions()
    {
        ConsoleService consoleService = new ConsoleService(project, "Fetching eZ-completions", false);
        Command command = new RefreshCompletions("ezcode:completion");
        consoleService.seteZCommand(command);

        ProgressManager.getInstance().run(consoleService);
    }

    public void projectClosed() {}

    public void completionsFetched(CompletionContainer completionContainer)
    {
        // Contributor tried accessing completions, but they wheren't ready yet.
        if (currentContributor != null) {
            currentContributor.registerCompletions(completionContainer);
        }
        /**
         * Contributor hasn't been requested yet, lets store completions for later use.
         * See {@link Framework.CompletionPreloader#attachContributor(Framework.eZCompletionContributor)}
         */
        completions = completionContainer;
    }
}
