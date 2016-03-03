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

        String tag = "@ContentType";
        String currentTagValue = partList.get(1);
        if (partList.size() >= 4 && currentTagValue.equals(tag)) {
            context.put("contentTypeCompletion", true);
            return true;
        }

        String currentText = currentTagValue.replace(Util.INTELLIJ_RULES, "");
        if (tag.startsWith(currentText)) {
            context.put("simpleCompletion", "ContentType");
            return true;
        }

        return false;
    }
}
