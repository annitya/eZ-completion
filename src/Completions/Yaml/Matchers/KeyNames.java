package Completions.Yaml.Matchers;

import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import fr.adrienbrault.idea.symfony2plugin.util.yaml.YamlHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.util.List;
import java.util.Set;

public class KeyNames extends PatternCondition<PsiElement>
{
    public KeyNames()
    {
        super("Controllers and templates, and matchers, oh my!");
    }

    @Override
    public boolean accepts(@NotNull PsiElement psiElement, ProcessingContext context)
    {
        // Thanks man.
        List<String> parentKeys = YamlHelper.getParentArrayKeys(psiElement);
        if (parentKeys.size() != 5) {
            return false;
        }

        if (!parentKeys.get(parentKeys.size() - 1).equals("system")) {
            return false;
        }

        YAMLKeyValue parent;
        try {
            parent = (YAMLKeyValue)psiElement.getParent().getParent();
            if (parent == null) {
                return false;
            }
            // What crawled up the .... of this one? $%&$% cannot be applied to...
            Set<String> existingKeys = YamlHelper.getKeySet(parent);
            if (existingKeys != null) {
                context.put("existingKeys", existingKeys.toArray(new String[0]));
            }
        }
        catch (Exception ignored) {}

        return true;
    }
}
