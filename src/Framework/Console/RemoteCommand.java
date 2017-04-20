package Framework.Console;

import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.project.Project;
import com.intellij.remote.RemoteSdkAdditionalData;
import com.intellij.remote.RemoteSdkCredentials;
import com.jetbrains.php.config.commandLine.PhpCommandSettings;
import com.jetbrains.plugins.remotesdk.RemoteSdkUtil;
import com.jetbrains.plugins.remotesdk.transport.SshRemoteSession;

public class RemoteCommand
{
    public static Process createProcess(PhpCommandSettings commandSettings, Project project) throws Exception
    {
        GeneralCommandLine generalCommandLine = commandSettings.createGeneralCommandLine();
        RemoteSdkAdditionalData remoteSdkAdditionalData = (RemoteSdkAdditionalData)commandSettings.getAdditionalData();
        if (remoteSdkAdditionalData == null) {
            throw new Exception("Unable to fetch remote sdk-data!");
        }

        RemoteSdkCredentials remoteSdkCredentials = remoteSdkAdditionalData.getRemoteSdkCredentials(project, false);
        SshRemoteSession session = RemoteSdkUtil.createRemoteSession(project, remoteSdkCredentials);
        return RemoteSdkUtil.createRemoteProcess(session, generalCommandLine, false, false, true);
    }
}
