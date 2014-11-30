package Completions;

import org.jetbrains.annotations.NotNull;

public class RoleCompletion extends Completion
{
    protected Integer id;
    protected String identifier;

    @Override
    protected String buildCompletion(String identifier)
    {
        switch (identifier)
        {
            case "loadRole":
                return format(id);
            case "loadRoleByIdentifier":
                return format(this.identifier);
            default:
                return null;
        }
    }

    @NotNull
    @Override
    public String getLookupString()
    {
        return identifier;
    }
}
