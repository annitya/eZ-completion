package Completions;

import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import org.jetbrains.annotations.NotNull;

public class MethodMatcher extends PatternCondition<PsiElement>
{
    protected int parameterIndex;
    protected String method;

    public MethodMatcher(int parameterIndex, String method)
    {
        super(method);
        this.parameterIndex = parameterIndex;
        this.method = method;
    }

    @Override
    public boolean accepts(@NotNull PsiElement psiElement, ProcessingContext context)
    {
        // Require quotes to trigger completion.
        try {
            if (((LeafPsiElement) psiElement).getElementType().toString().equals("identifier")) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return methodMatches(psiElement) && parameterMatches(psiElement);
    }

    protected Boolean methodMatches(PsiElement psiElement)
    {
        try {
            return psiElement
                    .getParent()
                    .getParent()
                    .getParent()
                    .getFirstChild()
                    .getNextSibling()
                    .getNextSibling()
                    .getText()
                    .equals(method);
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean parameterMatches(PsiElement psiElement)
    {
        try {
            PsiElement[] parameters = ((ParameterList)psiElement.getParent().getParent()).getParameters();
            for (int i=0; i < parameters.length; i++) {
                if (parameters[i].equals(psiElement.getParent())) {
                    return i == parameterIndex;
                }
            }

            return false;

        } catch (Exception e) {
            return false;
        }
    }
}
