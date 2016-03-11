package Completions.Php.EzDoc;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class EzDocCompletion extends CompletionProvider<CompletionParameters>
{
    public PsiElementPattern.Capture<PsiElement> getMatcher()
    {
        return PlatformPatterns.psiElement().with(new EzDocMatcher("ContentType-annotation", true));
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
