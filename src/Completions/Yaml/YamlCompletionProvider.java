package Completions.Yaml;

import Completions.Repository.Completion;
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

        result.addAllElements(getLookupElements(project, context));
    }
}
