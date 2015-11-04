package Completions.Content;

import Completions.EzCompletionProvider;
import Completions.Repository.MethodMatcher;
import Framework.CompletionContainer;
import Framework.CompletionPreloader;
import Framework.Util;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.psi.elements.Variable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Set;

public class TranslationCompletionProvider extends EzCompletionProvider
{
    @Override
    public PsiElementPattern.Capture<PsiElement> getMatcher()
    {
        MethodMatcher matcher = new MethodMatcher("\\eZ\\Publish\\Core\\Helper\\TranslationHelper", "getTranslatedField", 1);
        return PlatformPatterns.psiElement().with(matcher);
    }

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result)
    {
        PsiElement psiElement = parameters.getPosition();
        String contentClass;
        try {
            PsiElement[] parameterList = Util.getParameters(psiElement);
            Variable content = (Variable)parameterList[0];
            contentClass = content.getType().getTypes().toArray()[0].toString().replace("#Z", "");
        } catch (Exception e) {
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
