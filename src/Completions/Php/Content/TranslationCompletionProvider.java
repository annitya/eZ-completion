package Completions.Php.Content;

import Completions.Php.EzCompletionProvider;
import Completions.Php.Repository.Completion;
import Completions.Php.Repository.MethodMatcher;
import Framework.CompletionContainer;
import Framework.CompletionPreloader;
import Framework.Util;
import TypeProviders.Abstract.TypeKeys;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.psi.elements.Variable;
import org.jetbrains.annotations.NotNull;
import java.util.Set;

public class TranslationCompletionProvider extends EzCompletionProvider
{
    @Override
    public PsiElementPattern.Capture<PsiElement> getMatcher()
    {
        MethodMatcher matcher = new MethodMatcher("\\eZ\\Publish\\Core\\Helper\\TranslationHelper", "getTranslatedField", 1);
        return PlatformPatterns.psiElement().with(matcher);
    }

    protected String getClassName(PsiElement psiElement)
    {
        String contentClass;
        try {
            PsiElement[] parameterList = Util.getParameters(psiElement);
            Variable content = (Variable)parameterList[0];
            contentClass = TypeKeys.getTypeString(content, TypeKeys.CONTENT_KEY);
        } catch (Exception e) {
            return null;
        }

        return contentClass;
    }

    @Override
    protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result)
    {
        PsiElement psiElement = parameters.getPosition();
        String contentClass = getClassName(psiElement);
        if (contentClass == null) {
            return;
        }

        CompletionPreloader preloader = CompletionPreloader.getInstance(psiElement.getProject());
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
