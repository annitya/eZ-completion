package Settings;

import Framework.CompletionContainer;
import Framework.CompletionPreloader;
import Framework.Util;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class eZPlugin implements Configurable
{
    protected JComponent component;
    protected JPanel panel;
    protected JLabel languageLabel;
    protected JComboBox language;
    protected JLabel environmentLabel;
    protected JTextField environment;
    protected JLabel executableLabel;
    protected TextFieldWithBrowseButton executable;
    protected JLabel disableLabel;
    protected JCheckBox disablePlugin;

    protected Service settings;

    @Nullable
    @Override
    public JComponent createComponent()
    {
        settings = Service.getInstance(Util.currentProject());
        DefaultComboBoxModel model = createLanguageModel();
        if (model == null || model.getSize() == 0) {
            model = new DefaultComboBoxModel();
            model.addElement(Service.LANGUAGE_UNAVAILABLE);
            language.setEnabled(false);
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
        executable.setText(settings.getExecutable());
        disablePlugin.setSelected(settings.getDisabled());

        FileChooserDescriptor fileDescriptor = FileChooserDescriptorFactory.createSingleLocalFileDescriptor();
        MouseListener pathButtonMouseListener = createPathButtonMouseListener(executable.getTextField(), fileDescriptor);
        executable.getButton().addMouseListener(pathButtonMouseListener);

        component = panel;
        return component;
    }

    protected DefaultComboBoxModel createLanguageModel()
    {
        CompletionPreloader preloader = CompletionPreloader.getInstance(Util.currentProject());
        CompletionContainer completionContainer = preloader.getCurrentCompletions();

        if (completionContainer == null) {
            return null;
        }

        DefaultComboBoxModel model = new DefaultComboBoxModel();
        completionContainer.getContentLanguages().forEach(model::addElement);

        return model;
    }

    @Override
    public boolean isModified()
    {
        Object selectedLanguageItem = language.getSelectedItem();
        if (selectedLanguageItem != null && language.isEnabled()) {
            String selectedLanguage = selectedLanguageItem.toString();
            String storedLanguage = settings.getLanguage();

            if (storedLanguage == null || !selectedLanguage.equals(storedLanguage)) {
                return true;
            }
        }

        if (settings.getDisabled() != disablePlugin.isSelected()) {
            return true;
        }

        String selectedEnvironment = environment.getText();
        String storedEnvironment = settings.getEnvironment();

        String selectedExecutable = executable.getText();
        String storedExecutable = settings.getExecutable();

        return !storedEnvironment.equals(selectedEnvironment) || !storedExecutable.equals(selectedExecutable);
    }

    @Override
    public void apply() throws ConfigurationException
    {
        String selectedLanguage = language.getSelectedItem().toString();
        settings.setLanguage(selectedLanguage);

        String selectedEnvironment = environment.getText();
        settings.setEnvironment(selectedEnvironment);

        String selectedExecutable = executable.getText();
        settings.setExecutable(selectedExecutable);

        boolean isDisabled = disablePlugin.isSelected();
        settings.setDisabled(isDisabled);

        if (!isDisabled) {
            settings.refreshCompletions();
        }
    }

    protected MouseListener createPathButtonMouseListener(final JTextField textField, final FileChooserDescriptor fileChooserDescriptor) {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                VirtualFile projectDirectory = Util.currentProject().getBaseDir();
                VirtualFile selectedFile = FileChooser.chooseFile(
                        fileChooserDescriptor,
                        Util.currentProject(),
                        VfsUtil.findRelativeFile(textField.getText(), projectDirectory)
                );

                if (null == selectedFile) {
                    return; // Ignore but keep the previous path
                }

                String path = VfsUtil.getRelativePath(selectedFile, projectDirectory, '/');
                if (null == path) {
                    path = selectedFile.getPath();
                }

                textField.setText(path);
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
            }
        };
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
