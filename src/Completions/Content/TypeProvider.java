package Completions.Content;

import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class TypeProvider extends DumbAwareTypeProvider
{
    @Override
    public char getKey()
    {
        return 'Z';
    }

    @Override
    public Collection<? extends PhpNamedElement> getBySignature(String s, Project project)
    {
        return lookupClassname(s, project);
    }

    protected Collection<PhpClass> lookupClassname(String s, Project project)
    {
        return PhpIndex.getInstance(project).getAnyByFQN("\\eZCompletion\\" + s);
    }

    @Nullable
    @Override
    public String resolveType(PsiElement psiElement)
    {
        PsiElement parent = psiElement.getParent();
        if (parent == null) {
            return null;
        }

        PsiElement sibling = parent.getPrevSibling();
        if (sibling == null) {
            return null;
        }

        PhpDocTag phpDocTag = PsiTreeUtil.getChildOfType(sibling.getPrevSibling(), PhpDocTag.class);
        return parsePhpDoc(phpDocTag);
    }

    protected String parsePhpDoc(PsiElement psiElement)
    {
        if (psiElement == null) {
            return null;
        }

        // Lets wait until the index is ready.
        if (DumbService.isDumb(psiElement.getProject())) {
            return null;
        }

        PhpDocTag tagValue;
        try {
            tagValue = (PhpDocTag)psiElement;
        } catch (Exception e) {
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

        String className = firstPart.contains("$") ? secondPart : firstPart;
        Collection<PhpClass> result = lookupClassname(className, tagValue.getProject());

        if (result.size() > 0) {
            return className;
        }
        else {
            return null;
        }
    }
}
