package Completions.Content;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider2;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class FieldTypeProvider implements PhpTypeProvider2
{
    @Override
    public char getKey()
    {
        return 'Z';
    }

    @Override
    public Collection<? extends PhpNamedElement> getBySignature(String s, Project project)
    {
        return PhpIndex.getInstance(project).getAnyByFQN("\\eZCompletion\\" + s);
    }

    @Nullable
    @Override
    public String getType(PsiElement psiElement)
    {
        return parsePhpDoc(getTargetElement(psiElement));
    }

    protected PsiElement getTargetElement(PsiElement psiElement)
    {
        PsiElement parent = psiElement.getParent();
        if (parent == null) {
            return null;
        }

        PsiElement sibling = parent.getPrevSibling();
        if (sibling == null) {
            return null;
        }

        return sibling.getPrevSibling();
    }

    protected String parsePhpDoc(PsiElement psiElement)
    {
        PhpDocTag tagValue = PsiTreeUtil.getChildOfType(psiElement, PhpDocTag.class);
        if (tagValue == null) {
            return null;
        }

        if (!tagValue.getName().equals("@ContentType")) {
            return null;
        }

        String value = tagValue.getTagValue();
        if (value.length() == 0) {
            return null;
        }

        String[] parts = value.split(" ");
        String firstPart = parts[0];
        String secondPart = parts.length > 1 ? parts[1] : null;

        return firstPart.contains("$") ? secondPart : firstPart;
    }
}
