import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.project.Project;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class eZCompletionContributor extends CompletionContributor
{
    protected static ArrayList<String> cache;

    public eZCompletionContributor()
    {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(), new CompletionProvider<CompletionParameters>()
        {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result)
            {
                PsiElement element = parameters.getPosition();
                String lookupType = getLookupType(element, "loadContentTypeByIdentifier");
                if (lookupType.length() == 0) {
                    return;
                }

                Project project = parameters.getEditor().getProject();
                if (project == null) {
                    return;
                }
                String baseDir = project.getBaseDir().getCanonicalPath();
                performLookup(result, baseDir);
            }
        });
    }

    protected void performLookup(CompletionResultSet result, String baseDir)
    {
        if (cache == null) {
            cache = new ArrayList<>();

            String command = baseDir + "/../kavli.com/ezpublish/console devtools:contentclass";

            Process process;
            try {
                process = Runtime.getRuntime().exec(command);
                process.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String contentClass;
                while ((contentClass = reader.readLine())!= null) {
                    cache.add(contentClass);
                }

            } catch (Exception e) {
                return;
            }
        }

        for (String completion : cache) {
            result.addElement(new eZCompletion(completion));
        }
    }

    protected String getLookupType(PsiElement element, String searchText)
    {
        PsiElement method = element
                .getParent()
                .getParent()
                .getParent()
                .getChildren()[0]
                .getNextSibling()
                .getNextSibling();

        if (method == null) {
            return "";
        }

        String identifier = method.getText();
        if (!identifier.equals(searchText)) {
            return "";
        }

        return identifier;
    }
}

class eZCompletion extends LookupElement
{
    protected String entry;

    public eZCompletion(String lookupEntry)
    {
        entry = lookupEntry;
    }

    @NotNull
    @Override
    public String getLookupString()
    {
        return entry;
    }
}