package Settings;

import Framework.CompletionPreloader;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class Service implements ProjectComponent
{
    public static final String DEFAULT_ENV = "dev";
    Project project;

    public Service(Project project)
    {
        this.project = project;
    }

    public static Service getInstance(Project project)
    {
        return project.getComponent(Service.class);
    }

    public void refreshCompletions()
    {
        CompletionPreloader.getInstance(project).fetchCompletions();
    }

    public String getEnvironment()
    {
        String environment = getSetting("environment");
        if (environment == null) {
            return DEFAULT_ENV;
        }

        return environment;
    }

    public void setEnvironment(String environment)
    {
        setSetting("environment", environment);
    }

    public String getLanguage()
    {
        return getSetting("language");
    }

    public void setLanguage(String language)
    {
        setSetting("language", language);
    }

    protected void setSetting(String name, String value)
    {
        getStorage().setValue(uniqueName(name), value);
    }

    protected String getSetting(String name)
    {
        return getStorage().getValue(uniqueName(name));
    }

    protected String uniqueName(String name)
    {
        return "eZ_" + name;
    }

    protected PropertiesComponent getStorage()
    {
        return PropertiesComponent.getInstance(project);
    }

    @Override
    public void projectOpened() {}

    @Override
    public void projectClosed() {}

    @Override
    public void initComponent() {}

    @Override
    public void disposeComponent() {}

    @NotNull
    @Override
    public String getComponentName()
    {
        return "Settings.Service";
    }
}
