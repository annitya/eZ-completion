package Completions.Php.Content;

import Completions.Php.Repository.Completion;
import Framework.CompletionContainer;
import Framework.CompletionPreloader;
import TypeProviders.Abstract.TypeKeys;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.psi.elements.ArrayAccessExpression;
import org.jetbrains.annotations.NotNull;
import java.util.Set;

public class FieldArrayCompletionProvider extends CompletionProvider<CompletionParameters>
{
    public PsiElementPattern.Capture<PsiElement> getMatcher()
    {
        return PlatformPatterns.psiElement().with(new ArrayMatcher("Array of fields"));
    }

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result)
    {
        ArrayAccessExpression arrayAccess = ArrayMatcher.getArrayExpression(parameters.getPosition());
        if (arrayAccess == null) {
            return;
        }

        String contentClass = TypeKeys.getArrayAccessTypeString(arrayAccess, TypeKeys.FIELD_KEY);
        if (contentClass == null) {
            contentClass = TypeKeys.getArrayAccessTypeString(arrayAccess, TypeKeys.CONTENT_KEY);
        }

        CompletionPreloader preloader = CompletionPreloader.getInstance(arrayAccess.getProject());
        CompletionContainer completions = preloader.getCurrentCompletions();
        Set<String> fieldIdentifiers = completions.getFieldIdentifiers(contentClass);
        if (fieldIdentifiers == null) {
            return;
        }

        for (final String fieldIdentifier : fieldIdentifiers) {
            result.addElement(new Completion().initalizeSimpleCompletion(fieldIdentifier));
        }
    }
}
