package Framework;

import Completions.Completion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.project.Project;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

public class ConsoleService implements Callable
{
    Project project;

    public ConsoleService(Project project)
    {
        this.project = project;
    }

    protected String getConsole() throws Exception
    {
        String path = project.getBaseDir().getCanonicalPath() + File.separator + "ezpublish/console";
        if (!new File(path).isFile()) {
            throw new Exception("Unable to locate console-executable");
        }

        return path;
    }

    @Override
    public CompletionContainer call() throws Exception
    {
        String command = getConsole() + " ezcode:completion --env=dev";

        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Completion.class, new Completion());
        Gson gson = gsonBuilder.create();

        CompletionContainer completions = gson.fromJson(reader, CompletionContainer.class);
        process.waitFor();

        return completions;
    }
}
