package me.andrz.maven.executable

import groovy.util.logging.Slf4j

/**
 *
 */
@Slf4j
public class MavenExecutableCli {

	public static void main(String[] args) {

        def cli = new CliBuilder()

        def cliOptions = cli.parse(args)
        log.debug "args: $cliOptions"

        def cliArgs = cliOptions.arguments()
        log.debug "args: $cliArgs"

        def artifact = cliArgs[0]
        def arguments = cliArgs.drop(1).join(' ')

        log.debug "artifact: $artifact"
        log.debug "arguments: $arguments"

        run(artifact, arguments)
	}

    public static void run(String artifact, String arguments = null) {

        MavenExecutable mavenExecutable = new MavenExecutable()

        def out = new StringBuilder()
        def err = new StringBuilder()
        Process proc
        proc = mavenExecutable.run(artifact, new MavenExecutableParams(
                arguments: arguments
        ))

        proc.waitForProcessOutput(out, err)

        println out
        System.err.println err

        def exitValue = proc.exitValue()

        if (exitValue > 0) {
            System.exit(exitValue)
        }
    }

}
