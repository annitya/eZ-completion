package Completions.Content;

import Framework.Util;
import TypeProviders.Abstract.TypeKeys;
import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.psi.elements.ArrayAccessExpression;
import org.jetbrains.annotations.NotNull;

public class ArrayMatcher extends PatternCondition<PsiElement>
{
    public ArrayMatcher(String debugMethodName)
    {
        super(debugMethodName);
    }

    public static ArrayAccessExpression getArrayExpression(PsiElement psiElement)
    {
        if (!Util.withinQuotes(psiElement)) {
            return null;
        }

        return PsiTreeUtil.getParentOfType(psiElement, ArrayAccessExpression.class);
    }

    @Override
    public boolean accepts(@NotNull PsiElement psiElement, ProcessingContext context)
    {
        ArrayAccessExpression arrayExpression = getArrayExpression(psiElement);
        return TypeKeys.isField(arrayExpression) || TypeKeys.isContent(arrayExpression);
    }
}
