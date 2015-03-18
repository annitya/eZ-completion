package Completions.Annotation;

import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocElementType;
import org.jetbrains.annotations.NotNull;

public class AnnotationMatcher extends PatternCondition<PsiElement>
{
    protected boolean tagCompletion;

    public AnnotationMatcher(String debugMethodName, boolean tagCompletion)
    {
        super(debugMethodName);
        this.tagCompletion = tagCompletion;
    }

    @Override
    public boolean accepts(@NotNull PsiElement psiElement, ProcessingContext context)
    {
        PsiElement parent = psiElement.getParent();
        if (parent == null) {
            return false;
        }

        PsiElement sibling = parent.getPrevSibling();
        if (sibling == null) {
            return false;
        }
        sibling = sibling.getPrevSibling();
        if (sibling == null) {
            return false;
        }

        //noinspection SimplifiableIfStatement
        if (!(sibling.getNode().getElementType() instanceof PhpDocElementType)) {
            return false;
        }

        boolean hasTag = sibling.getText().equals("@ContentType");
        return tagCompletion ? !hasTag : hasTag;
    }
}
