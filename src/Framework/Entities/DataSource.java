package Framework.Entities;

import com.intellij.database.dataSource.LocalDataSource;

public class DataSource
{
    protected String id;
    protected String name;

    public DataSource(String id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public DataSource(LocalDataSource dataSource)
    {
        id = dataSource.getUniqueId();
        name = dataSource.getName();
    }

    public String getId() { return id; }

    @Override
    public String toString() {
        return name;
    }
}
