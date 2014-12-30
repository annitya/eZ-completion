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
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator)
    {
        try {
            eZCommand.execute(indicator, myTitle);
        } catch (Exception e){
            notifyFailure(e.getMessage());
        }
    }

    @Override
    public void onSuccess()
    {
        eZCommand.success();
    }

    protected void notifyFailure(String message)
    {
        NotificationGroup group = new NotificationGroup("EZ_GROUP", NotificationDisplayType.STICKY_BALLOON, true);
        Notification notification = group.createNotification(
                "Failed: " + eZCommand,
                message,
                NotificationType.ERROR,
                null
        );
        Notifications.Bus.notify(notification);
    }

    @Override
    public boolean shouldStartInBackground() { return true; }

    @Override
    public void processSentToBackground() {}
}
