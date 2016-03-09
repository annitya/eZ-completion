package Completions.Yaml;

import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.parser.PhpElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLTokenTypes;
import org.jetbrains.yaml.psi.YAMLKeyValue;

public class SiblingMatcher extends PatternCondition<PsiElement>
{
    public SiblingMatcher()
    {
        super("Verify previous sibling.");
    }

    @Override
    public boolean accepts(@NotNull PsiElement psiElement, ProcessingContext processingContext)
    {
        YAMLKeyValue keyValue;
        LeafPsiElement previous;
        try {
            keyValue = (YAMLKeyValue)psiElement.getParent();
            previous = (LeafPsiElement)psiElement.getPrevSibling();
        }
        catch (Exception e) {
            return false;
        }

        String keyName = keyValue.getName();
        if (keyName == null) {
            return false;
        }

        IElementType elementType = previous.getElementType();

        if (keyName.equals("match")) {
            Integer parentTabLength = keyValue.getPrevSibling().getTextLength();
            Integer currentTabLength = previous.getTextLength();

            if (currentTabLength <= parentTabLength) {
                return false;
            }

            return elementType == YAMLTokenTypes.INDENT;
        }

        return elementType == PhpElementTypes.WHITE_SPACE;
    }
}
