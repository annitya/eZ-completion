package Completions.Twig;

import Completions.Repository.Completion;
import Framework.CompletionContainer;
import Framework.CompletionPreloader;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class TwigCompletionProvider extends CompletionProvider<CompletionParameters>
{
    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result)
    {
        String contentClass;
        try {
            contentClass = (String)context.get("contentTypeIdentifier");
        }
        catch (Exception e) {
            return;
        }

        CompletionPreloader completionPreloader = CompletionPreloader.getInstance(parameters.getEditor().getProject());
        CompletionContainer completions = completionPreloader.getCurrentCompletions();
        if (!completions.contentClassExists(contentClass)) {
            return;
        }

        Set<String> fieldIdentifiers = completions.getFieldIdentifiers(contentClass);

        for (final String fieldIdentifier : fieldIdentifiers) {
            result.addElement(new Completion().initalizeSimpleCompletion(fieldIdentifier));
        }
    }
}
