package TypeProviders;

import Framework.CompletionContainer;
import Framework.CompletionPreloader;
import TypeProviders.Abstract.DumbAwareTypeProvider;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.*;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.HashMap;

public class FieldTypeProvider extends DumbAwareTypeProvider
{
    @Nullable
    @Override
    public String resolveType(PsiElement psiElement)
    {
        MethodReference methodReference;
        try {
            methodReference = (MethodReference)psiElement;
            Method psiMethod = (Method)methodReference.resolve();
            if (psiMethod == null) {
                return null;
            }

            if (!"\\eZ\\Publish\\API\\Repository\\Values\\Content\\Content.getFieldValue".equals(psiMethod.getFQN())) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        PsiElement[] children = psiElement.getChildren();
        if (children.length == 0) {
            return null;
        }

        PsiElement[] parameters;
        try {
            parameters = ((ParameterList) psiElement.getChildren()[1]).getParameters();
        } catch (Exception e) {
            return null;
        }

        StringLiteralExpression stringParameter;
        try {
            stringParameter = (StringLiteralExpression)parameters[0];
        } catch (Exception e) {
            return null;
        }
        String fieldName = stringParameter.getContents();

        String className = getClassname(children[0]);
        if (className == null || className.length() == 0 || fieldName.length() == 0) {
            return null;
        }

        return formatResponse(className, fieldName);
    }

    protected String formatResponse(String className, String fieldName)
    {
        return className + "#" + getKey() + fieldName;
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
