package Framework;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.LeafPsiElement;

import com.jetbrains.php.lang.psi.elements.ClassReference;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.impl.NewExpressionImpl;

public class Util
{
    public static final String INTELLIJ_RULES = "IntellijIdeaRulezzz";

    public static Boolean withinQuotes(PsiElement psiElement)
    {
        // Require quotes to trigger completion.
        try {
            return !(((LeafPsiElement) psiElement).getElementType().toString().equals("identifier"));
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean newFqnMatches(PsiElement method, String fqn)
    {
        try {
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

    public static Boolean fqnMatches(PsiElement method, String fqn)
    {
        PhpClass phpClass;
        try {
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
        if (classFqn.equals(fqn)) {
            return true;
        }

        PhpClass[] implementedInterfaces = phpClass.getImplementedInterfaces();
        if (implementedInterfaces.length == 0) {
            return false;
        }

        for (PhpClass implementedInterface : implementedInterfaces) {
            String interfaceFqn = implementedInterface.getFQN();

            if (interfaceFqn.equals(fqn)) {
                return true;
            }
        }

        return false;
    }

    public static Boolean methodMatches(PsiElement psiElement, String method)
    {
        if (method == null) {
            return true;
        }

        try {
            return psiElement
                    .getFirstChild()
                    .getNextSibling()
                    .getNextSibling()
                    .getText()
                    .equals(method);
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean dependenceMatches(PsiElement psiElement, String dependence)
    {
        if (dependence == null) {
            return true;
        }

        PsiElement[] parameters = getParameters(psiElement);

        return parameters.length > 0 && parameters[0].getText().replaceAll("^\"|\"$|\'|\'$", "").equals(dependence);
    }

    public static Boolean parameterMatches(PsiElement psiElement, Integer parameterIndex)
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

    public static PsiElement[] getParameters(PsiElement psiElement)
    {
        try {
            return ((ParameterList)psiElement.getParent().getParent()).getParameters();
        } catch (Exception e) {
            return new PsiElement[0];
        }
    }

    public static Project currentProject()
    {
        DataContext context = DataManager.getInstance().getDataContextFromFocus().getResultSync();
        return CommonDataKeys.PROJECT.getData(context);
    }

    public static PsiElement getMethodFromVariable(PsiElement psiElement)
    {
        try {
            return psiElement.getParent().getParent().getParent();
        } catch (Exception e) {
            return null;
        }
    }
}
