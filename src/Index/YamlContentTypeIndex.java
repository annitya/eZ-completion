package Index;

import com.google.common.collect.Maps;
import com.intellij.openapi.project.DumbService;
import com.intellij.util.indexing.*;
import com.intellij.util.io.DataExternalizer;
import com.intellij.util.io.EnumeratorStringDescriptor;
import com.intellij.util.io.KeyDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLFile;
import java.util.Map;

public class YamlContentTypeIndex extends FileBasedIndexExtension<String, String>
{
    public static final ID<String, String> KEY = ID.create("flageolett.eZ.framework.yamlIndex");
    private final KeyDescriptor<String> myKeyDescriptor = new EnumeratorStringDescriptor();

    @NotNull
    @Override
    public ID<String, String> getName()
    {
        return KEY;
    }

    @NotNull
    @Override
    public DataIndexer<String, String, FileContent> getIndexer()
    {
        return this::getTemplateMatches;
    }

    @NotNull
    protected Map<String, String> getTemplateMatches(FileContent fileContent)
    {
        if (DumbService.isDumb(fileContent.getProject())) {
            return Maps.newHashMap();
        }

        YAMLFile yamlFile;
        try {
            yamlFile = (YAMLFile)fileContent.getPsiFile();
        }
        catch (Exception e) {
            return Maps.newHashMap();
        }

        YamlVisitor visitor = new YamlVisitor();
        yamlFile.accept(visitor);

        return visitor.getMap();
    }

    @NotNull
    @Override
    public KeyDescriptor<String> getKeyDescriptor()
    {
        return myKeyDescriptor;
    }

    @NotNull
    @Override
    public DataExternalizer<String> getValueExternalizer()
    {
        return new YamlDataExternalizer();
    }

    @NotNull
    @Override
    public FileBasedIndex.InputFilter getInputFilter()
    {
        try {
            Class.forName("org.jetbrains.yaml.YAMLFileType.YML");
        }
        catch (Exception e) {
            return file -> false;
        }

        return  file -> file.getFileType() == org.jetbrains.yaml.YAMLFileType.YML || "yml".equalsIgnoreCase(file.getExtension());
    }

    @Override
    public boolean dependsOnFileContent()
    {
        return true;
    }

    @Override
    public int getVersion()
    {
        return 1;
    }
}
