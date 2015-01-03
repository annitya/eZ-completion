package Actions;

import Framework.Console.ConsoleService;
import Framework.Console.QueryCommand;
import Framework.Util;
import com.intellij.codeInsight.intention.impl.BaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.php.lang.psi.elements.Method;
import org.jetbrains.annotations.NotNull;

public class Query extends BaseIntentionAction
{

    public static final String SEARCHSERVICE_FQN = "\\eZ\\Publish\\API\\Repository\\SearchService";
    public static final String SEARCHSERVICE_METHOD = "findContent";

    @NotNull
    @Override
    public String getFamilyName()
    {
        return getText();
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file)
    {
        PsiElement element = PsiUtilBase.getElementAtCaret(editor);
        if (element == null) {
            return false;
        }

        PsiElement method = element.getParent();
        if (method == null) {
            return false;
        }

        Boolean fqnMatches = Util.fqnMatches(method, SEARCHSERVICE_FQN);
        Boolean methodMatches = Util.methodMatches(method, SEARCHSERVICE_METHOD);

        return fqnMatches && methodMatches;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException
    {
        PsiElement element = PsiUtilBase.getElementAtCaret(editor);
        if (element == null) {
            return;
        }

        Method method = PsiTreeUtil.getParentOfType(element, Method.class);
        if (method == null) {
            return;
        }
        String methodFqn = method.getFQN();
        String queryCode = "";

        QueryCommand command = new QueryCommand("ezcode:query", project);
        command.setParameters(methodFqn, queryCode);
        ConsoleService consoleService = new ConsoleService(project, "Executing queryCode.", false);
        consoleService.seteZCommand(command);
        consoleService.queue();
    }

    @NotNull
    @Override
    public String getText()
    {
        return "Run eZ query";
    }
}
