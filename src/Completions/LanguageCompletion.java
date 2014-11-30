package Completions;

import org.jetbrains.annotations.NotNull;

public class LanguageCompletion extends Completion
{
    protected Integer id;
    protected String code;
    protected String name;

    @NotNull
    @Override
    public String getLookupString()
    {
        return name;
    }

    protected String buildCompletion(String identifier)
    {
        switch (identifier) {
            case "loadLanguageById":
                return format(id);
            case "loadLanguage":
                return format(code);
            default:
                return null;
        }
    }
}
