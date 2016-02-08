package TypeProviders.Content.Fields;

import TypeProviders.Abstract.TypeKeys;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import com.jetbrains.php.lang.psi.elements.Variable;

/**
 * Provides completions for:
 *
 * $content->getFieldValue('|')
 * $content->getFieldValue('some_identifier')->|
 */
public class GetFieldValueTypeProvider extends FieldTypeProvider
{
    @Override
    public String resolveType(PsiElement psiElement)
    {
        MethodReference methodReference;
        try {
            methodReference = (MethodReference)psiElement;
        } catch (Exception e) {
            return null;
        }
        String methodName = methodReference.getName();
        // Slightly weak match.
        if (methodName == null || !methodName.equals("getFieldValue")) {
            return null;
        }

        PsiElement[] parameters = methodReference.getParameters();
        if (parameters.length < 1) {
            return null;
        }

        String className;
        try {
            Variable variable = (Variable)methodReference.getFirstChild();
            className = TypeKeys.getTypeString(variable, TypeKeys.CONTENT_KEY);
        }
        catch (Exception e) {
            return null;
        }

        String fieldName;
        try {
            fieldName = ((StringLiteralExpression)parameters[0]).getContents();
        } catch (Exception e) {
            return null;
        }

        return className + typeSeparator() + fieldName;
    }
}
