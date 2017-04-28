package Settings;

import Framework.CompletionContainer;
import Framework.CompletionPreloader;
import Framework.Database.EzConnection;
import Framework.Entities.DataSource;
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
import java.util.List;

public class EzPlugin implements Configurable
{
    protected JComponent component;
    protected JPanel panel;
    protected JLabel languageLabel;
    protected JComboBox<String> language;
    protected JLabel environmentLabel;
    protected JTextField environment;
    protected JLabel executableLabel;
    protected TextFieldWithBrowseButton executable;
    protected JLabel disableLabel;
    protected JCheckBox disablePlugin;
    private JComboBox<DataSource> database;

    protected Service settings;

    @Nullable
    @Override
    public JComponent createComponent()
    {
        settings = Service.getInstance(Util.currentProject());
        DefaultComboBoxModel<String> languageModel = createLanguageModel();
        if (languageModel == null || languageModel.getSize() == 0) {
            languageModel = new DefaultComboBoxModel<>();
            languageModel.addElement(Service.UNAVAILABLE);
            language.setEnabled(false);
            languageLabel.setEnabled(false);
            language.setEditable(false);
            language.setToolTipText("Completions are not ready. Try to refresh, and then open preferences again.");
        }
        else {
            String selectedLanguage = settings.getLanguage();
            if (selectedLanguage != null) {
                languageModel.setSelectedItem(selectedLanguage);
            }
        }

        database.setModel(createDatabaseModel());
        language.setModel(languageModel);
        environment.setText(settings.getEnvironment());
        executable.setText(settings.getExecutable());
        disablePlugin.setSelected(settings.getDisabled());

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
        completionContainer.getContentLanguages().forEach(model::addElement);

        return model;
    }

    protected DefaultComboBoxModel<DataSource> createDatabaseModel()
    {
        DefaultComboBoxModel<DataSource> databaseModel = new DefaultComboBoxModel<>();
        List<DataSource> dataSources = EzConnection.getDataSources(Util.currentProject());

        if (dataSources.size() == 0) {
            dataSources.add(new DataSource(Service.UNAVAILABLE, Service.UNAVAILABLE));
            database.setEnabled(false);
            database.setToolTipText("You need to configure a datasource first.");
        }
        else {
            String selectedDatabase = settings.getDatabaseConnectionId();

            dataSources
                    .stream()
                    .filter(d -> d.getId().equals(selectedDatabase))
                    .findFirst()
                    .ifPresent(databaseModel::setSelectedItem);
        }

        dataSources.forEach(databaseModel::addElement);

        return databaseModel;
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

        Object selectedDatabaseItem = database.getSelectedItem();
        if (selectedDatabaseItem != null && database.isEnabled()) {
            DataSource dataSource = (DataSource)selectedDatabaseItem;
            String storedDataSource = settings.getDatabaseConnectionId();

            if (storedDataSource == null || !storedDataSource.equals(dataSource.getId())) {
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

        DataSource selectedDataSource = (DataSource)database.getSelectedItem();
        settings.setDatabaseConnection(selectedDataSource.getId());

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
