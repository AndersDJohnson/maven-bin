package me.andrz.maven.executable

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
class MavenExecutableExecMojo extends AbstractMojo {

    /**
     * The enclosing project.
     */
    @Parameter( defaultValue = '${project}', readonly = true )
    protected MavenProject project;

    @Parameter( property  = "artifact" )
    private String artifact;

    @Parameter( property  = "arguments" )
    private String arguments;

    @Parameter( property  = "alias", required = false )
    private String alias;


    public void execute()
            throws MojoExecutionException, MojoFailureException
    {
        Log log = getLog()

        MavenExecutable mavenExecutable = new MavenExecutable()

        def out = new StringBuilder()
        def err = new StringBuilder()
        Process proc
        proc = mavenExecutable.run(artifact, new MavenExecutableParams(
                arguments: arguments,
                alias: alias
        ))

        proc.waitForProcessOutput(out, err)
        log.info out.toString()
        log.info err.toString()
    }

}
