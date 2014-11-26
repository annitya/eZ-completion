package Completions;

import Framework.eZCompletionContributor;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiUtilBase;
import com.jetbrains.php.lang.psi.PhpPsiElementFactory;

abstract public class Completion extends LookupElement
{
    protected abstract PsiElement buildCompletion(InsertionContext context, String identifier);

    @Override
    public void handleInsert(InsertionContext context)
    {
        PsiElement cursorElement = PsiUtilBase.getElementAtCaret(context.getEditor());
        if (cursorElement == null) {
            return;
        }

        String lookupIdentifier = eZCompletionContributor.getLookupType(cursorElement);
        PsiElement completion = buildCompletion(context, lookupIdentifier);

        if (completion != null) {
            cursorElement.replace(completion);
        }
    }

    protected PsiElement buildElement(InsertionContext context, Integer numeric)
    {
        return createElement(context, numeric.toString());
    }

    protected PsiElement buildElement(InsertionContext context, String value)
    {
        value = "'" + value + "'";
        return createElement(context, value);
    }

    protected PsiElement createElement(InsertionContext context, String value)
    {
        return PhpPsiElementFactory.createFromText(context.getProject(), LeafPsiElement.class, value);
    }
}

