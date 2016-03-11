package Completions.Yaml.Providers;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.project.Project;
import com.intellij.util.ProcessingContext;
import fr.adrienbrault.idea.symfony2plugin.util.controller.ControllerIndex;

import java.util.List;

public class Controller extends YamlCompletionProvider
{
    protected List<LookupElement> getLookupElements(Project project, ProcessingContext context)
    {
        return ControllerIndex.getControllerLookupElements(project);
    }
}
