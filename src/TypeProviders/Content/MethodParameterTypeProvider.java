package TypeProviders.Content;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.elements.Parameter;
import com.jetbrains.php.lang.psi.elements.impl.MethodImpl;
import org.jetbrains.annotations.Nullable;

/**
 * Lets the user specify content-type for method-parameters
 */
public class MethodParameterTypeProvider extends ContentVariableTypeProvider
{
    @Nullable
    @Override
    public String resolveType(PsiElement psiElement)
    {
        String variableName;
        try {
            Parameter parameter = (Parameter)psiElement;
            variableName = parameter.getName();
        }
        catch (Exception e) {
            return null;
        }
        MethodImpl method = PsiTreeUtil.getParentOfType(psiElement, MethodImpl.class);
        if (method == null) {
            return null;
        }

        PhpDocComment comment = method.getDocComment();
        if (comment == null) {
            return null;
        }

        PhpDocTag[] phpDocTags = comment.getTagElementsByName("@ContentType");
        if (phpDocTags.length == 0) {
            return null;
        }

        for (PhpDocTag tag : phpDocTags) {
            if (tagMatches(tag, variableName)) {
                return parsePhpDoc(tag);
            }
        }

        return null;
    }
}
