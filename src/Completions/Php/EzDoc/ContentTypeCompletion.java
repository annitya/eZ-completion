package Completions.Php.EzDoc;

import Completions.Php.Repository.Completion;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ContentTypeCompletion extends CompletionProvider<CompletionParameters>
{
    protected ArrayList<Completion> contentTypes;

    public ContentTypeCompletion(ArrayList<Completion> contentTypes)
    {
        this.contentTypes = contentTypes;
    }

    public PsiElementPattern.Capture<PsiElement> getMatcher()
    {
        return PlatformPatterns.psiElement().with(new EzDocMatcher("ContentTypes", false));
    }

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result)
    {
        result.addAllElements(contentTypes);
    }
}
