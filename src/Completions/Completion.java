package Completions;

import com.google.gson.*;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiUtilBase;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

public class Completion extends LookupElement implements JsonDeserializer<Completion>
{
    protected String lookupValue;
    protected String returnValue;
    protected Boolean keepQuotes;

    public Completion() {}

    public Completion(String lookupValue, String returnValue, Boolean keepQuotes)
    {
        this.lookupValue = lookupValue;
        this.returnValue = returnValue;
        this.keepQuotes = keepQuotes;
    }

    @NotNull
    @Override
    public String getLookupString() { return lookupValue; }

    @Override
    public void handleInsert(InsertionContext context)
    {
        PsiElement cursorElement = PsiUtilBase.getElementAtCaret(context.getEditor());
        if (cursorElement == null) {
            return;
        }

        String completion = keepQuotes ? "'" + returnValue + "'" : returnValue;

        ((LeafPsiElement)cursorElement).replaceWithText(completion);
    }

    @Override
    public Completion deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
        final JsonObject jsonObject = jsonElement.getAsJsonObject();

        final JsonElement jsonLookupValue = jsonObject.get("lookupValue");
        lookupValue = jsonLookupValue.getAsString();

        final JsonElement jsonReturnValue = jsonObject.get("returnValue");
        try {
            final Integer intValue = jsonReturnValue.getAsInt();
            returnValue = intValue.toString();
            keepQuotes = false;
        } catch (Exception e) {
            returnValue = jsonReturnValue.getAsString();
            keepQuotes = true;
        }

        return new Completion(lookupValue, returnValue, keepQuotes);
    }
}

