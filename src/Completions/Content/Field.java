package Completions.Content;

public class Field
{
    protected String name;
    protected String fqn;
    protected String description;

    public String getFqn() { return fqn; }
    public String getDescription() { return description; }
    public String getName() { return name; }

    public boolean hasName() { return name != null && name.length() > 0; }
    public boolean hasFqn() { return fqn != null && fqn.length() > 0; }
    public boolean hasDescription() { return description != null && description.length() > 0; }
}
