package Completions.Yaml.Matchers;

import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;

public class ParentKey extends PatternCondition<PsiElement>
{
    protected String name;

    public ParentKey(String name)
    {
        super("Should be controller, template or match.");
        this.name = name;
    }

    @Override
    public boolean accepts(@NotNull PsiElement psiElement, ProcessingContext processingContext)
    {
        YAMLKeyValue keyValue = PsiTreeUtil.getParentOfType(psiElement, YAMLKeyValue.class);
        if (keyValue == null) {
            return false;
        }

        String keyName = keyValue.getName();
        if (keyName == null) {
            return false;
        }

        return keyName.equals(name);
    }
}
