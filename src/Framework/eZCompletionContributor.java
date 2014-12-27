package Framework;

import Completions.ParameterCompletion;
import com.intellij.codeInsight.completion.*;
import com.intellij.patterns.PlatformPatterns;

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
    }

    public void refreshCompletions(CompletionContainer completionContainer)
    {
        if (completionContainer == null) {
            return;
        }
        completions.refresh(completionContainer.getList());
    }
}