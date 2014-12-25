package Framework;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.progress.PerformInBackgroundOption;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class ConsoleService extends Task.Backgroundable implements PerformInBackgroundOption
{
    protected CompletionContainer completions;

    public ConsoleService(Project project, @NotNull String title, boolean canBeCancelled)
    {
        super(project, title, canBeCancelled);
    }

    protected String getConsole() throws Exception
    {
        String path = myProject.getBaseDir().getCanonicalPath() + File.separator + "ezpublish/console";
        if (!new File(path).isFile()) {
            throw new Exception("Unable to locate console-executable");
        }

        return path;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator)
    {
        try {
            String command = getConsole() + " ezcode:completion --env=dev";
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            Gson gson = new GsonBuilder().create();
            completions = gson.fromJson(reader, CompletionContainer.class);
            process.waitFor();
        } catch (Exception ignored) {}
    }

    @Override
    public void onSuccess()
    {
        CompletionPreloader.getInstance(myProject).completionsFetched(completions);
    }

    @Override
    public boolean shouldStartInBackground() { return true; }

    @Override
    public void processSentToBackground() {}
}
