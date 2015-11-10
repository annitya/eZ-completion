package TypeProviders.Content.Fields;

import Completions.Content.Field;
import Framework.CompletionContainer;
import Framework.CompletionPreloader;
import TypeProviders.Abstract.DumbAwareTypeProvider;
import TypeProviders.Abstract.TypeKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.*;
import java.util.Collection;
import java.util.HashMap;

public class FieldTypeProvider extends DumbAwareTypeProvider
{
    protected String accessorName;

    public FieldTypeProvider()
    {
        accessorName = "fields";
    }

    @Override
    public char getKey()
    {
        return TypeKeys.FIELD_KEY;
    }

    protected boolean validReferenceName(PsiElement firstChild, String name)
    {
        if (firstChild == null) {
            return false;
        }

        PhpReference reference;
        String referenceName;
        try {
            reference = (PhpReference)firstChild;
            referenceName = reference.getName();
        } catch (Exception e) {
            return false;
        }

        return name != null && name.equals(referenceName);

    }

    protected String getArrayAccessIndexValue(PsiElement psiElement)
    {
        ArrayAccessExpression arrayAccess;
        ArrayIndex arrayIndex;
        StringLiteralExpression stringLiteral;
        try {
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

        if (stringLiteral == null || stringLiteral.getContents().length() == 0) {
            return null;
        }

        return stringLiteral.getContents();
    }

    public String resolveType(PsiElement psiElement)
    {
        PsiElement firstChild = psiElement.getFirstChild();
        if (!validReferenceName(firstChild, accessorName)) {
            return null;
        }

        String className = TypeKeys.getTypeString(firstChild, TypeKeys.ARRAY_FIELD_KEY);
        if (className == null) {
            className = TypeKeys.getTypeString(firstChild, TypeKeys.CONTENT_KEY);
        }

        if (className == null) {
            return null;
        }

        String accessor = getArrayAccessIndexValue(psiElement);
        if (accessor == null) {
            return null;
        }

        return className + typeSeparator() + accessor;
    }

    @Override
    public Collection<? extends PhpNamedElement> getBySignature(String s, Project project)
    {
        CompletionPreloader preloader = CompletionPreloader.getInstance(project);
        CompletionContainer completions = preloader.getCurrentCompletions();

        HashMap<String, HashMap<String, Field>> contentTypeFields = completions.getContentTypeFields();
        String[] parts = s.split("#" + getKey());
        if (parts.length != 2) {
            return null;
        }
        String contentClass = parts[0];
        String fieldType = parts[1];

        HashMap<String, Field> fieldTypeList = contentTypeFields.get(contentClass);
        if (fieldTypeList == null) {
            return null;
        }
        if (!fieldTypeList.containsKey(fieldType)) {
            return null;
        }
        Field field = fieldTypeList.get(fieldType);
        if (!field.hasFqn()) {
            return null;
        }

        return PhpIndex.getInstance(project).getAnyByFQN(field.getFqn());
    }
}
