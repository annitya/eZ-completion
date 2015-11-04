package TypeProviders.Abstract;

import Framework.CompletionPreloader;
import com.intellij.openapi.project.DumbService;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.PhpTypedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider2;
import org.jetbrains.annotations.Nullable;

public abstract class DumbAwareTypeProvider implements PhpTypeProvider2
{
    public final static char TYPE_IDENTIFIER = 'Z';

    @Override
    public char getKey()
    {
        return TYPE_IDENTIFIER;
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

    protected String getClassname(PsiElement psiElement)
    {
        PhpTypedElement typedElement;
        try {
            typedElement = (PhpTypedElement)psiElement;
        } catch (Exception e) {
            return null;
        }
        Object[] types = typedElement.getType().getTypes().toArray();
        if (types.length == 0) {
            return null;
        }

        String className = types[0].toString().replace("#Z", "");
        CompletionPreloader preloader = CompletionPreloader.getInstance(psiElement.getProject());
        if (!preloader.getCurrentCompletions().contentClassExists(className)) {
            return null;
        }

        return className;
    }
}
