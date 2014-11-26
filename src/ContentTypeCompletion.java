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
        PsiElement completion = null;
        switch (lookupIdentifier) {
            case "loadContentTypeGroup":
            case "loadContentType":
            case "loadContentTypeDraft":
                completion = PhpPsiElementFactory.createFromText(context.getProject(), LeafPsiElement.class, id.toString());
            break;
            case "loadContentTypeByRemoteId":
                completion = PhpPsiElementFactory.createFromText(context.getProject(), LeafPsiElement.class, "'" + remoteId + "'");
            break;
        }

        if (completion != null) {
            cursorElement.replace(completion);
        }
    }
}