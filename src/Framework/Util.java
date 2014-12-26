package Framework;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;

public class Util
{
    public static Project currentProject()
    {
        DataContext context = DataManager.getInstance().getDataContextFromFocus().getResultSync();
        return CommonDataKeys.PROJECT.getData(context);
    }
}
