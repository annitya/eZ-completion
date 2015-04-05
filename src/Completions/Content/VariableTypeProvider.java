package Completions.Content;

import com.intellij.psi.PsiElement;
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

        return parsePhpDoc(super.getTargetElement(parent));
    }
}
