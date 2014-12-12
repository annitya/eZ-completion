package Framework;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import  com.intellij.codeInsight.completion.CompletionProvider;

public class eZCompletionProvider extends CompletionProvider
{
    protected static CompletionContainer completionContainer;

    protected eZCompletionProvider()
    {
        CompletionPreloader preloaderInstance = CompletionPreloader.getCurrentProject().getComponent(CompletionPreloader.class);
        completionContainer = preloaderInstance.getCompletions();
    }

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

//    @Nullable
//    protected static Integer getCurrentParameterIndex(PsiElement psiElement)
//    {
//        ParameterListImpl parameters = (ParameterListImpl)psiElement.getContext().getParent();
//        return getCurrentParameterIndex(parameters.getParameters(), psiElement);
//    }

//    @Nullable
//    public static Integer getCurrentParameterIndex(PsiElement[] parameters, PsiElement parameter)
//    {
//        for(int i = 0; i < parameters.length; i++) {
//            if(parameters[i].equals(parameter)) {
//                return i;
//            }
//        }
//
//        return null;
//    }
}
