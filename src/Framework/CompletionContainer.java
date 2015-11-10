package Framework;

import Completions.Content.Field;
import Completions.EzDoc.ContentType;
import Completions.EzCompletionProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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

    public ArrayList<ContentType> getContentTypes() { return contentTypes; }

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
