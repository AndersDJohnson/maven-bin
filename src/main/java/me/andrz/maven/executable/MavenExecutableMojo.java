package me.andrz.maven.executable;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Echos an object string to the output screen.
 * @goal echo
 * @requiresProject false
 */
@Mojo( name = "executable" )
class MavenExecutableMojo extends AbstractMojo {

    /**
     * The enclosing project.
     */
    @Parameter( defaultValue = "${project}", readonly = true )
    protected MavenProject project;

    public void execute()
            throws MojoExecutionException, MojoFailureException
    {
        getLog().info( "TEST LOG" );
    }

}
