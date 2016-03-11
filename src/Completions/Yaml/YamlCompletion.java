package Completions.Yaml;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import org.jetbrains.annotations.NotNull;

public class YamlCompletion extends LookupElement
{
    protected String lookupValue;
    protected Boolean insertQuotes;

    public YamlCompletion(String lookupValue, Boolean insertQuotes)
    {
        this.lookupValue = lookupValue;
        this.insertQuotes = insertQuotes;
    }

    @Override
    public boolean isCaseSensitive() { return false; }

    @NotNull
    @Override
    public String getLookupString()
    {
        String lookupString = lookupValue + ": ";
        if (insertQuotes) {
            lookupString += "\"\"";
        }

        return lookupString;
    }

    @Override
    public void renderElement(LookupElementPresentation presentation)
    {
        presentation.setItemText(lookupValue);
    }

    @Override
    public void handleInsert(InsertionContext context)
    {
        if (!insertQuotes) {
            return;
        }

        context.getEditor().getCaretModel().getPrimaryCaret().moveCaretRelatively(-1, 0, false, false);
    }
}
