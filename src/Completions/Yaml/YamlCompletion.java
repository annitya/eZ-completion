package Completions.Yaml;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import org.jetbrains.annotations.NotNull;

public class YamlCompletion extends LookupElement
{
    protected String lookupValue;

    public YamlCompletion(String lookupValue)
    {
        this.lookupValue = lookupValue;
    }

    @Override
    public boolean isCaseSensitive() { return true; }

    @NotNull
    @Override
    public String getLookupString()
    {
        return lookupValue + ": ";
    }

    @Override
    public void renderElement(LookupElementPresentation presentation)
    {
        presentation.setItemText(lookupValue);
    }
}
