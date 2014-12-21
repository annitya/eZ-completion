package Framework;

import Completions.*;

import java.util.ArrayList;

public class CompletionContainer
{
    protected ArrayList<ParameterCompletion> list;

    public ArrayList<ParameterCompletion> getList() { return list; }

    public ArrayList<ParameterCompletion> refresh(ArrayList<ParameterCompletion> newList)
    {
        for (ParameterCompletion p : list) {
            p.getCompletions().clear();

            if (newList.contains(p)) {
                p.getCompletions().addAll(newList.get(newList.indexOf(p)).getCompletions());
            }
        }

        ArrayList<ParameterCompletion> newCompletions = new ArrayList<>();

        for (ParameterCompletion p : newList) {
            if (!list.contains(p)) {
                newCompletions.add(p);
            }
        }

        return newCompletions;
    }
}
