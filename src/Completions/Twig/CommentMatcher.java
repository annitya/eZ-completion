package Completions.Twig;

import com.intellij.psi.*;

public class CommentMatcher extends PsiRecursiveElementWalkingVisitor
{
    protected String variable;
    protected String match;

    public CommentMatcher(String variable)
    {
        this.variable = variable;
    }

    public String getMatch() { return match; }

    @Override
    public void visitComment(PsiComment comment) {
        String[] parts = comment.getText().split(" ");

        if (parts.length != 5) {
            return;
        }
        if (!parts[1].equals("@ContentType")) {
            return;
        }

        String first = parts[2];
        String second = parts[3];
        Boolean firstMatches = first.equals(variable);
        Boolean secondMatches = second.equals(variable);
        if (!firstMatches && !secondMatches) {
            return;
        }

        // Return the one which is not the variable. If they are equal... well then who cares.
        this.match = firstMatches ? second : first;
    }
}
