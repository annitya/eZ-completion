package Completions.Yaml.Providers;

import Completions.Yaml.YamlCompletion;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.project.Project;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class MatchTypes extends YamlCompletionProvider
{
    protected static final String MATCHER_FACTORY_CLASS = "eZ\\Publish\\Core\\MVC\\Symfony\\Matcher\\AbstractMatcherFactory";
    protected static final String MATCHER_INTERFACE = "eZ\\Publish\\Core\\MVC\\Symfony\\Matcher\\MatcherInterface";
    protected static final String CONST_FIELD_NAME = "MATCHER_RELATIVE_NAMESPACE";

    protected Boolean insertQuotes(String fqn)
    {
        ArrayList<String> hasValueCompletions = new ArrayList<>(Arrays.asList(
            "Id\\ContentType",
            "Id\\ParentContentType",
            "Id\\ContentTypeGroup",
            "Id\\Section",
            "Identifier\\ContentType",
            "Identifier\\ParentContentType",
            "Identifier\\Section"
        ));

        return hasValueCompletions.contains(fqn);
    }

    @Override
    protected Collection<LookupElement> getLookupElements(Project project, ProcessingContext context)
    {
        Collection<PhpClass> matchers = PhpIndex.getInstance(project).getAllSubclasses(MATCHER_INTERFACE);
        matchers.removeIf(phpClass -> phpClass.isAbstract() || phpClass.isInterface() || phpClass.isTrait());
        List<String> relativeNamespaces = getRelativeNamespaces(project);

        List<LookupElement> matcherElements = new ArrayList<>();
        for (PhpClass matcher : matchers) {
            String fqn = matcher.getFQN();
            for (String relativeNamespace : relativeNamespaces) {
                fqn = fqn.replace(relativeNamespace, "");
            }

            matcherElements.add(new YamlCompletion(fqn, insertQuotes(fqn)));
        }

        return matcherElements;
    }

    protected List<String> getRelativeNamespaces(Project project)
    {
        Collection<PhpClass> matcherFactories = PhpIndex.getInstance(project).getDirectSubclasses(MATCHER_FACTORY_CLASS);
        List<String> relativeNamespaces = new ArrayList<>();

        matcherFactories.forEach(phpClass -> phpClass.getFields().forEach((Consumer<Field>) field -> {
            if (!field.getName().equals(CONST_FIELD_NAME)) {
                return;
            }

            StringLiteralExpression fieldValue;
            try {
                fieldValue = (StringLiteralExpression)field.getDefaultValue();
                if (fieldValue == null) {
                    return;
                }
            }
            catch (Exception e) {
                return;
            }

            String formattedNamespace = "\\" + fieldValue.getContents().replace("\\\\", "\\") + "\\";
            relativeNamespaces.add(formattedNamespace);
        }));

        return relativeNamespaces;
    }
}
