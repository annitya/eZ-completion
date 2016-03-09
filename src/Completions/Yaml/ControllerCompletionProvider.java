package Completions.Yaml;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.project.Project;
import fr.adrienbrault.idea.symfony2plugin.util.controller.ControllerIndex;

import java.util.List;

public class ControllerCompletionProvider extends YamlCompletionProvider
{
    protected List<LookupElement> getLookupElements(Project project)
    {
        return ControllerIndex.getControllerLookupElements(project);
    }
}
