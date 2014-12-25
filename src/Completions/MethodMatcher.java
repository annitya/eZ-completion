package Completions;

import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.impl.NewExpressionImpl;
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

    @Override
    public boolean accepts(@NotNull PsiElement psiElement, ProcessingContext context)
    {
        return
                withinQuotes(psiElement) &&
                (fqnMatches(psiElement) || newFqnMatches(psiElement)) &&
                methodMatches(psiElement) &&
                dependenceMatches(psiElement) &&
                parameterMatches(psiElement);
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

    protected Boolean newFqnMatches(PsiElement psiElement)
    {
        try {
            PsiElement method = psiElement.getParent().getParent().getParent();
            ClassReference classReference = ((NewExpressionImpl) method).getClassReference();
            if (classReference == null) {
                return false;
            }

            String classFqn = classReference.getFQN();
            return classFqn != null && classFqn.equals(fqn);

        }
        catch (Exception e) {
            return false;
        }
    }

    protected Boolean fqnMatches(PsiElement psiElement)
    {
        PhpClass phpClass;
        try {
            PsiElement method = psiElement.getParent().getParent().getParent();
            if (method == null) {
                return false;
            }

            PsiReference reference = method.getReference();
            if (reference == null) {
                return false;
            }

            PsiElement resolved = reference.resolve();
            if (resolved == null) {
                return false;
            }

            phpClass = (PhpClass)resolved.getParent();
            if (phpClass == null) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        String classFqn = phpClass.getFQN();
        if (classFqn != null && classFqn.equals(fqn)) {
            return true;
        }

        PhpClass[] implementedInterfaces = phpClass.getImplementedInterfaces();
        if (implementedInterfaces.length == 0) {
            return false;
        }

        for (PhpClass implementedInterface : implementedInterfaces) {
            String interfaceFqn = implementedInterface.getFQN();
            if (interfaceFqn == null) {
                continue;
            }

            if (interfaceFqn.equals(fqn)) {
                return true;
            }
        }

        return false;
    }

    protected Boolean methodMatches(PsiElement psiElement)
    {
        if (method == null) {
            return true;
        }

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
