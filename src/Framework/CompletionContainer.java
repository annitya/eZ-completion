package Framework;

import Completions.ContentTypeCompletion;
import Completions.LanguageCompletion;
import com.intellij.codeInsight.lookup.LookupElement;

import java.util.ArrayList;

public class CompletionContainer
{
    protected ArrayList<ContentTypeCompletion> contentClass;
    protected ArrayList<ContentTypeCompletion> contentClassGroup;
    protected ArrayList<LanguageCompletion> languages;

    public ArrayList<LookupElement> getCompletions(String identifier)
    {
        ArrayList<LookupElement> completions = new ArrayList<>();

        switch (identifier) {
            case "loadContentTypeGroup":
            case "loadContentTypeGroupByIdentifier":
                completions.addAll(contentClassGroup);
            break;
            case "loadContentType":
            case "loadContentTypeByIdentifier":
            case "loadContentTypeByRemoteId":
            case "loadContentTypeDraft":
                completions.addAll(contentClass);
            break;
            case "loadLanguage":
            case "loadLanguageById":
                completions.addAll(languages);
            break;
        }

        return completions;
    }
}
