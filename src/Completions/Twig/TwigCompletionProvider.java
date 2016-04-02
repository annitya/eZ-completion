package Completions.Twig;

import Completions.Php.Repository.Completion;
import Framework.CompletionContainer;
import Framework.CompletionPreloader;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import java.util.Set;

public class TwigCompletionProvider extends CompletionProvider<CompletionParameters>
{
    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result)
    {
        String simpleCompletion = (String)context.get("simpleCompletion");
        if (simpleCompletion != null && simpleCompletion.length() > 0) {
            result.addElement(new Completion().initalizeSimpleCompletion(simpleCompletion));
            return;
        }

        CompletionPreloader completionPreloader = CompletionPreloader.getInstance(parameters.getEditor().getProject());
        CompletionContainer completions = completionPreloader.getCurrentCompletions();

        Boolean contentTypeCompletion = (Boolean)context.get("contentTypeCompletion");
        if (contentTypeCompletion != null && contentTypeCompletion) {
            result.addAllElements(completions.getContentTypeIdentifierCompletions());
            return;
        }

        String contentClass = (String)context.get("contentTypeIdentifier");
        if (contentClass == null || contentClass.length() == 0) {
            return;
        }

        if (!completions.contentClassExists(contentClass)) {
            return;
        }

        Set<String> fieldIdentifiers = completions.getFieldIdentifiers(contentClass);

        for (final String fieldIdentifier : fieldIdentifiers) {
            result.addElement(new Completion().initalizeSimpleCompletion(fieldIdentifier));
        }
    }
}
