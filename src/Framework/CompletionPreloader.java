package Framework;

import Framework.CompletionContributors.Php;
import Framework.Console.Command;
import Framework.Console.ConsoleCommandFactory;
import Framework.Console.ConsoleService;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class CompletionPreloader implements ProjectComponent
{
    protected Project project;
    protected Php currentContributor;
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

    public void attachContributor(Php contributor)
    {
        currentContributor = contributor;
        // Completions are not yet registered, but they are available.
        if (!currentContributor.hasCompletions() && completions != null) {
            currentContributor.registerCompletions(completions);
        }
    }

    public void projectOpened()
    {
        // No autofetch if plugin is disabled.
        if (Settings.Service.getInstance(project).getDisabled()) {
            return;
        }

        try {
            // Enable caching of command-result on startup.
            ConsoleService consoleService = ConsoleCommandFactory.createConsoleCommand(ConsoleCommandFactory.CommandName.REFRESH_COMPLETIONS, project);
            Command command = consoleService.getEzCommand();
            command.setUseFileCache(true);
            consoleService.setEzCommand(command);
            consoleService.queue();
        } catch (Exception ignored) {}
    }

    public void fetchCompletions()
    {
        ConsoleCommandFactory.runConsoleCommand(ConsoleCommandFactory.CommandName.REFRESH_COMPLETIONS, project);
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
         * See {@link Framework.CompletionPreloader#attachContributor(Php)}
         */
        completions = completionContainer;
    }
}
