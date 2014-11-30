package Framework;

import com.intellij.codeInsight.completion.*;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class eZCompletionContributor extends CompletionContributor
{
    protected static CompletionContainer completionContainer;

    public eZCompletionContributor()
    {
        CompletionPreloader preloaderInstance = CompletionPreloader.getCurrentProject().getComponent(CompletionPreloader.class);
        completionContainer = preloaderInstance.getCompletions();

        extend(CompletionType.BASIC, PlatformPatterns.psiElement(), new CompletionProvider<CompletionParameters>()
        {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result)
            {
                if (completionContainer == null) {
                    return;
                }

                String lookupIdentifier = getLookupType(parameters.getPosition());
                if (lookupIdentifier == null) {
                    return;
                }

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

    public static String getLookupType(PsiElement element)
    {
        // Completions without quotes.
        try {
            if (((LeafPsiElement) element).getElementType().toString().equals("identifier")) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

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