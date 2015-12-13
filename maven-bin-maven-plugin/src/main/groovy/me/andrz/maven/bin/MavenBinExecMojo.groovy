package me.andrz.maven.bin

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException
import org.apache.maven.plugin.logging.Log
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 *
 */
@Mojo(
        name = "exec",
        requiresProject = false
)
class MavenBinExecMojo extends AbstractMojo {

    /**
     * The enclosing project.
     */
    @Parameter( defaultValue = '${project}', readonly = true, required = false )
    protected MavenProject project;

    @Parameter( property  = "artifact", required = false )
    private String artifact;

    @Parameter( property  = "arguments", required = false )
    private String arguments;


    public void execute()
            throws MojoExecutionException, MojoFailureException
    {
        Log log = getLog()

        MavenBin mavenBin = new MavenBin()

        StdIo stdIo = mavenBin.run(artifact, new MavenBinParams(
                arguments: arguments
        ))

        log.info stdIo.out.toString()
        log.info stdIo.err.toString()
    }

}
