package Completions.Content;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider2;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;

public class ContentTypeProvider implements PhpTypeProvider2
{
    @Override
    public char getKey()
    {
        return 'Z';
    }

    @Nullable
    @Override
    public String getType(PsiElement psiElement)
    {
        PsiElement parent = psiElement.getParent();
        if (parent == null) {
            return null;
        }
        parent = parent.getParent();
        if (parent == null) {
            return null;
        }

        PsiElement sibling = parent.getPrevSibling();
        if (sibling == null) {
            return null;
        }
        sibling = sibling.getPrevSibling();
        if (sibling == null) {
            return null;
        }
        if (!(sibling instanceof PhpDocComment)) {
            return null;
        }

        String comment = sibling.getText();
        if (!comment.contains("@ContentType")) {
            return null;
        }

        String[] parts = comment.split(" ");
        if (parts.length < 2) {
            return null;
        }

        return null;
        //return parts[2];
    }

    @Override
    public Collection<? extends PhpNamedElement> getBySignature(String s, Project project)
    {
        if (!s.equals("user_idea")) {
            return null;
        }

        return null;
        //return PhpIndex.getInstance(project).getAnyByFQN("\\ContentTypes\\user_idea_test");
    }
}
