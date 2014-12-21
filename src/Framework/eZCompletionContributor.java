package Framework;

import Completions.ParameterCompletion;
import com.intellij.codeInsight.completion.*;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.patterns.PlatformPatterns;

import java.util.ArrayList;

public class eZCompletionContributor extends CompletionContributor
{
    protected CompletionContainer completionContainer;

    public eZCompletionContributor()
    {
        completionContainer = getCurrentPreloader().getCompletions(false);
        registerCompletions(completionContainer.getList());
        getCurrentPreloader().setCurrentContributor(this);
    }

    protected void registerCompletions(ArrayList<ParameterCompletion> completions)
    {
        for (ParameterCompletion completion : completions) {
            extend(CompletionType.BASIC, PlatformPatterns.psiElement().with(completion.getMatcher()), completion);
        }
    }

    public void refreshCompletions()
    {
        CompletionContainer newContainer = getCurrentPreloader().getCompletions(true);
        ArrayList<ParameterCompletion> newCompletions = completionContainer.refresh(newContainer.getList());
        registerCompletions(newCompletions);
    }

    protected CompletionPreloader getCurrentPreloader()
    {
        return getCurrentProject().getComponent(CompletionPreloader.class);
    }

    protected Project getCurrentProject()
    {
        DataContext context = DataManager.getInstance().getDataContextFromFocus().getResultSync();
        return CommonDataKeys.PROJECT.getData(context);
    }
}