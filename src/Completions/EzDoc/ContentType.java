package Completions.EzDoc;

import com.intellij.codeInsight.lookup.LookupElement;
import org.jetbrains.annotations.NotNull;

public class ContentType extends LookupElement
{
    protected String identifier;

    @Override
    public boolean isCaseSensitive() { return false; }

    @NotNull
    @Override
    public String getLookupString()
    {
        return identifier;
    }
}
