package TypeProviders.Abstract;

import Framework.CompletionPreloader;
import com.intellij.openapi.project.DumbService;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider2;
import org.jetbrains.annotations.Nullable;

public abstract class DumbAwareTypeProvider implements PhpTypeProvider2
{
    public String typeSeparator()
    {
        return "#" + getKey();
    }

    @Nullable
    @Override
    public String getType(PsiElement psiElement)
    {
        // Lets wait until the index is ready.
        if (DumbService.isDumb(psiElement.getProject())) {
            return null;
        }

        // Do not attempt to resolve types if plugin is disabled.
        if (Settings.Service.getInstance(psiElement.getProject()).getDisabled()) {
            return null;
        }

        CompletionPreloader preloader = CompletionPreloader.getInstance(psiElement.getProject());
        if (preloader.getCurrentCompletions() == null) {
            return null;
        }

        return resolveType(psiElement);
    }

    public abstract String resolveType(PsiElement psiElement);
}
