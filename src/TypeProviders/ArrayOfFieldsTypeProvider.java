package TypeProviders;

import Framework.CompletionPreloader;
import TypeProviders.Abstract.DumbAwareTypeProvider;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * A Content-object in eZ will always have an array of fields which has various accessors.
 * This class tags them for later use when requesting field-identifier completions.
 *
 * $content->fields['|'], $content->getFields()['|']
 */
public class ArrayOfFieldsTypeProvider extends DumbAwareTypeProvider
{
    public final static String FIELD_LIST_IDENTIFIER = "contentclass_field_list";

    @Nullable
    @Override
    public String resolveType(PsiElement psiElement)
    {
        PsiElement[] children = psiElement.getChildren();
        if (children.length != 2) {
            return null;
        }

        PhpTypedElement typedElement;
        try {
            typedElement = (PhpTypedElement)children[0];
        } catch (Exception e) {
            return null;
        }
        Object[] types = typedElement.getType().getTypes().toArray();
        if (types.length == 0) {
            return null;
        }

        String accessType = types[0].toString().replace("#Z", "");
        if (accessType == null) {
            return null;
        }

        accessType = accessType.replace("#P", "").replace("#M", "");
        String[] parts = accessType.split("\\.");
        if (parts.length != 2) {
            return null;
        }
        String className = parts[0];
        if (!CompletionPreloader.getInstance(psiElement.getProject()).getCurrentCompletions().contentClassExists(className)) {
            return null;
        }

        String accessor = parts[1];
        if (!accessor.equals("fields") && !accessor.equals("getFields")) {
            return null;
        }

        return className + "#" + getKey() + FIELD_LIST_IDENTIFIER;
    }

    @Override
    public Collection<? extends PhpNamedElement> getBySignature(String s, Project project)
    {
        return null;
    }
}
