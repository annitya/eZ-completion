package Intentions;

import TypeProviders.Abstract.TypeKeys;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment;
import com.jetbrains.php.lang.psi.PhpPsiElementFactory;
import com.jetbrains.php.lang.psi.elements.*;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class EzDocVariableIntention implements IntentionAction
{

    public static final String PHP_CONTENT_CLASS = "\\eZ\\Publish\\API\\Repository\\Values\\Content\\Content";

    @Nls
    @NotNull
    @Override
    public String getText()
    {
        return "Add eZDoc comment";
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName()
    {
        return getText();
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile psiFile)
    {
        int primaryCaretOffset = editor.getCaretModel().getPrimaryCaret().getOffset();

        PsiReference reference = psiFile.findReferenceAt(primaryCaretOffset);
        if (reference == null) {
            return false;
        }

        // We do not add doc-blocks for methods or fields.
        if (reference instanceof MethodReference || reference instanceof FieldReference) {
            return false;
        }

        String typeName;
        try {
            PhpType type = ((PhpTypedElement)reference).getType();
            typeName = type.getTypes().toArray()[0].toString();
        } catch (Exception e) {
            return false;
        }

        if (typeName == null) {
            return false;
        }

        // Already has a doc-block.
        if (typeName.indexOf(TypeKeys.CONTENT_KEY) != -1) {
            return false;
        }

        // Type can be inferred directly from variable.
        if (typeName.equals(PHP_CONTENT_CLASS)) {
            return true;
        }

        // Not a signature.
        if (!typeName.contains("#")) {
            return false;
        }

        PhpIndex index = PhpIndex.getInstance(project);
        Collection<? extends PhpNamedElement> elements = index.getBySignature(typeName);
        if (elements.size() == 0) {
            return false;
        }

        // Try to resolve type from method.
        Method method;
        try {
            method = (Method)elements.toArray()[0];
            return method.getType().toString().equals(PHP_CONTENT_CLASS);
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile psiFile) throws IncorrectOperationException
    {
        int primaryCaretOffset = editor.getCaretModel().getPrimaryCaret().getOffset();

        PsiElement currentPsiElement = psiFile.findElementAt(primaryCaretOffset);
        PsiElement statement = PsiTreeUtil.getParentOfType(currentPsiElement, Statement.class);
        if (statement == null) {
            return;
        }

        String commentStart = "/** @ContentType IDENTIFIER ";
        String commentContent = commentStart + currentPsiElement.getText() + "*/";
        PhpDocComment comment = PhpPsiElementFactory.createFromText(project, PhpDocComment.class, commentContent);
        if (comment == null) {
            return;
        }

        PsiElement result = statement.addBefore(comment, statement.getFirstChild());
        editor.getCaretModel().getPrimaryCaret().moveToOffset(result.getTextOffset() + commentStart.length() - 1);

        editor.getCaretModel().getPrimaryCaret().selectWordAtCaret(true);
    }

    @Override
    public boolean startInWriteAction()
    {
        return true;
    }
}
