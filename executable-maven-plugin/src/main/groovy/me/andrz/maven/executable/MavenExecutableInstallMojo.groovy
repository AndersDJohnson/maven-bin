package me.andrz.maven.executable

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugin.MojoFailureException
import org.apache.maven.plugin.logging.Log
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter

/**
 *
 */
@Mojo(
        name = "install",
        requiresProject = false
)
class MavenExecutableInstallMojo extends AbstractMojo {

    @Parameter( property  = "artifact" )
    private String artifact;

    @Parameter( property  = "alias", required = false )
    private String alias;


    public void execute()
            throws MojoExecutionException, MojoFailureException
    {
        Log log = getLog()

        MavenExecutable mavenExecutable = new MavenExecutable()

        mavenExecutable.run(artifact, new MavenExecutableParams(
                install: true,
                run: false,
                alias: alias
        ))
    }

}
