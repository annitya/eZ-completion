package Framework;

import Completions.EzDoc.EzDocCompletion;
import Completions.EzDoc.ContentTypeCompletion;
import Completions.Repository.ParameterCompletion;
import com.intellij.codeInsight.completion.*;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;

public class eZCompletionContributor extends CompletionContributor
{
    protected CompletionContainer completions;

    public eZCompletionContributor()
    {
        CompletionPreloader.getInstance(Util.currentProject()).attachContributor(this);
    }

    public Boolean hasCompletions()
    {
        return completions != null;
    }

    public void registerCompletions(CompletionContainer completionContainer)
    {
        // Refresh if we already have completions.
        if (completions != null) {
            refreshCompletions(completionContainer);
            return;
        }
        completions = completionContainer;

        for (ParameterCompletion completion : completions.getList()) {
            extend(CompletionType.BASIC, PlatformPatterns.psiElement().with(completion.getMatcher()), completion);
        }

        EzDocCompletion annotationCompletion = new EzDocCompletion();
        extend(CompletionType.BASIC, PlatformPatterns.psiElement().with(annotationCompletion.getMatcher()), annotationCompletion);

        ContentTypeCompletion contentTypeCompletion = new ContentTypeCompletion(completions.getContentTypes());
        PsiElementPattern.Capture<PsiElement> elementPattern = PlatformPatterns.psiElement().with(contentTypeCompletion.getMatcher());
        extend(CompletionType.BASIC, elementPattern, contentTypeCompletion);
    }

    public void refreshCompletions(CompletionContainer completionContainer)
    {
        if (completionContainer == null) {
            return;
        }
        completions.refresh(completionContainer.getList());
    }
}