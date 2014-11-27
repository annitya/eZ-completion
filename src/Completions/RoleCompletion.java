package Completions;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class RoleCompletion extends Completion
{
    protected Integer id;
    protected String identifier;

    @Override
    protected PsiElement buildCompletion(InsertionContext context, String identifier)
    {
        switch (identifier)
        {
            case "loadRole":
                return buildElement(context, id);
            case "loadRoleByIdentifier":
                return buildElement(context, this.identifier);
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
