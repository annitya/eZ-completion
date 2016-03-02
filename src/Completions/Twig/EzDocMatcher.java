package Completions.Twig;

import Framework.Util;
import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class EzDocMatcher extends PatternCondition<PsiElement>
{
    public EzDocMatcher() { super("Twig eZ-Doc"); }

    @Override
    public boolean accepts(@NotNull PsiElement psiElement, ProcessingContext context)
    {
        PsiComment comment;
        try {
            comment = (PsiComment)psiElement;
        }
        catch (Exception e) {
            return false;
        }

        List<String> partList = new ArrayList<>();
        for (String part : comment.getText().split(" ")) {
            if (part.length() == 0) {
                continue;
            }

            if (part.equals(Util.INTELLIJ_RULES)) {
                continue;
            }

            partList.add(part);
        }

        if (partList.size() < 3) {
            return false;
        }

        String currentText = partList.get(1).replace(Util.INTELLIJ_RULES, "");
        String tag = "@ContentType";
        if (!currentText.equals(tag) && tag.contains(currentText)) {
            context.put("simpleCompletion", "ContentType");
            return true;
        }

        // Variable name is missing
        if (partList.size() < 4) {
            return false;
        }

        context.put("contentTypeCompletion", true);
        return true;
    }
}
