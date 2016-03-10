package Framework;

import Completions.Yaml.*;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import org.jetbrains.yaml.psi.YAMLKeyValue;

public class YamlCompletionContributor extends CompletionContributor
{
    public YamlCompletionContributor()
    {
        PsiElementPattern.Capture<PsiElement> templateElementPattern = getViewElementMatcher("template");
        extend(CompletionType.BASIC, templateElementPattern, new TemplateCompletionProvider());

        PsiElementPattern.Capture<PsiElement> controllerElementPattern = getViewElementMatcher("controller");
        extend(CompletionType.BASIC, controllerElementPattern, new ControllerCompletionProvider());

        PsiElementPattern.Capture<PsiElement> matchElementPattern = getViewElementMatcher("match");
        extend(CompletionType.BASIC, matchElementPattern, new MatchTypesCompletionProvider());

        PsiElementPattern.Capture<PsiElement> keyNamesMatcher = PlatformPatterns.psiElement().with(new KeyNamesMatcher());
        extend(CompletionType.BASIC, keyNamesMatcher, new KeyNamesCompletionProvider());
    }

    protected PsiElementPattern.Capture<PsiElement> getViewElementMatcher(String name)
    {
        return PlatformPatterns
                .psiElement()
                .withParent(PlatformPatterns
                    .psiElement(YAMLKeyValue.class)
                    .withName(name)
                )
                .with(new SiblingMatcher());
    }
}
