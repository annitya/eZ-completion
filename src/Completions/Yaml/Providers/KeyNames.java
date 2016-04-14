package Completions.Yaml.Providers;

import Completions.Yaml.YamlCompletion;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.project.Project;
import com.intellij.util.ProcessingContext;

import java.util.*;

public class KeyNames extends YamlCompletionProvider
{
    @Override
    protected Collection<LookupElement> getLookupElements(Project project, ProcessingContext context)
    {
        List<String> availableCompletions = new LinkedList<>(Arrays.asList("controller", "template", "match"));
        String[] existingKeys = (String[])context.get("existingKeys");
        if (existingKeys == null) {
            existingKeys = new String[0];
        }

        for (String existingCompletion : existingKeys) {
            availableCompletions.remove(existingCompletion);
        }

        List<LookupElement> completions = new ArrayList<>();
        for (String completion : availableCompletions) {
            completions.add(new YamlCompletion(completion));
        }

        return completions;
    }
}
