package Framework.Console;

import Framework.CompletionContainer;
import com.intellij.notification.*;
import com.intellij.openapi.progress.PerformInBackgroundOption;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ConsoleService extends Task.Backgroundable implements PerformInBackgroundOption
{
    protected CompletionContainer completions;
    protected Command eZCommand;

    public ConsoleService(Project project, @NotNull String title, boolean canBeCancelled)
    {
        super(project, title, canBeCancelled);
    }

    public void seteZCommand(Command command)
    {
        eZCommand = command;
        command.setTitle(myTitle);
    }

    public Command geteZCommand() { return eZCommand; }

    @Override
    public void run(@NotNull ProgressIndicator indicator)
    {
        eZCommand.setProgressIndicator(indicator);

        try {
            eZCommand.execute();
        } catch (Exception e){
            notifyFailure(e.getMessage(), eZCommand.toString(), myProject);
        }
    }

    @Override
    public void onSuccess()
    {
        eZCommand.success();
    }

    public static void notifyFailure(String message, String command, Project project)
    {
        message += "\n\n<a href='http://google.no'>Disable plugin for this project.</a>";

        NotificationGroup group = new NotificationGroup("EZ_GROUP", NotificationDisplayType.STICKY_BALLOON, true);
        Notification notification = group.createNotification(
                "Failed: " + command,
                message,
                NotificationType.ERROR,
                new ErrorListener(project)
        );
        Notifications.Bus.notify(notification);
    }

    @Override
    public boolean shouldStartInBackground() { return true; }

    @Override
    public void processSentToBackground() {}
}
