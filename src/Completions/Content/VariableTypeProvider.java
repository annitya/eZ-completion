package Completions.Content;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.Nullable;

public class VariableTypeProvider extends FieldTypeProvider
{
    @Nullable
    @Override
    public String getType(PsiElement psiElement)
    {
        PsiElement parent = psiElement.getParent();
        if (parent == null) {
            return null;
        }

        PsiElement targetElement = super.getTargetElement(parent);
        // No support for doc-blocks above methods.
        if (targetElement == null || targetElement.getParent() instanceof PhpClass) {
            return null;
        }
        return parsePhpDoc(targetElement);
    }
}
