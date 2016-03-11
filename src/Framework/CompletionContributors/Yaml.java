package Framework.CompletionContributors;

import Completions.Yaml.Matchers.Sibling;
import Completions.Yaml.Providers.*;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import org.jetbrains.yaml.psi.YAMLKeyValue;

public class Yaml extends CompletionContributor
{
    public Yaml()
    {
        PsiElementPattern.Capture<PsiElement> templateElementPattern = getViewElementMatcher("template");
        extend(CompletionType.BASIC, templateElementPattern, new Templates());

        PsiElementPattern.Capture<PsiElement> controllerElementPattern = getViewElementMatcher("controller");
        extend(CompletionType.BASIC, controllerElementPattern, new Controller());

        PsiElementPattern.Capture<PsiElement> matchElementPattern = getViewElementMatcher("match");
        extend(CompletionType.BASIC, matchElementPattern, new MatchTypes());

        PsiElementPattern.Capture<PsiElement> keyNamesMatcher = PlatformPatterns.psiElement().with(new Completions.Yaml.Matchers.KeyNames());
        extend(CompletionType.BASIC, keyNamesMatcher, new KeyNames());

        PsiElementPattern.Capture<PsiElement> valueMatcher = PlatformPatterns.psiElement().with(new Completions.Yaml.Matchers.MatchValues());
        extend(CompletionType.BASIC, valueMatcher, new MatchValues());
    }

    protected PsiElementPattern.Capture<PsiElement> getViewElementMatcher(String name)
    {
        return PlatformPatterns
                .psiElement()
                .withParent(PlatformPatterns
                    .psiElement(YAMLKeyValue.class)
                    .withName(name)
                )
                .with(new Sibling());
    }
}
