package Completions.EzDoc;

import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag;
import com.jetbrains.php.lang.psi.elements.Method;
import org.jetbrains.annotations.NotNull;

public class EzDocMatcher extends PatternCondition<PsiElement>
{
    protected boolean tagCompletion;

    public EzDocMatcher(String debugMethodName, boolean tagCompletion)
    {
        super(debugMethodName);
        this.tagCompletion = tagCompletion;
    }

    @Override
    public boolean accepts(@NotNull PsiElement psiElement, ProcessingContext context)
    {
        // Do not provide completions if plugin is disabled.
        if (Settings.Service.getInstance(psiElement.getProject()).getDisabled()) {
            return false;
        }

        PhpDocTag phpDocTag = PsiTreeUtil.getParentOfType(psiElement, PhpDocTag.class);
        if (phpDocTag == null) {
            return false;
        }

        // Disallow completion within method-phpdoc.
        try {
            if (phpDocTag.getParent().getNextSibling().getNextSibling() instanceof Method) {
                return false;
            }
        } catch (Exception ignored) {}

        String tagValue = phpDocTag.getTagValue();
        String[] parts = tagValue.split(" ");

        // Tag already has a contentclass.
        for (String part : parts) {
            if (part.length() > 0 & !part.contains("$") && !part.equals("IntellijIdeaRulezzz")) {
                return false;
            }
        }

        boolean hasTag = phpDocTag.getName().equals("@ContentType");
        return tagCompletion ? !hasTag : hasTag;
    }
}
