package Completions.EzDoc;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ContentTypeCompletion extends CompletionProvider<CompletionParameters>
{
    protected ArrayList<ContentType> contentTypes;

    public ContentTypeCompletion(ArrayList<ContentType> contentTypes)
    {
        this.contentTypes = contentTypes;
    }

    public EzDocMatcher getMatcher() { return new EzDocMatcher("ContentTypes", false); }

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result)
    {
        result.addAllElements(contentTypes);
    }
}
