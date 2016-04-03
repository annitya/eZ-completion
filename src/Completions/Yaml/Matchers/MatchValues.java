package Completions.Yaml.Matchers;

import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLTokenTypes;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLSequence;
import org.jetbrains.yaml.psi.YAMLSequenceItem;

public class MatchValues extends PatternCondition<PsiElement>
{
    public MatchValues()
    {
        super("Values for predefined matchers.");
    }

    @Override
    public boolean accepts(@NotNull PsiElement psiElement, ProcessingContext context)
    {
        LeafPsiElement leaf;
        try {
            leaf = (LeafPsiElement)psiElement;
        }
        catch (Exception e) {
            return false;
        }

        IElementType elementType = leaf.getElementType();
        // WithinQuotes ftw!
        if (elementType != YAMLTokenTypes.SCALAR_STRING && elementType != YAMLTokenTypes.SCALAR_DSTRING) {
            return false;
        }

        Boolean isSingleValue, isMultiValue;

        PsiElement previous = leaf.getParent().getPrevSibling();
        isSingleValue = previous instanceof PsiWhiteSpace;
        isMultiValue = leaf.getParent().getParent() instanceof YAMLSequenceItem;

        if (!isSingleValue && !isMultiValue) {
            return false;
        }

        PsiElement parent = PsiTreeUtil.getParentOfType(leaf, YAMLKeyValue.class);
        if (parent == null) {
            return false;
        }
        PsiElement matcherFqn = parent.getFirstChild();
        if (matcherFqn == null) {
            return false;
        }

        String[] parts = matcherFqn.getText().replace(":", "").split("\\\\");
        if (parts.length != 2) {
            return false;
        }

        context.put("value", parts[0]);
        context.put("type", parts[1]);

        return true;
    }
}
