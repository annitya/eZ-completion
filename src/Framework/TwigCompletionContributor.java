package Framework;

import Completions.Twig.EzDocMatcher;
import Completions.Twig.TwigCompletionProvider;
import Completions.Twig.EzFieldHelperMatcher;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;

public class TwigCompletionContributor extends CompletionContributor
{
    public TwigCompletionContributor()
    {
        PsiElementPattern.Capture<PsiElement> eZFieldHelperMatcher = PlatformPatterns.psiElement().with(new EzFieldHelperMatcher());
        extend(CompletionType.BASIC, eZFieldHelperMatcher, new TwigCompletionProvider());

        PsiElementPattern.Capture<PsiElement> ezDocMatcher = PlatformPatterns.psiElement().with(new EzDocMatcher());
        extend(CompletionType.BASIC, ezDocMatcher, new TwigCompletionProvider());
    }
}
