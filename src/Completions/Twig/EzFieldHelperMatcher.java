package Completions.Twig;

import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ProcessingContext;
import com.jetbrains.twig.TwigTokenTypes;
import org.jetbrains.annotations.NotNull;

public class EzFieldHelperMatcher extends PatternCondition<PsiElement>
{
    protected static String[] allowedTypes = {
            "ez_field_value",
            "ez_render_field",
            "ez_field_description",
            "ez_field_name",
            "ez_is_field_empty"
    };

    public EzFieldHelperMatcher()
    {
        super("EzFieldHelper");
    }

    @Override
    public boolean accepts(@NotNull PsiElement psiElement, ProcessingContext processingContext)
    {
        // Wrong element, lets end it here.
        LeafPsiElement source;
        try {
            source = (LeafPsiElement)psiElement;
        }
        catch (Exception e) {
            return false;
        }

        try {
            // Cursor in wrong place.
            if (!withinQuotes(source)) {
                return false;
            }

            // Try to find match. What a terrible implementation! :-/
            LeafPsiElement extensionIdentifier;
            for (String identifier : allowedTypes) {
                extensionIdentifier = findPreviousSibling(source, TwigTokenTypes.IDENTIFIER, identifier);
                if (extensionIdentifier != null) {
                    break;
                }
            }
        }
        catch (Exception e) {
            return false;
        }

        PsiComment eZDocBlock = findComment(psiElement);
        if (eZDocBlock == null) {
            return false;
        }

        String[] parts = eZDocBlock.getText().split(" ");
        if (parts.length != 5) {
            return false;
        }
        if (!parts[1].equals("@ContentType")) {
            return false;
        }

        String twigVariable = parts[3];
        // More shitty code.
        if (findPreviousSibling(source, TwigTokenTypes.IDENTIFIER, twigVariable) == null) {
            return false;
        }

        String contentClass = parts[2];
        processingContext.put("contentClassIdentifier", contentClass);

        return true;
    }

    /**
     * Worst implementation ever. #$%#$&#$% Twig-files.
     */
    protected PsiComment findComment(PsiElement psiElement)
    {
        int start = psiElement.getTextOffset() - psiElement.getTextLength();
        PsiFile file = psiElement.getContainingFile();

        PsiElement current;
        while (start > 0) {
            current = file.findElementAt(start);
            try {
                return (PsiComment)current;
            }
            catch (Exception e) {
                start -= current.getTextLength();
            }
        }

        return null;
    }

    protected boolean withinQuotes(LeafPsiElement psiElement) {
        return
            findPreviousSibling(psiElement, null, "'") != null &&
            findNextSibling(psiElement, null, "'") != null;
    }

    protected LeafPsiElement findPreviousSibling(LeafPsiElement source, IElementType elementType, String value)
    {
        if (!withinBounds(source)) {
            return null;
        }

        if (sourceMatches(source, elementType, value)) {
            return source;
        }

        return findPreviousSibling((LeafPsiElement)source.getPrevSibling(), elementType, value);
    }

    protected LeafPsiElement findNextSibling(LeafPsiElement source, IElementType elementType, String value)
    {
        if (!withinBounds(source)) {
            return null;
        }

        if (sourceMatches(source, elementType, value)) {
            return source;
        }

        return findNextSibling((LeafPsiElement)source.getNextSibling(), elementType, value);
    }

    protected boolean withinBounds(LeafPsiElement source)
    {
        if (source == null) {
            return false;
        }

        // Lets not go outside of our little block.
        if (source.getElementType() == TwigTokenTypes.PRINT_BLOCK_START) {
            return false;
        }

        // Lets not go outside of our statement either.
        return source.getElementType() != TwigTokenTypes.STATEMENT_BLOCK_START;
    }

    protected boolean sourceMatches(LeafPsiElement source, IElementType elementType, String value)
    {
        if (source.getElementType() == elementType && value == null) {
            return true;
        }

        if (elementType == null && source.getText().equals(value)) {
            return true;
        }

        return source.getElementType() == elementType && source.getText().equals(value);
    }
}