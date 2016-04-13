package Index;

import com.google.common.collect.Maps;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import fr.adrienbrault.idea.symfony2plugin.TwigHelper;
import org.jetbrains.yaml.psi.*;
import org.jetbrains.yaml.psi.impl.YAMLBlockMappingImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class YamlVisitor extends PsiRecursiveElementWalkingVisitor
{
    ArrayList<String> contentTypeMatchers = new ArrayList<>(Arrays.asList(
//        "Id\\ContentType",
//        "Id\\ParentContentType",
        "Identifier\\ContentType",
        "Identifier\\ParentContentType"
    ));

    protected Map<String, String> map;

    public YamlVisitor()
    {
        map = Maps.newHashMap();
    }

    public Map<String, String> getMap() { return map; }

    @Override
    public void visitElement(PsiElement element)
    {
        YAMLKeyValue keyValue;
        try {
            keyValue = (YAMLKeyValue)element;
        }
        catch (Exception e) {
            super.visitElement(element);
            return;
        }

        String keyName = keyValue.getName();
        if (keyName == null) {
            super.visitElement(element);
            return;
        }

        if (!contentTypeMatchers.contains(keyName)) {
            super.visitElement(element);
            return;
        }

        String identifier = getContentTypeIdentifier(keyValue);
        if (identifier == null) {
            return;
        }

        String twigFileIdentifier = getTwigFileIdentifier(element);
        if (twigFileIdentifier == null) {
            return;
        }

        map.put(twigFileIdentifier, identifier);
    }

    public String getTwigFileIdentifier(PsiElement element)
    {
        PsiElement parent = element.getParent();
        if (parent == null) {
            return null;
        }

        YAMLBlockMappingImpl mapDefinition = PsiTreeUtil.getParentOfType(parent, YAMLBlockMappingImpl.class);
        if (mapDefinition == null) {
            return null;
        }

        for (PsiElement psiElement :  mapDefinition.getChildren()) {
            YAMLKeyValue child;
            try {
                child = (YAMLKeyValue)psiElement;
            }
            catch (Exception e) {
                continue;
            }

            String childName = child.getName();
            if (childName == null) {
                continue;
            }

            if (!childName.equals("template")) {
                continue;
            }

            String templateIdentifier = child.getValueText();
            PsiFile templatePsiFile = TwigHelper.getTemplateFileByName(element.getProject(), templateIdentifier);
            if (templatePsiFile == null) {
                continue;
            }

            return templatePsiFile.getVirtualFile().getCanonicalPath();
        }

        return null;
    }

    public String getContentTypeIdentifier(YAMLKeyValue keyValue)
    {
        YAMLValue value = keyValue.getValue();
        if (value == null) {
            return null;
        }

        // Handle arrays with a single value.
        try {
            YAMLSequence yamlSequence = (YAMLSequence)value;
            List<YAMLSequenceItem> items = yamlSequence.getItems();
            if (items.size() != 1) {
                return null;
            }

            return items.get(0).getText();
        }
        catch (Exception ignored) {}

        try {
            YAMLScalar scalar = (YAMLScalar)value;
            return scalar.getTextValue();
        }
        catch (Exception e) {
            return null;
        }
    }
}
