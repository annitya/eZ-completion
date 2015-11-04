package TypeProviders;

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
        PhpDocTag tagValue;
        // Check all statements within current scope for doc-blocks.
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

        return parsePhpDoc(tagValue);
    }

    protected String parsePhpDoc(PsiElement psiElement)
    {
        if (psiElement == null) {
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

        return firstPart.contains("$") ? secondPart : firstPart;
    }
}
