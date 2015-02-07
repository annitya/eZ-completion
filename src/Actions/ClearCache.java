package Actions;

import Framework.Console.ConsoleCommandFactory;
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

        ConsoleCommandFactory.runConsoleCommand(ConsoleCommandFactory.CommandName.CLEAR_CACHE, project);
    }
}
