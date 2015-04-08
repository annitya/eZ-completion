package Completions.Content;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.Statement;
import org.jetbrains.annotations.Nullable;

public class VariableTypeProvider extends TypeProvider
{
    @Nullable
    @Override
    public String getType(PsiElement psiElement)
    {
        String variableName = psiElement.getText();
        PsiElement statement = PsiTreeUtil.getParentOfType(psiElement, Statement.class);
        PsiElement sibling = PsiTreeUtil.getPrevSiblingOfType(statement, PhpDocComment.class);
        PhpDocTag tagValue;
        do {
            tagValue = PsiTreeUtil.getChildOfType(sibling, PhpDocTag.class);
            if (tagValue != null && tagValue.getTagValue().contains(variableName)) {
                break;
            }
            sibling = PsiTreeUtil.getPrevSiblingOfType(sibling, PhpDocComment.class);
            tagValue = null;
        } while (sibling != null);

        if (tagValue == null) {
            return null;
        }

        // No support for doc-blocks above methods.
        if (sibling.getParent() instanceof PhpClass) {
            return null;
        }

        return parsePhpDoc(tagValue);
    }
}
