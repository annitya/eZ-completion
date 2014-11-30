package Completions;

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
    protected String buildCompletion(String identifier)
    {
        switch (identifier) {
            case "loadContentTypeGroup":
            case "loadContentType":
            case "loadContentTypeDraft":
                return format(id);
            case "loadContentTypeByRemoteId":
                return format(remoteId);
            default:
                return null;
        }
    }
}