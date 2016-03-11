package Completions.Yaml.Providers;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.project.Project;
import com.intellij.util.ProcessingContext;
import fr.adrienbrault.idea.symfony2plugin.TwigHelper;

import java.util.Collection;

public class Templates extends YamlCompletionProvider
{
    @Override
    protected Collection<LookupElement> getLookupElements(Project project, ProcessingContext context)
    {
        return TwigHelper.getTwigLookupElements(project);
    }
}
