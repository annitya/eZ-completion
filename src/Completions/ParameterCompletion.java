package Completions;

import java.util.ArrayList;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class ParameterCompletion extends CompletionProvider<CompletionParameters>
{
    protected ArrayList<Completion> completions;
    protected MethodMatcher matcher;

    public ArrayList<Completion> getCompletions()
    {
        return completions;
    }

    public MethodMatcher getMatcher()
    {
        return matcher;
    }

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result)
    {
        result.addAllElements(completions);
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof ParameterCompletion && ((ParameterCompletion)obj).matcher.equals(matcher);
    }
}
