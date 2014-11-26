import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiUtilBase;
import com.jetbrains.php.lang.psi.PhpPsiElementFactory;
import org.jetbrains.annotations.NotNull;

class ContentTypeCompletion extends LookupElement
{
    protected Integer id;
    protected String identifier;
    protected String remoteId;

    @NotNull
    @Override
    public String getLookupString() {
        return identifier;
    }

    @Override
    public void renderElement(LookupElementPresentation presentation) { presentation.setItemText(identifier); }

    @Override
    public void handleInsert(InsertionContext context)
    {
        PsiElement cursorElement = PsiUtilBase.getElementAtCaret(context.getEditor());
        if (cursorElement == null) {
            return;
        }

        String lookupIdentifier = eZCompletionContributor.getLookupType(cursorElement);
        PsiElement completion;
        switch (lookupIdentifier) {
            case "loadContentTypeGroup":
            case "loadContentType":
                completion = PhpPsiElementFactory.createFromText(context.getProject(), LeafPsiElement.class, id.toString());
                replaceElement(context, cursorElement, completion);
            break;
            case "loadContentTypeByRemoteId":
                completion = PhpPsiElementFactory.createFromText(context.getProject(), LeafPsiElement.class, "'" + remoteId + "'");
                replaceElement(context, cursorElement, completion);
            break;
        }
    }

    protected void replaceElement(InsertionContext context, PsiElement replace, PsiElement completion)
    {
        if (completion == null) {
            return;
        }

        replace.getParent().add(completion);
        context.getEditor().getCaretModel().getCurrentCaret().moveCaretRelatively(completion.getTextLength() + 1, 0, false, false);
        replace.delete();
    }
}