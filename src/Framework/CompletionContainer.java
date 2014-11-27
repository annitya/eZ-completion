package Framework;

import Completions.*;
import com.intellij.codeInsight.lookup.LookupElement;

import java.util.ArrayList;

public class CompletionContainer
{
    protected ArrayList<ContentTypeCompletion> contentClass;
    protected ArrayList<ContentTypeCompletion> contentClassGroup;
    protected ArrayList<LanguageCompletion> languages;
    protected ArrayList<FieldTypeCompletion> fieldTypes;
    protected ArrayList<ObjectStateCompletion> objectStates;
    protected ArrayList<ObjectStateCompletion> objectStateGroups;
    protected ArrayList<RoleCompletion> roles;

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
            case "getFieldType":
            case "hasFieldType":
                completions.addAll(fieldTypes);
            break;
            case "loadObjectState":
                completions.addAll(objectStates);
            break;
            case "loadObjectStateGroup":
                completions.addAll(objectStateGroups);
            break;
            case "loadRole":
            case "loadRoleByIdentifier":
                completions.addAll(roles);
            break;
        }

        return completions;
    }
}
