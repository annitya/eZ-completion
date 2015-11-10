package Completions.Content;

import Framework.CompletionContainer;
import Framework.CompletionPreloader;
import TypeProviders.Abstract.TypeKeys;
import TypeProviders.Content.Fields.ArrayOfFieldsTypeProvider;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.psi.elements.ArrayAccessExpression;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
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

        String contentClass = TypeKeys.getTypeString(arrayAccess, TypeKeys.FIELD_KEY);
        if (contentClass == null) {
            contentClass = TypeKeys.getTypeString(arrayAccess, TypeKeys.CONTENT_KEY);
        }

        CompletionPreloader preloader = CompletionPreloader.getInstance(arrayAccess.getProject());
        CompletionContainer completions = preloader.getCurrentCompletions();
        Set<String> fieldIdentifiers = completions.getFieldIdentifiers(contentClass);
        if (fieldIdentifiers == null) {
            return;
        }

        for (final String fieldIdentifier : fieldIdentifiers) {
            result.addElement(new LookupElement()
            {
                @NotNull
                @Override
                public String getLookupString()
                {
                    return fieldIdentifier;
                }
            });
        }
    }
}
