package Completions;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class FieldTypeCompletion extends Completion
{
    protected String identifier;

    @Override
    protected PsiElement buildCompletion(InsertionContext context, String identifier)
    {
        return buildElement(context, this.identifier);
    }

    @NotNull
    @Override
    public String getLookupString()
    {
        return identifier;
    }
}
