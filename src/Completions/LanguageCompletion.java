package Completions;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.psi.PsiElement;
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

    protected PsiElement buildCompletion(InsertionContext context, String identifier)
    {
        switch (identifier) {
            case "loadLanguageById":
                return buildElement(context, id);
            case "loadLanguage":
                return buildElement(context, code);
            default:
                return null;
        }
    }
}
