package TypeProviders.Content.Fields;

import TypeProviders.Abstract.DumbAwareTypeProvider;
import TypeProviders.Abstract.TypeKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.*;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;

/**
 * A Content-object in eZ will always have an array of fields which has various accessors.
 * This class tags them for later use when requesting field-identifier completions.
 *
 * $content->getFields()['|']
 *
 * The resolved signature is a contentclass-identifier
 */
public class ArrayOfFieldsTypeProvider extends DumbAwareTypeProvider
{
    @Override
    public char getKey()
    {
        return TypeKeys.ARRAY_FIELD_KEY;
    }

    @Nullable
    @Override
    public String resolveType(PsiElement psiElement)
    {
        MethodReference method;
        Variable variable;
        try {
            method = (MethodReference) psiElement;
            variable = (Variable)method.getFirstChild();
        }
        catch (Exception e) {
            return null;
        }

        String methodName = method.getName();
        if (methodName == null) {
            return null;
        }
        if (/**!methodName.equals("getFields") &&*/ !methodName.equals("getFieldsByLanguage")) {
            return null;
        }

        if (!TypeKeys.isContent(variable)) {
            return null;
        }

        String className = TypeKeys.getTypeString(variable, TypeKeys.CONTENT_KEY);
        if (className == null) {
            return null;
        }

        return typeSeparator() + className;
    }

    @Override
    public Collection<? extends PhpNamedElement> getBySignature(String s, Project project)
    {
        return null;
    }
}
