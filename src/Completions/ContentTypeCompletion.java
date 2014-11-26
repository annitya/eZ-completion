package Completions;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class ContentTypeCompletion extends Completion
{
    protected Integer id;
    protected String identifier;
    protected String remoteId;

    @NotNull
    @Override
    public String getLookupString() { return identifier; }

    @Override
    protected PsiElement buildCompletion(InsertionContext context, String identifier)
    {
        switch (identifier) {
            case "loadContentTypeGroup":
            case "loadContentType":
            case "loadContentTypeDraft":
                return buildElement(context, id);
            case "loadContentTypeByRemoteId":
                return buildElement(context, remoteId);
            default:
                return null;
        }
    }
}