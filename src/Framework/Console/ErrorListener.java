package Framework.Console;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.HyperlinkEvent;

public class ErrorListener implements NotificationListener
{
    protected Project project;

    public ErrorListener(Project project)
    {
        this.project = project;
    }

    @Override
    public void hyperlinkUpdate(@NotNull Notification notification, @NotNull HyperlinkEvent event)
    {
        Settings.Service.getInstance(project).setDisabled(true);
        notification.hideBalloon();
    }
}
