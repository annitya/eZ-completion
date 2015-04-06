package Framework;

import Framework.Console.ConsoleCommandFactory;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.jetbrains.php.config.PhpProjectConfigurationFacade;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
        // No autofetch if plugin is disabled.
        if (Settings.Service.getInstance(project).getDisabled()) {
            return;
        }

        try {
            fetchCompletions();
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
         * See {@link Framework.CompletionPreloader#attachContributor(Framework.eZCompletionContributor)}
         */
        completions = completionContainer;

        checkAddIncludePath();
    }

    protected void checkAddIncludePath()
    {
        PhpProjectConfigurationFacade facade = PhpProjectConfigurationFacade.getInstance(this.project);
        List<String> includePaths = facade.getIncludePath();
        String completionIncludePath = completions.getIncludePath();

        if (includePaths.contains(completionIncludePath)) {
            includePaths.remove(completionIncludePath);
            facade.setIncludePath(includePaths);
        }

        includePaths.add(completionIncludePath);
        facade.setIncludePath(includePaths);
    }
}
