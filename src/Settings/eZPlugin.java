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
    protected JLabel languageLabel;
    protected JComboBox<String> language;
    protected JLabel environmentLabel;
    protected JTextField environment;
    protected Service settings;

    @Nullable
    @Override
    public JComponent createComponent()
    {
        settings = Service.getInstance(Util.currentProject());

        DefaultComboBoxModel<String> model = createLanguageModel();
        if (model == null || model.getSize() == 0) {
            model = new DefaultComboBoxModel<>();
            model.addElement("N/A");
            languageLabel.setEnabled(false);
            language.setEditable(false);
            language.setToolTipText("Completions are not ready. Try to refresh, and then open preferences again.");

        }
        else {
            String selectedLanguage = settings.getLanguage();
            if (selectedLanguage != null) {
                model.setSelectedItem(selectedLanguage);
            }

        }

        language.setModel(model);
        environment.setText(settings.getEnvironment());

        component = panel;
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
        Object selectedLanguageItem = language.getSelectedItem();
        if (selectedLanguageItem != null) {
            String selectedLanguage = selectedLanguageItem.toString();
            String storedLanguage = settings.getLanguage();

            if (storedLanguage == null || !selectedLanguage.equals(storedLanguage)) {
                return true;
            }
        }

        String selectedEnvironment = environment.getText();
        String storedEnvironment = settings.getEnvironment();

        return !storedEnvironment.equals(selectedEnvironment);
    }

    @Override
    public void apply() throws ConfigurationException
    {
        String selectedLanguage = language.getSelectedItem().toString();
        settings.setLanguage(selectedLanguage);

        String selectedEnvironment = environment.getText();
        settings.setEnvironment(selectedEnvironment);

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
