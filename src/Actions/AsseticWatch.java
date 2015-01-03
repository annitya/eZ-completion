package Actions;

import Framework.Console.Command;
import Framework.Console.ConsoleService;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class AsseticWatch extends AnAction
{
    static Command command;

    @Override
    public void update(@NotNull AnActionEvent e)
    {
        Icon icon = command != null ? AllIcons.Actions.Suspend : AllIcons.Actions.Rerun;
        e.getPresentation().setIcon(icon);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e)
    {
        if (command != null && command.getProcess() != null) {
            command.getProcess().destroy();
            command = null;

            return;
        }

        Project project = e.getProject();
        if (project == null) {
            return;
        }

        command = new Command("assetic:watch", project) { @Override public void success() {}};
        command.setAsync(true);
        ConsoleService consoleService = new ConsoleService(project, "Watching assets for environment: " + command.getEnvironment(), false);
        consoleService.seteZCommand(command);
        consoleService.queue();
    }
}
