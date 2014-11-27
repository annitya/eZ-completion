package Completions;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class ObjectStateCompletion extends Completion
{
    protected Integer id;
    protected String name;

    @Override
    protected PsiElement buildCompletion(InsertionContext context, String identifier)
    {
        return buildElement(context, id);
    }

    @NotNull
    @Override
    public String getLookupString()
    {
        return name;
    }
}
