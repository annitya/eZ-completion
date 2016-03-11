package Completions.Php;

import java.util.ArrayList;

import Completions.Php.Repository.Completion;
import Completions.Php.Repository.MethodMatcher;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class EzCompletionProvider extends CompletionProvider<CompletionParameters>
{
    protected ArrayList<Completion> completions;
    protected MethodMatcher matcher;

    public ArrayList<Completion> getCompletions()
    {
        return completions;
    }

    public PsiElementPattern.Capture<PsiElement> getMatcher()
    {
        return PlatformPatterns.psiElement().with(matcher);
    }

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result)
    {
        result.addAllElements(completions);
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof EzCompletionProvider && ((EzCompletionProvider)obj).matcher.equals(matcher);
    }
}
