package Framework;

import Completions.Content.FieldArrayCompletionProvider;
import Completions.Content.GetFieldValueCompletionProvider;
import Completions.Content.TranslationCompletionProvider;
import Completions.EzCompletionProvider;
import Completions.EzDoc.ContentTypeCompletion;
import Completions.EzDoc.EzDocCompletion;
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
        if (completionContainer == null) {
            return;
        }
        completions = completionContainer;

        for (EzCompletionProvider completion : completions.getList()) {
            extend(CompletionType.BASIC, completion.getMatcher(), completion);
        }

        EzDocCompletion annotationCompletion = new EzDocCompletion();
        extend(CompletionType.BASIC, annotationCompletion.getMatcher(), annotationCompletion);

        ContentTypeCompletion contentTypeCompletion = new ContentTypeCompletion(completions.getContentTypes());
        extend(CompletionType.BASIC, contentTypeCompletion.getMatcher(), contentTypeCompletion);

        TranslationCompletionProvider translationCompletion = new TranslationCompletionProvider();
        extend(CompletionType.BASIC, translationCompletion.getMatcher(), translationCompletion);

        GetFieldValueCompletionProvider fieldValueCompletion = new GetFieldValueCompletionProvider();
        extend(CompletionType.BASIC, fieldValueCompletion.getMatcher(), fieldValueCompletion);

        FieldArrayCompletionProvider fieldArrayCompletion = new FieldArrayCompletionProvider();
        extend(CompletionType.BASIC, fieldArrayCompletion.getMatcher(), fieldArrayCompletion);
    }

    public void refreshCompletions(CompletionContainer completionContainer)
    {
        if (completionContainer == null) {
            return;
        }
        completions.refresh(completionContainer.getList());
    }
}