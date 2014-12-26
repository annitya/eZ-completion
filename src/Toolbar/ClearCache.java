package Toolbar;

import Framework.Console.Command;
import Framework.Console.ConsoleService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressManager;
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

        ConsoleService consoleService = new ConsoleService(project, "Clearing cache for environment: dev", false);
        Command command = new Command("cache:clear", "dev") { @Override public void success() {}};
        consoleService.seteZCommand(command);

        ProgressManager.getInstance().run(consoleService);
    }
}
