import com.google.gson.Gson;
import com.intellij.openapi.project.Project;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

public class ConsoleService implements Callable
{
    protected static String getConsole() throws Exception
    {
        Project project = CompletionPreloader.getCurrentProject();
        String path = project.getBaseDir().getCanonicalPath() + File.separator + "ezpublish/console";
        if (!new File(path).isFile()) {
            throw new Exception("Unable to locate console-executable");
        }

        return path;
    }

    @Override
    public CompletionContainer call() throws Exception
    {
        String command = getConsole() + " ezcode:completion";

        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String json = "", currentLine;
        while ((currentLine = reader.readLine()) != null) {
            json += currentLine;
        }

        Gson gson = new Gson();
        return gson.fromJson(json, CompletionContainer.class);
    }
}
