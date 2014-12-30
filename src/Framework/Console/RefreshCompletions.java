package Framework.Console;

import Framework.CompletionContainer;
import Framework.CompletionPreloader;
import Settings.Service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jetbrains.php.config.commandLine.PhpCommandSettings;

public class RefreshCompletions extends Command
{
    protected CompletionContainer completions;
    public RefreshCompletions(String command) { super(command); }

    @Override
    public void success()
    {
        CompletionPreloader.getInstance(project).completionsFetched(completions);
    }

    @Override
    public PhpCommandSettings createCommandSettings() throws Exception
    {
        PhpCommandSettings commandSettings = super.createCommandSettings();
        String selectedLanguage = Service.getInstance(project).getLanguage();
        commandSettings.addArgument("--language=" + selectedLanguage);

        return commandSettings;
    }

    @Override
    public void execute() throws Exception
    {
        super.execute();

        Gson gson = new GsonBuilder().create();
        completions = gson.fromJson(result, CompletionContainer.class);
    }
}
