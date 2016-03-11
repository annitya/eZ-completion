package Completions.Php.Repository;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiUtilBase;
import org.jetbrains.annotations.NotNull;

public class Completion extends LookupElement
{
    protected String lookupValue;
    protected String returnValue;
    protected Boolean keepQuotes;

    /**
     * This exists because funky constructor-chaining screwing things up.
     */
    public Completion initalizeSimpleCompletion(String value)
    {
        lookupValue = value;
        returnValue = value;
        keepQuotes = true;

        return this;
    }

    @NotNull
    @Override
    public String getLookupString() { return returnValue; }

    @Override
    public boolean isCaseSensitive() { return false; }

    @Override
    public void renderElement(LookupElementPresentation presentation)
    {
        if (!returnValue.equals(lookupValue)) {
            presentation.appendTailText("(" + lookupValue + ")", true);
        }
        super.renderElement(presentation);
    }

    @Override
    public void handleInsert(InsertionContext context)
    {
        if (keepQuotes) {
            return;
        }

        PsiElement rightSingleQuote = PsiUtilBase.getElementAtCaret(context.getEditor());
        if (rightSingleQuote == null || !isQuote(rightSingleQuote)) {
            return;
        }

        PsiElement previousSibling = rightSingleQuote.getPrevSibling();
        if (previousSibling == null) {
            return;
        }

        PsiElement leftSingleQuote = previousSibling.getPrevSibling();
        if (leftSingleQuote == null || !isQuote(leftSingleQuote)) {
            return;
        }

        leftSingleQuote.delete();
        rightSingleQuote.delete();
    }

    protected boolean isQuote(PsiElement element)
    {
        String text = element.getText();
        return text.equals("\'") || text.equals("\"");
    }
}

