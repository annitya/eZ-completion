package Completions;

import org.jetbrains.annotations.NotNull;

public class ObjectStateCompletion extends Completion
{
    protected Integer id;
    protected String name;

    @Override
    protected String buildCompletion(String identifier)
    {
        return id.toString();
    }

    @NotNull
    @Override
    public String getLookupString()
    {
        return name;
    }
}
