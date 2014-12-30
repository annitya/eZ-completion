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
    protected JComboBox<String> language;
    protected JLabel environmentLabel;
    protected JTextField environment;
    protected JLabel executableLabel;
    protected TextFieldWithBrowseButton executable;

    protected Service settings;

    @Nullable
    @Override
    public JComponent createComponent()
    {
        settings = Service.getInstance(Util.currentProject());
        DefaultComboBoxModel<String> model = createLanguageModel();
        if (model == null || model.getSize() == 0) {
            model = new DefaultComboBoxModel<>();
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

        FileChooserDescriptor fileDescriptor = FileChooserDescriptorFactory.createSingleLocalFileDescriptor();
        MouseListener pathButtonMouseListener = createPathButtonMouseListener(executable.getTextField(), fileDescriptor);
        executable.getButton().addMouseListener(pathButtonMouseListener);

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

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
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

        settings.refreshCompletions();
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
