package Completions.Twig;

import Index.YamlContentTypeIndex;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ProcessingContext;
import com.intellij.util.indexing.FileBasedIndex;
import java.util.List;

/**
 * Attempts to match the ContentType by using yaml-configuration.
 */
public class YamlContentTypeMatcher extends EzFieldHelperMatcher
{
    @Override
    protected Boolean matchContentType(PsiElement psiElement, String variableName, ProcessingContext context)
    {
        if (!variableName.equals("content")) {
            return false;
        }

        FileBasedIndex instance = FileBasedIndex.getInstance();

        String canonicalPath = psiElement
                .getContainingFile()
                .getOriginalFile()
                .getVirtualFile()
                .getCanonicalPath();

        if (canonicalPath == null) {
            return false;
        }

        GlobalSearchScope globalSearchScope = GlobalSearchScope.allScope(psiElement.getProject());

        List<String> values = instance.getValues(YamlContentTypeIndex.KEY, canonicalPath, globalSearchScope);
        if (values.size() != 1) {
            return false;
        }

        context.put("contentTypeIdentifier", values.get(0));
        return true;
    }
}
