package Completions.Yaml.Providers;

import Framework.CompletionContainer;
import Framework.CompletionPreloader;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.project.Project;
import com.intellij.util.ProcessingContext;
import java.util.ArrayList;
import java.util.Collection;

public class MatchValues extends YamlCompletionProvider
{
    @Override
    protected Collection<LookupElement> getLookupElements(Project project, ProcessingContext context)
    {
        String type = (String)context.get("type");
        String value = (String)context.get("value");
        if (type == null || value == null) {
            return null;
        }

        if (!type.equals("ContentType") && !type.equals("ParentContentType")) {
            return null;
        }

        if  (!value.equals("Identifier")) {
            return null;
        }

        CompletionPreloader preloader = CompletionPreloader.getInstance(project);
        CompletionContainer completions = preloader.getCurrentCompletions();

        return new ArrayList<>(completions.getContentTypes());
    }
}
