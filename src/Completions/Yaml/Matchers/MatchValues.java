package Completions.Yaml.Matchers;

import Framework.Util;
import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;
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

        Boolean isSingleValue, isMultiValue;

        isSingleValue = leaf
            .getText()
            .replace("\"", "")
            .replace("'", "")
            .trim()
            .equals(Util.INTELLIJ_RULES);

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
