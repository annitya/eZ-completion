package Completions.Content;

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

public class GetFieldValueCompletionProvider extends TranslationCompletionProvider
{
    @Override
    public PsiElementPattern.Capture<PsiElement> getMatcher()
    {
        MethodMatcher matcher = new MethodMatcher("\\eZ\\Publish\\API\\Repository\\Values\\Content\\Content", "getFieldValue", 0);
        return PlatformPatterns.psiElement().with(matcher);
    }

    @Override
    protected String getClassName(PsiElement psiElement)
    {
        String contentClass;
        try {
            PsiElement statement = Util.getMethodFromVariable(psiElement);
            if (statement == null) {
                return null;
            }
            Variable content = (Variable)statement.getFirstChild();
            contentClass = content.getType().getTypes().toArray()[0].toString().replace("#Z", "");
        } catch (Exception e) {
            return null;
        }

        return contentClass;
    }
}
