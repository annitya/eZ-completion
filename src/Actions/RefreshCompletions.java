package Actions;

import Framework.CompletionPreloader;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class RefreshCompletions extends AnAction
{
    public void actionPerformed(@NotNull AnActionEvent e)
    {
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        CompletionPreloader.getInstance(project).fetchCompletions();
    }
}
