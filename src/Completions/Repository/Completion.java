package Completions.Repository;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiUtilBase;
import org.jetbrains.annotations.NotNull;

public class Completion extends LookupElement
{
    protected String lookupValue;
    protected String returnValue;
    protected Boolean keepQuotes;

    @NotNull
    @Override
    public String getLookupString() { return lookupValue; }

    @Override
    public boolean isCaseSensitive() { return false; }

    @Override
    public void renderElement(LookupElementPresentation presentation)
    {
        if (!returnValue.equals(lookupValue)) {
            presentation.appendTailText("(" + returnValue + ")", true);
        }
        super.renderElement(presentation);
    }

    @Override
    public void handleInsert(InsertionContext context)
    {
        PsiElement cursorElement = PsiUtilBase.getElementAtCaret(context.getEditor());
        if (cursorElement == null) {
            return;
        }

        String completion = returnValue;
        if (keepQuotes) {
            String quote = cursorElement.getText().substring(0, 1);
            completion = quote + returnValue + quote;
        }

        ((LeafPsiElement)cursorElement).replaceWithText(completion);
    }
}

