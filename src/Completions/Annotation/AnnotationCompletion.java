package Completions.Annotation;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class AnnotationCompletion extends CompletionProvider<CompletionParameters>
{
    public AnnotationMatcher getMatcher()
    {
        return new AnnotationMatcher("ContentType-annotation", true);
    }

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result)
    {
        result.addElement(new LookupElement()
        {
            @NotNull
            @Override
            public String getLookupString()
            {
                return "ContentType";
            }
        });
    }
}
