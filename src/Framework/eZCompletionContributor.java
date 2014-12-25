package Framework;

import Completions.ParameterCompletion;
import com.intellij.codeInsight.completion.*;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.patterns.PlatformPatterns;

public class eZCompletionContributor extends CompletionContributor
{
    protected CompletionContainer completions;

    public eZCompletionContributor()
    {
        DataContext context = DataManager.getInstance().getDataContextFromFocus().getResultSync();
        Project project = CommonDataKeys.PROJECT.getData(context);
        CompletionPreloader.getInstance(project).attachContributor(this);
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
        completions.refresh(completionContainer.getList());
    }
}