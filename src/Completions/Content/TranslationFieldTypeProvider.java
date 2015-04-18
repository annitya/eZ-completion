package Completions.Content;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import org.jetbrains.annotations.Nullable;

public class TranslationFieldTypeProvider extends FieldTypeProvider
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

        PsiElement[] parameters = methodReference.getParameters();
        if (parameters.length < 2) {
            return null;
        }

        String className = getClassname(parameters[0]);
        if (className == null) {
            return null;
        }

        String fieldName;
        try {
        fieldName = ((StringLiteralExpression)parameters[1]).getContents();
        } catch (Exception e) {
            return null;
        }

        return formatResponse(className, fieldName);
    }
}
