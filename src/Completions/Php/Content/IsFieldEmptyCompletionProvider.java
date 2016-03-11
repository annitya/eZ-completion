package Completions.Php.Content;

import Completions.Php.Repository.MethodMatcher;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;

public class IsFieldEmptyCompletionProvider extends TranslationCompletionProvider
{
    @Override
    public PsiElementPattern.Capture<PsiElement> getMatcher()
    {
        MethodMatcher matcher = new MethodMatcher("\\eZ\\Publish\\Core\\Helper\\FieldHelper", "isFieldEmpty", 1);
        return PlatformPatterns.psiElement().with(matcher);
    }
}
