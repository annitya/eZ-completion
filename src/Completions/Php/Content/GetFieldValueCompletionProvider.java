package Completions.Php.Content;

import Completions.Php.Repository.MethodMatcher;
import Framework.Util;
import TypeProviders.Abstract.TypeKeys;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.Variable;

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
        try {
            PsiElement statement = Util.getMethodFromVariable(psiElement);
            if (statement == null) {
                return null;
            }
            Variable content = (Variable)statement.getFirstChild();
            return TypeKeys.getTypeString(content, TypeKeys.CONTENT_KEY);
        } catch (Exception e) {
            return null;
        }
    }
}
