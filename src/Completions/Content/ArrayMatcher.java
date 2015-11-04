package Completions.Content;

import Framework.Util;
import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.psi.elements.ArrayAccessExpression;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import org.jetbrains.annotations.NotNull;

public class ArrayMatcher extends PatternCondition<PsiElement>
{
    public ArrayMatcher(String debugMethodName)
    {
        super(debugMethodName);
    }

    public static PhpType getArrayVariableType(PsiElement psiElement)
    {
        if (!Util.withinQuotes(psiElement)) {
            return null;
        }

        ArrayAccessExpression arrayAccessExpression = PsiTreeUtil.getParentOfType(psiElement, ArrayAccessExpression.class);
        if (arrayAccessExpression == null) {
            return null;
        }

        return arrayAccessExpression.getType();
    }

    @Override
    public boolean accepts(@NotNull PsiElement psiElement, ProcessingContext context)
    {
        PhpType type = getArrayVariableType(psiElement);
        return type != null && type.toString().contains("#Z");
    }
}
