package Framework;

import Completions.*;

import java.util.ArrayList;

public class CompletionContainer
{
    protected ArrayList<ParameterCompletion> list;

    public CompletionContainer()
    {
        list = new ArrayList<>();
    }

    public ArrayList<ParameterCompletion> getList(){ return list; }

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
