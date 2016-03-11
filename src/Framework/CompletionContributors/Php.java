package Framework.CompletionContributors;

import Completions.Php.Content.FieldArrayCompletionProvider;
import Completions.Php.Content.GetFieldValueCompletionProvider;
import Completions.Php.Content.IsFieldEmptyCompletionProvider;
import Completions.Php.Content.TranslationCompletionProvider;
import Completions.Php.EzCompletionProvider;
import Completions.Php.EzDoc.ContentTypeCompletion;
import Completions.Php.EzDoc.EzDocCompletion;
import Framework.CompletionContainer;
import Framework.CompletionPreloader;
import com.intellij.codeInsight.completion.*;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.util.AsynchConsumer;

public class Php extends CompletionContributor
{
    protected CompletionContainer completions;

    public Php()
    {
        Php currentContributor = this;

        DataManager
                .getInstance()
                .getDataContextFromFocus()
                .doWhenDone(new AsynchConsumer<DataContext>()
                {
                    @Override
                    public void finished() {}

                    @Override
                    public void consume(DataContext dataContext)
                    {
                        Project project = CommonDataKeys.PROJECT.getData(dataContext);
                        CompletionPreloader.getInstance(project).attachContributor(currentContributor);
                    }
                });
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

        IsFieldEmptyCompletionProvider isFieldEmptyCompletionProvider = new IsFieldEmptyCompletionProvider();
        extend(CompletionType.BASIC, isFieldEmptyCompletionProvider.getMatcher(), isFieldEmptyCompletionProvider);

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