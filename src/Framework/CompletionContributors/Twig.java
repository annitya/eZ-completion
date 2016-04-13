package Framework.CompletionContributors;

import Completions.Twig.EzDocMatcher;
import Completions.Twig.TwigCompletionProvider;
import Completions.Twig.EzFieldHelperMatcher;
import Completions.Twig.YamlContentTypeMatcher;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;

public class Twig extends CompletionContributor
{
    public Twig()
    {
        PsiElementPattern.Capture<PsiElement> eZFieldHelperMatcher = PlatformPatterns.psiElement().andOr(
            PlatformPatterns.psiElement().with(new EzFieldHelperMatcher()),
            PlatformPatterns.psiElement().with(new YamlContentTypeMatcher())
        );
        extend(CompletionType.BASIC, eZFieldHelperMatcher, new TwigCompletionProvider());

        PsiElementPattern.Capture<PsiElement> ezDocMatcher = PlatformPatterns.psiElement().with(new EzDocMatcher());
        extend(CompletionType.BASIC, ezDocMatcher, new TwigCompletionProvider());
    }
}
