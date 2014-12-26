package Settings;

import Framework.CompletionContainer;
import Framework.CompletionPreloader;
import Framework.Util;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;

public class eZPlugin implements Configurable
{
    protected JComponent component;
    protected JPanel panel;
    protected JLabel label;
    protected JComboBox<String> language;
    protected Service settings;

    @Nullable
    @Override
    public JComponent createComponent()
    {
        settings = Service.getInstance(Util.currentProject());

        DefaultComboBoxModel<String> model = createLanguageModel();
        if (model == null || model.getSize() == 0) {
            return notAvailableYet();
        }

        String selectedLanguage = settings.getLanguage();
        if (selectedLanguage != null) {
            model.setSelectedItem(selectedLanguage);
        }

        language.setModel(model);

        component = panel;
        return component;
    }

    protected JComponent notAvailableYet()
    {
        JPanel unavailablePanel = new JPanel();
        JLabel unavailable = new JLabel();
        unavailable.setText("Completions are not ready. Try to refresh, and then open preferences again.");
        unavailablePanel.add(unavailable);

        component = unavailablePanel;
        return component;
    }

    protected DefaultComboBoxModel<String> createLanguageModel()
    {
        CompletionPreloader preloader = CompletionPreloader.getInstance(Util.currentProject());
        CompletionContainer completionContainer = preloader.getCurrentCompletions();

        if (completionContainer == null) {
            return null;
        }

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel();
        for (String contentLanguage : completionContainer.getContentLanguages()) {
            model.addElement(contentLanguage);
        }

        return model;
    }

    @Override
    public boolean isModified()
    {
        String selectedLanguage = language.getSelectedItem().toString();
        String storedLanguage = settings.getLanguage();

        return storedLanguage == null || !selectedLanguage.equals(storedLanguage);
    }

    @Override
    public void apply() throws ConfigurationException
    {
        String selectedLanguage = language.getSelectedItem().toString();
        settings.setLanguage(selectedLanguage);
        settings.refreshCompletions();
    }

    @Override
    public void reset()
    {

    }

    @Override
    public void disposeUIResources()
    {

    }

    @Nls
    @Override
    public String getDisplayName() { return "eZ Plugin"; }

    @Nullable
    @Override
    public String getHelpTopic()
    {
        return null;
    }
}
