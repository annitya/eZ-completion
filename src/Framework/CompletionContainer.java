package Framework;

import Completions.Php.Content.Field;
import Completions.Php.EzCompletionProvider;
import Completions.Php.Repository.Completion;
import Framework.Entities.ContentType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class CompletionContainer
{
    protected ArrayList<EzCompletionProvider> list;
    protected ArrayList<ContentType> contentTypes;
    protected HashMap<String, HashMap<String, Field>> contentTypeFields;
    protected ArrayList<String> contentLanguages;

    public CompletionContainer()
    {
        list = new ArrayList<>();
        contentTypes = new ArrayList<>();
        contentLanguages = new ArrayList<>();
        contentTypeFields = new HashMap<>();
    }

    public ArrayList<EzCompletionProvider> getList(){ return list; }

    public ArrayList<String> getContentLanguages() { return contentLanguages; }

    public ArrayList<Completion> getContentTypeIdentifierCompletions()
    {
        return contentTypes
                .stream()
                .map(contentType -> new Completion()
                        .initializeSimpleCompletion(contentType.getName(), contentType.getIdentifier(), true))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Completion> getContentTypeIdCompletions()
    {
        return contentTypes
                .stream()
                .map(contentType -> new Completion()
                        .initializeSimpleCompletion(contentType.getName(), contentType.getId(), false))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public HashMap<String, HashMap<String, Field>> getContentTypeFields() { return contentTypeFields; }

    public Set<String> getFieldIdentifiers(String contentClass)
    {
        if (!contentTypeFields.containsKey(contentClass)) {
            return null;
        }

        return contentTypeFields.get(contentClass).keySet();
    }

    public void refresh(ArrayList<EzCompletionProvider> newList)
    {
        for (EzCompletionProvider p : list) {
            p.getCompletions().clear();

            if (newList.contains(p)) {
                p.getCompletions().addAll(newList.get(newList.indexOf(p)).getCompletions());
            }
        }
    }

    public boolean contentClassExists(String contentClass)
    {
        return contentTypeFields.containsKey(contentClass);
    }
}
