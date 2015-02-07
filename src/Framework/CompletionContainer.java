package Framework;

import Completions.*;

import java.util.ArrayList;

public class CompletionContainer
{
    protected ArrayList<ParameterCompletion> list;
    protected ArrayList<String> contentLanguages;

    public CompletionContainer()
    {
        list = new ArrayList<ParameterCompletion>();
        contentLanguages = new ArrayList<String>();
    }

    public ArrayList<ParameterCompletion> getList(){ return list; }

    public ArrayList<String> getContentLanguages() { return contentLanguages; }

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
