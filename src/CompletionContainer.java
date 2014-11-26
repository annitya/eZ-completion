import java.util.ArrayList;
import java.util.HashMap;

public class CompletionContainer
{
    protected ArrayList<ContentTypeCompletion> contentClass;
    protected ArrayList<ContentTypeCompletion> contentClassGroup;
    protected HashMap<String, String> completionTypes;

    public CompletionContainer()
    {
        completionTypes = new HashMap<>();
        completionTypes.put("loadContentTypeGroup", "contentclassgroupid");
        completionTypes.put("loadContentTypeGroupByIdentifier", "contentclassgroup");
        completionTypes.put("loadContentTypeByIdentifier", "contentclass");
        completionTypes.put("loadContentType", "contentclassid");
    }

    public ArrayList<ContentTypeCompletion> getCompletions(String identifier)
    {
        switch (identifier) {
            case "loadContentTypeGroup":
            case "loadContentTypeGroupByIdentifier":
                return contentClassGroup;
            case "loadContentType":
            case "loadContentTypeByIdentifier":
            case "loadContentTypeByRemoteId":
                return contentClass;

            default:
                return new ArrayList<>();
        }
    }
}
