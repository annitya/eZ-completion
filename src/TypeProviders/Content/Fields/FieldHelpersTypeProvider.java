package TypeProviders.Content.Fields;

import TypeProviders.Abstract.TypeKeys;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import org.jetbrains.annotations.Nullable;

/**
 * Provides type for field-helpers:
 *
 * $translationHelper->getTranslatedField($content, 'identifier')->|
 */
public class FieldHelpersTypeProvider extends FieldTypeProvider
{
    @Nullable
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
        if (methodName == null) {
            return null;
        }
        // Slightly weak match.
        if (!methodName.equals("getTranslatedField")) {
            return null;
        }

        PsiElement[] parameters = methodReference.getParameters();
        if (parameters.length < 2) {
            return null;
        }

        String className = TypeKeys.getTypeString(parameters[0], TypeKeys.CONTENT_KEY);

        String fieldName;
        try {
            fieldName = ((StringLiteralExpression)parameters[1]).getContents();
        } catch (Exception e) {
            return null;
        }

        return className + typeSeparator() + fieldName;
    }
}
