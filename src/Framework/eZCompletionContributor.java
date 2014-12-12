package Framework;

import com.intellij.codeInsight.completion.*;
import com.intellij.patterns.PlatformPatterns;

public class eZCompletionContributor extends CompletionContributor
{
    public eZCompletionContributor()
    {
        eZCompletionProvider provider = new eZCompletionProvider();
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(), provider);
    }
}