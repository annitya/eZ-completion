package TypeProviders.Content.Fields;

import Framework.CompletionContainer;
import Framework.CompletionPreloader;
import TypeProviders.Abstract.DumbAwareTypeProvider;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class FieldTypeProvider extends DumbAwareTypeProvider
{
    @Nullable
    @Override
    public String resolveType(PsiElement psiElement)
    {
        ArrayAccessExpression arrayAccess;
        ArrayIndex arrayIndex;
        StringLiteralExpression stringLiteral;
        FieldReference field;

        try {
            field = (FieldReference)psiElement.getFirstChild();
            if (field == null) {
                return null;
            }

            arrayAccess = (ArrayAccessExpression)psiElement;
            arrayIndex = arrayAccess.getIndex();
            if (arrayIndex == null) {
                return null;
            }
            stringLiteral = (StringLiteralExpression)arrayIndex.getValue();
        }
        catch (Exception e) {
            return null;
        }

        PhpType fieldType = field.getType();
        if (fieldType.getTypes().size() != 1) {
            return null;
        }
        String classTypeIdentifier = fieldType.getTypes().toArray()[0].toString().replace("#P", "").replace("#Z", "");
        String className = classTypeIdentifier.split("\\.")[0];

        if (stringLiteral == null) {
            return null;
        }
        String fieldIdentifier = stringLiteral.getContents();

        return className + "#" + getKey() + fieldIdentifier;
    }

    @Override
    public Collection<? extends PhpNamedElement> getBySignature(String s, Project project)
    {
        CompletionPreloader preloader = CompletionPreloader.getInstance(project);
        CompletionContainer completions = preloader.getCurrentCompletions();

        HashMap<String, HashMap<String, String>> contentTypeFields = completions.getContentTypeFields();
        String[] parts = s.split("#" + getKey());
        if (parts.length != 2) {
            return null;
        }
        String contentClass = parts[0];
        String fieldType = parts[1];

        HashMap<String, String> fieldTypeList = contentTypeFields.get(contentClass);
        if (fieldTypeList == null) {
            return null;
        }
        String className = fieldTypeList.get(fieldType);
        if (className == null) {
            return null;
        }

        return PhpIndex.getInstance(project).getAnyByFQN(className);
    }
}
