package Index;

import com.intellij.util.io.DataExternalizer;
import com.intellij.util.io.EnumeratorStringDescriptor;
import org.jetbrains.annotations.NotNull;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class YamlDataExternalizer implements DataExternalizer<String>
{
    private final EnumeratorStringDescriptor myStringEnumerator = new EnumeratorStringDescriptor();

    public synchronized void save(@NotNull DataOutput out, String value) throws IOException
    {
        this.myStringEnumerator.save(out, value);

    }

    public synchronized String read(@NotNull DataInput in) throws IOException
    {
        return this.myStringEnumerator.read(in);
    }
}
