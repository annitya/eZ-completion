package Framework;

import Completions.Content.Field;
import Completions.EzDoc.ContentType;
import Completions.EzCompletionProvider;
import java.util.ArrayList;
import java.util.HashMap;

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
