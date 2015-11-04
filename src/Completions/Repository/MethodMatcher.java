package Completions.Repository;

import Framework.Util;
import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class MethodMatcher extends PatternCondition<PsiElement>
{
    protected int parameterIndex;
    protected String fqn;
    protected String method;
    protected String dependence;

    public MethodMatcher(int parameterIndex, String method)
    {
        super(method);
        this.parameterIndex = parameterIndex;
        this.method = method;
    }

    public MethodMatcher(String fqn, String method, int parameterIndex)
    {
        this(parameterIndex, method);
        this.fqn = fqn;
    }

    @Override
    public boolean accepts(@NotNull PsiElement psiElement, ProcessingContext context)
    {
        PsiElement methodReference = Util.getMethodFromVariable(psiElement);

        return
                methodReference != null &&
                Util.withinQuotes(psiElement) &&
                (Util.fqnMatches(methodReference, fqn) || Util.newFqnMatches(methodReference, fqn)) &&
                Util.methodMatches(methodReference, method) &&
                Util.dependenceMatches(psiElement, dependence) &&
                Util.parameterMatches(psiElement, parameterIndex);
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object obj)
    {
        try {
            MethodMatcher other = (MethodMatcher)obj;
            if (!other.fqn.equals(fqn)) {
                return false;
            }

            if (method != null && !other.method.equals(method)) {
                return false;
            }

            if (other.parameterIndex != parameterIndex) {
                return false;
            }

            if (dependence != null && !other.dependence.equals(dependence)) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
