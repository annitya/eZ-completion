package Settings;

import Framework.CompletionPreloader;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class Service implements ProjectComponent
{
    public static final String DEFAULT_ENV = "dev";
    public static final String DEFAULT_EXECUTABLE = "ezpublish/console";
    public static final String LANGUAGE_UNAVAILABLE = "N/A";

    Project project;

    public Service(Project project)
    {
        this.project = project;
    }

    public static Service getInstance(Project project)
    {
        return project.getComponent(Service.class);
    }

    public Boolean getDisabled() { return getSetting("disabled", "false").equals("true"); }

    public void setDisabled(Boolean disabled) { setSetting("disabled", disabled.toString()); }

    public void refreshCompletions()
    {
        CompletionPreloader.getInstance(project).fetchCompletions();
    }

    public String getEnvironment()
    {
        return getSetting("environment", DEFAULT_ENV);
    }

    public void setEnvironment(String environment)
    {
        setSetting("environment", environment);
    }

    public void setExecutable(String executable)
    {
        setSetting("executable", executable);
    }

    public String getExecutable()
    {
        return getSetting("executable", DEFAULT_EXECUTABLE);
    }

    public String getLanguage()
    {
        return getSetting("language");
    }

    public void setLanguage(String language)
    {
        if (language.equals(LANGUAGE_UNAVAILABLE)) {
            return;
        }

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

    protected String getSetting(String name, String fallback)
    {
        String value = getSetting(name);
        return value != null ? value : fallback;

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
