package Framework;

import Completions.EzDoc.ContentType;
import Completions.Repository.ParameterCompletion;
import java.util.ArrayList;
import java.util.HashMap;

public class CompletionContainer
{
    protected ArrayList<ParameterCompletion> list;
    protected ArrayList<ContentType> contentTypes;
    protected HashMap<String, HashMap<String, String>> contentTypeFields;
    protected ArrayList<String> contentLanguages;
    protected String includePath;

    public CompletionContainer()
    {
        list = new ArrayList<ParameterCompletion>();
        contentTypes = new ArrayList<ContentType>();
        contentLanguages = new ArrayList<String>();
        contentTypeFields = new HashMap<String, HashMap<String, String>>();
    }

    public String getIncludePath() { return includePath; }

    public ArrayList<ParameterCompletion> getList(){ return list; }

    public ArrayList<String> getContentLanguages() { return contentLanguages; }

    public ArrayList<ContentType> getContentTypes() { return contentTypes; }

    public HashMap<String, HashMap<String, String>> getContentTypeFields() { return contentTypeFields; }

    public void refresh(ArrayList<ParameterCompletion> newList)
    {
        for (ParameterCompletion p : list) {
            p.getCompletions().clear();

            if (newList.contains(p)) {
                p.getCompletions().addAll(newList.get(newList.indexOf(p)).getCompletions());
            }
        }
    }
}
