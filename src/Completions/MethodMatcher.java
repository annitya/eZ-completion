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
    protected String dependence;

    public MethodMatcher(int parameterIndex, String method)
    {
        super(method);
        this.parameterIndex = parameterIndex;
        this.method = method;
    }

    @Override
    public boolean accepts(@NotNull PsiElement psiElement, ProcessingContext context)
    {
        return withinQuotes(psiElement) && dependenceMatches(psiElement) && methodMatches(psiElement) && parameterMatches(psiElement);
    }

    protected Boolean withinQuotes(PsiElement psiElement)
    {
        // Require quotes to trigger completion.
        try {
            return !(((LeafPsiElement) psiElement).getElementType().toString().equals("identifier"));
        } catch (Exception e) {
            return false;
        }
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

    protected Boolean dependenceMatches(PsiElement psiElement)
    {
        if (dependence == null) {
            return true;
        }

        PsiElement[] parameters = getParameters(psiElement);

        return parameters.length > 0 && parameters[0].getText().replaceAll("^\"|\"$|\'|\'$", "").equals(dependence);
    }

    protected Boolean parameterMatches(PsiElement psiElement)
    {
        try {
            PsiElement[] parameters = getParameters(psiElement);
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

    protected PsiElement[] getParameters(PsiElement psiElement)
    {
        try {
            return ((ParameterList)psiElement.getParent().getParent()).getParameters();
        } catch (Exception e) {
            return new PsiElement[0];
        }
    }
}
