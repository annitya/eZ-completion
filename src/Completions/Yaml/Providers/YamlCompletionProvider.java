package Completions.Yaml.Providers;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.project.Project;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;

abstract public class YamlCompletionProvider extends CompletionProvider<CompletionParameters>
{
    abstract protected Collection<LookupElement> getLookupElements(Project project, ProcessingContext context);

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result)
    {
        Project project = parameters.getEditor().getProject();
        if (project == null) {
            return;
        }

        Collection<LookupElement> lookupElements = getLookupElements(project, context);
        if (lookupElements == null) {
            return;
        }

        result.addAllElements(lookupElements);
    }
}
