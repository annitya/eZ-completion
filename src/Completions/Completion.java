package Completions;

import Framework.eZCompletionContributor;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiUtilBase;

abstract public class Completion extends LookupElement
{
    protected abstract String buildCompletion(String identifier);

    @Override
    public void handleInsert(InsertionContext context)
    {
        PsiElement cursorElement = PsiUtilBase.getElementAtCaret(context.getEditor());
        if (cursorElement == null) {
            return;
        }

        String lookupIdentifier = eZCompletionContributor.getLookupType(cursorElement);
        String completion = buildCompletion(lookupIdentifier);

        if (completion != null) {
            ((LeafPsiElement)cursorElement).replaceWithText(completion);
        }
    }

    protected String format(String value)
    {
        return "'" + value + "'";
    }

    protected String format(Integer value)
    {
        return value.toString();
    }
}

