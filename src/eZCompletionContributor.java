import com.intellij.codeInsight.completion.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.Future;

public class eZCompletionContributor extends CompletionContributor
{
    protected static CompletionContainer completionContainer;
    protected Future consoleServicePromise;

    public eZCompletionContributor()
    {
        try {
             consoleServicePromise = ApplicationManager.getApplication().executeOnPooledThread(new ConsoleService());
        } catch (Exception e) {
            return;
        }

        extend(CompletionType.BASIC, PlatformPatterns.psiElement(), new CompletionProvider<CompletionParameters>()
        {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result)
            {
            try {
                completionContainer = (CompletionContainer)consoleServicePromise.get();
            } catch (Exception e) {
                return;
            }

            if (completionContainer == null) {
                return;
            }

            String lookupIdentifier = getLookupType(parameters.getPosition());
            result.addAllElements(completionContainer.getCompletions(lookupIdentifier));
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

    protected static String getLookupType(PsiElement element)
    {
        try {
            return element
                    .getParent()
                    .getParent()
                    .getParent()
                    .getFirstChild()
                    .getNextSibling()
                    .getNextSibling()
                    .getText();
        }
        catch (Exception e) {
            return null;
        }
    }
}