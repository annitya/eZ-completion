package Completions.Content;

import Framework.CompletionContainer;
import Framework.CompletionPreloader;
import TypeProviders.ArrayOfFieldsTypeProvider;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
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
        PsiElement psiElement = parameters.getPosition();
        PhpType variableType = ArrayMatcher.getArrayVariableType(psiElement);
        if (variableType == null) {
            return;
        }

        Set<String> types = variableType.getTypes();
        if (types.size() > 1) {
            return;
        }

        String[] typeParts = types.toArray()[0].toString().split("#Z");
        if (typeParts.length != 3) {
            return;
        }

        String contentClass = typeParts[1];
        String fieldTypeIdentifier = typeParts[2];
        if (!fieldTypeIdentifier.equals(ArrayOfFieldsTypeProvider.FIELD_LIST_IDENTIFIER)) {
            return;
        }

        CompletionPreloader preloader = CompletionPreloader.getInstance(psiElement.getProject());
        CompletionContainer completions = preloader.getCurrentCompletions();
        HashMap<String, HashMap<String, String>> contentTypeFields = completions.getContentTypeFields();
        if (!contentTypeFields.containsKey(contentClass)) {
            return;
        }
        Set<String> fieldIdentifiers = contentTypeFields.get(contentClass).keySet();
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
