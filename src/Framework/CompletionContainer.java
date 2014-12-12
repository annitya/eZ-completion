package Framework;

import Completions.*;

import java.util.ArrayList;

public class CompletionContainer
{
    protected ArrayList<ParameterCompletion> list;

    public ArrayList<Completion> getCompletions(String identifier)
    {
        for (ParameterCompletion completion : list) {
            if (completion.accepts(identifier)) {
                return completion.getCompletions();
            }
        }

        return new ArrayList<>();
    }
}
