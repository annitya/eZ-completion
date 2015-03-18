package Framework;

import Completions.Annotation.ContentType;
import Completions.Repository.ParameterCompletion;

import java.util.ArrayList;

public class CompletionContainer
{
    protected ArrayList<ParameterCompletion> list;
    protected ArrayList<ContentType> contentTypes;
    protected ArrayList<String> contentLanguages;

    public CompletionContainer()
    {
        list = new ArrayList<ParameterCompletion>();
        contentTypes = new ArrayList<ContentType>();
        contentLanguages = new ArrayList<String>();
    }

    public ArrayList<ParameterCompletion> getList(){ return list; }

    public ArrayList<String> getContentLanguages() { return contentLanguages; }

    public ArrayList<ContentType> getContentTypes() { return contentTypes; }

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
