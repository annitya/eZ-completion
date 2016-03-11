package Annotation;

import Completions.Php.Content.Field;
import Framework.CompletionContainer;
import Framework.CompletionPreloader;
import TypeProviders.Abstract.TypeKeys;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.ArrayAccessExpression;
import com.jetbrains.php.lang.psi.elements.ArrayIndex;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Set;

public class FieldAnnotator implements Annotator
{
    @Override
    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder)
    {
        ArrayAccessExpression arrayAccess;
        try {
            arrayAccess = (ArrayAccessExpression)psiElement;
        }
        catch (Exception e) {
            return;
        }
        ArrayIndex arrayIndex = arrayAccess.getIndex();
        if (arrayIndex == null) {
            return;
        }

        Set<String> types = arrayAccess.getType().getTypes();
        if (types.size() != 1) {
            return;
        }
        String[] typeParts = types.toArray()[0].toString().split("#" + TypeKeys.FIELD_KEY);
        if (typeParts.length != 3) {
            return;
        }

        String className = typeParts[1];
        String fieldIdentifier = typeParts[2];

        CompletionContainer completions = CompletionPreloader.getInstance(psiElement.getProject()).getCurrentCompletions();

        HashMap<String, HashMap<String, Field>> contentTypeFields = completions.getContentTypeFields();
        if (!contentTypeFields.containsKey(className)) {
            return;
        }

        HashMap<String, Field> fieldIdentifiers = contentTypeFields.get(className);
        if (!fieldIdentifiers.containsKey(fieldIdentifier)) {
            return;
        }

        Field field = fieldIdentifiers.get(fieldIdentifier);
        if (!field.hasDescription()) {
            return;
        }

        String annotation = "";
        if (field.hasName()) {
            annotation = field.getName() + ": ";
        }
        annotation += field.getDescription();

        annotationHolder.createInfoAnnotation(arrayIndex, annotation);
    }
}
