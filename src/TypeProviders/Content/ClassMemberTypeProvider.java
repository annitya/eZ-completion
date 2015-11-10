package TypeProviders.Content;

import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.elements.impl.FieldImpl;
import org.jetbrains.annotations.Nullable;

/**
 * Provides content-type for class-members. Eg. protected $content;
 */
public class ClassMemberTypeProvider extends ContentVariableTypeProvider
{
    @Nullable
    @Override
    public String resolveType(PsiElement psiElement)
    {
        PhpDocComment comment;
        try {
            FieldImpl field = (FieldImpl)psiElement;
            comment = field.getDocComment();
        }
        catch (Exception e) {
            return null;
        }

        if (comment == null) {
            return null;
        }

        PhpDocTag[] phpDocTags = comment.getTagElementsByName("@ContentType");
        if (phpDocTags.length != 1) {
            return null;
        }
        PhpDocTag tag = phpDocTags[0];
        String[] tagParts = tag.getTagValue().split(" ");
        if (tagParts.length != 1) {
            return null;
        }

        return typeSeparator() + parsePhpDoc(tag);
    }
}
