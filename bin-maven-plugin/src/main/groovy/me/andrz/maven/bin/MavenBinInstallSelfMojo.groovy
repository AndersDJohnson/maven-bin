package me.andrz.maven.bin

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugin.MojoFailureException
import org.apache.maven.plugin.logging.Log
import org.apache.maven.plugins.annotations.Mojo

/**
 *
 */
@Mojo(
        name = "installSelf",
        requiresProject = false
)
class MavenBinInstallSelfMojo extends AbstractMojo {

    public void execute()
            throws MojoExecutionException, MojoFailureException
    {
        Log log = getLog()

        MavenBin mavenBin = new MavenBin()

        mavenBin.run('me.andrz.maven:bin-cli', new MavenBinParams(
                install: true,
                run: false,
                alias: 'mvbn'
        ))
    }

}
