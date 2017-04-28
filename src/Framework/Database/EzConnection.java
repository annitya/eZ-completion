package Framework.Database;

import Framework.Entities.DataSource;
import com.intellij.database.dataSource.DataSourceManagerEx;
import com.intellij.openapi.project.Project;

import java.util.List;
import java.util.stream.Collectors;

public class EzConnection
{
    public static List<DataSource> getDataSources(Project project)
    {
        return DataSourceManagerEx
                .getInstanceEx(project)
                .getDataSources()
                .stream()
                .map(DataSource::new)
                .collect(Collectors.toList());
    }
}
