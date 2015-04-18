package Completions.Content;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.ArrayAccessExpression;
import com.jetbrains.php.lang.psi.elements.ArrayIndex;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import org.jetbrains.annotations.Nullable;

public class ArrayFieldTypeProvider extends FieldTypeProvider
{
    @Nullable
    @Override
    public String getType(PsiElement psiElement)
    {
        String fieldName;
        try {
            ArrayAccessExpression arrayAccessExpression = (ArrayAccessExpression)psiElement;
            ArrayIndex index = arrayAccessExpression.getIndex();
            if (index == null) {
                return null;
            }
            StringLiteralExpression stringLiteralExpression = (StringLiteralExpression)index.getValue();
            if (stringLiteralExpression == null) {
                return null;
            }
            fieldName = stringLiteralExpression.getContents();
        } catch (Exception e) {
            return null;
        }

        PsiElement[] children = psiElement.getChildren();
        if (children.length != 2) {
            return null;
        }

        String accessType = getClassname(children[0]);
        if (accessType == null) {
            return null;
        }

        accessType = accessType.replace("#P", "").replace("#M", "");
        String[] parts = accessType.split("\\.");
        if (parts.length != 2) {
            return null;
        }
        String className = parts[0];
        String accessor = parts[1];
        if (!accessor.equals("fields") && !accessor.equals("getFields")) {
            return null;
        }
        //validate fqn
        return formatResponse(className, fieldName);
    }
}
