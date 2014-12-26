package Framework.Console;

import Framework.CompletionContainer;
import Framework.CompletionPreloader;
import Settings.Service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RefreshCompletions extends Command
{
    public RefreshCompletions(String command, String environment)
    {
        super(command, environment);
    }

    @Override
    public void success()
    {
        Gson gson = new GsonBuilder().create();
        CompletionContainer completions = gson.fromJson(result, CompletionContainer.class);
        CompletionPreloader.getInstance(project).completionsFetched(completions);
    }

    @Override
    public String toString()
    {
        String selectedLanguage = Service.getInstance(project).getLanguage();
        if (selectedLanguage == null) {
            return super.toString();
        }

        return super.toString() + " --language=" + selectedLanguage;
    }
}
