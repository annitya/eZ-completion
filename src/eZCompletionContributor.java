import com.google.gson.JsonObject;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.openapi.project.Project;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.psi.stubs.PhpConstantElementType;
import org.jetbrains.annotations.NotNull;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class eZCompletionContributor extends CompletionContributor
{
    protected static HashMap<String, ArrayList<String>> cache;
    protected static HashMap<String, String> completionTypes;

    protected static void setCompletionTypes()
    {
        completionTypes = new HashMap<>();
        completionTypes.put("loadContentTypeGroup", "contentclassgroupid");
        completionTypes.put("loadContentTypeGroupByIdentifier", "contentclassgroup");
        completionTypes.put("loadContentTypeByIdentifier", "contentclass");
        completionTypes.put("loadContentType", "contentclassid");
    }

    public eZCompletionContributor()
    {
        cache = new HashMap<>();
        setCompletionTypes();

        extend(CompletionType.BASIC, PlatformPatterns.psiElement(), new CompletionProvider<CompletionParameters>()
        {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result)
            {
                Project project = parameters.getEditor().getProject();
                if (project == null) {
                    return;
                }

                String lookupIdentifier = getLookupType(parameters.getPosition());
                if (!completionTypes.containsKey(lookupIdentifier)) {
                    return;
                }
                String lookupType = completionTypes.get(lookupIdentifier);

                String baseDir = project.getBaseDir().getCanonicalPath();
                performLookup(result, baseDir, lookupType);

            }
        });
    }

//    @Override
//    public boolean invokeAutoPopup(@NotNull PsiElement position, char typeChar)
//    {
//        String type = position
//                .getParent()
//                .getFirstChild()
//                .getNextSibling()
//                .getNextSibling()
//                .getText();
//
//        return completionTypes.containsKey(type);
//    }

    protected void performLookup(CompletionResultSet result, String baseDir, String type)
    {
        if (!cache.containsKey(type)) {
            cache.put(type, new ArrayList<String>());
            String command = baseDir + "/ezpublish/console ezcode:completion --type=" + type;

            Process process;
            try {
                process = Runtime.getRuntime().exec(command);
                process.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String completion;
                while ((completion = reader.readLine())!= null) {
                    cache.get(type).add(completion);
            }
            } catch (Exception e) {
                return;
            }
        }

        for (String completion : cache.get(type)) {
            // Contains id as well.
            if (completion.contains(";")) {
                String[] parts = completion.split(";");
                result.addElement(new eZCompletion(parts[0], parts[1]));
            }
            else {
                result.addElement(new eZCompletion(completion));
            }
        }

    }

    protected String getLookupType(PsiElement element)
    {
        PsiElement method = element
                .getParent()
                .getParent()
                .getParent()
                .getFirstChild()
                .getNextSibling()
                .getNextSibling();

        return method == null ? "" : method.getText();
    }
}

class eZCompletion extends LookupElement
{
    protected String value;
    protected String displayText;


    public eZCompletion(String value, String displayText)
    {
        this.value = value;
        this.displayText = displayText;
    }

    public eZCompletion(String entry)
    {
        value = entry;
        displayText = entry;
    }

    @NotNull
    @Override
    public String getLookupString() { return value; }

    @Override
    public void renderElement(LookupElementPresentation presentation) { presentation.setItemText(displayText); }

    @Override
    public void handleInsert(InsertionContext context)
    {
        super.handleInsert(context);
    }
}