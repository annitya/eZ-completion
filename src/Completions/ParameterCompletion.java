package Completions;

import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ParameterCompletion extends PatternCondition<PsiElement>
{
    protected int parameterIndex;
    protected String method;
    protected ArrayList<Completion> completions;

    public ParameterCompletion(String debugMethodName) { super(debugMethodName); }

    @Override
    public boolean accepts(@NotNull PsiElement psiElement, ProcessingContext context)
    {
        return parameterIndex == 0;
    }

    public boolean accepts (String identifier)
    {
        return method.equals(identifier);
    }

    public ArrayList<Completion> getCompletions()
    {
        return completions;
    }
}
