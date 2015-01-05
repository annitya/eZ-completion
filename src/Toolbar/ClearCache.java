package Toolbar;

import Framework.Console.Command;
import Framework.Console.ConsoleService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ClearCache extends AnAction
{
    @Override
    public void actionPerformed(@NotNull AnActionEvent e)
    {
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        Command command = new Command("cache:clear", project) { @Override public void success() {}};
        ConsoleService consoleService = new ConsoleService(project, "Clearing cache for environment: " + command.getEnvironment(), false);
        consoleService.seteZCommand(command);
        consoleService.queue();
    }
}
