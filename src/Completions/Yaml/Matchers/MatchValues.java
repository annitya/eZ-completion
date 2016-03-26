package Completions.Yaml.Matchers;

import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLTokenTypes;

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

        PsiElement previous = leaf.getParent().getPrevSibling();
        if (!(previous instanceof PsiWhiteSpace)) {
            return false;
        }

        String[] parts = previous.getPrevSibling().getText().replace(":", "").split("\\\\");
        if (parts.length != 2) {
            return false;
        }

        context.put("value", parts[0]);
        context.put("type", parts[1]);

        return true;
    }
}
