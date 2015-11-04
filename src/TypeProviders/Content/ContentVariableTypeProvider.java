package TypeProviders.Content;

import Framework.CompletionPreloader;
import TypeProviders.Abstract.DumbAwareTypeProvider;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.elements.Statement;
import com.jetbrains.php.lang.psi.elements.impl.FieldImpl;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * Parses PhpDoc and returns the specified content-type-identifier eg. "article_list"
 * "@ContentType article_list $content"
 */
public class ContentVariableTypeProvider extends DumbAwareTypeProvider
{
    @Override
    public Collection<? extends PhpNamedElement> getBySignature(String s, Project project)
    {
        // Break if user specifies a wrong contentclass.
        if (!CompletionPreloader.getInstance(project).getCurrentCompletions().contentClassExists(s)) {
            return Collections.emptySet();
        }

        // Passthrough
        return PhpIndex.getInstance(project).getClassesByFQN("\\eZ\\Publish\\API\\Repository\\Values\\Content\\Content");
    }

    @Nullable
    @Override
    public String resolveType(PsiElement psiElement)
    {
        String variableName = psiElement.getText();
        PsiElement statement = PsiTreeUtil.getParentOfType(psiElement, Statement.class);
        PsiElement sibling = PsiTreeUtil.getPrevSiblingOfType(statement, PhpDocComment.class);
        PhpDocTag tag;

        // Check all statements within current scope for doc-blocks.
        do {
            tag = PsiTreeUtil.getChildOfType(sibling, PhpDocTag.class);
            if (tagMatches(tag, variableName)) {
                break;
            }
            sibling = PsiTreeUtil.getPrevSiblingOfType(sibling, PhpDocComment.class);
            tag = null;
        } while (sibling != null);

        if (tag == null) {
            return null;
        }

        return parsePhpDoc(tag);
    }

    protected boolean tagMatches(PhpDocTag tag, String variableName)
    {
        if (tag == null) {
            return false;
        }

        variableName = variableName.replace("$", "");
        String[] parts = tag.getTagValue().split(" ");
        for (String part : parts) {
            part = part.replace("$", "");
            if (part.equals(variableName)) {
                return true;
            }
        }

        return false;
    }

    protected String parsePhpDoc(PhpDocTag tag)
    {
        if (!tag.getName().equals("@ContentType")) {
            return null;
        }

        String value = tag.getTagValue();
        if (value.length() == 0) {
            return null;
        }

        String[] parts = value.split(" ");
        if (parts.length == 1) {
            return parts[0];
        }

        String firstPart = parts[0];
        String secondPart = parts.length > 1 ? parts[1] : null;

        return firstPart.contains("$") ? secondPart : firstPart;
    }
}
