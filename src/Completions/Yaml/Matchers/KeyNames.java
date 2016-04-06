package Completions.Yaml.Matchers;

import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.intellij.util.containers.HashSet;
import fr.adrienbrault.idea.symfony2plugin.util.yaml.YamlHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLBlockMappingImpl;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

        try {
            YAMLBlockMappingImpl yamlBlockMapping;
            yamlBlockMapping = (YAMLBlockMappingImpl)psiElement.getParent().getParent();
            Set<String> existingKeys = yamlBlockMapping
                .getKeyValues()
                .stream()
                .map(YAMLKeyValue::getKeyText).collect(Collectors.toCollection(HashSet::new));

            context.put("existingKeys", existingKeys.toArray(new String[0]));
        }
        catch (Exception ignored) {}

        return true;
    }
}
