package Completions;

import org.jetbrains.annotations.NotNull;

public class FieldTypeCompletion extends Completion
{
    protected String identifier;

    @Override
    protected String buildCompletion(String identifier)
    {
        return format(this.identifier);
    }

    @NotNull
    @Override
    public String getLookupString()
    {
        return identifier;
    }
}
