package me.andrz.maven.bin

import groovy.util.logging.Slf4j

/**
 *
 */
@Slf4j
public class MavenBinCli {

	public static void main(String[] args) {

        def usage = 'mvbn [options] [artifact] [args...]'
        def cli = new CliBuilder(usage: usage)

        cli.h(longOpt: 'help', 'Show help.')
        cli.i(longOpt: 'install', 'Install artifact.')
        cli.a(longOpt: 'alias', 'Alias for installed artifact.', args: 1)

        def cliOptions = cli.parse(args)
        log.debug "args: $cliOptions"

        def cliArgs = cliOptions.arguments()
        log.debug "args: $cliArgs"

        if (cliOptions.help) {
            cli.usage()
            return
        }

        def artifact = cliArgs[0]
        def arguments = cliArgs.drop(1).join(' ')

        log.debug "artifact: $artifact"
        log.debug "arguments: $arguments"

        Boolean shouldInstall = cliOptions.install
        // run if not install
        Boolean shouldRun = ! shouldInstall

        String alias = cliOptions.alias ? cliOptions.alias : null

        def out = new StringBuilder()
        def err = new StringBuilder()

        MavenBinParams params = [
                arguments: arguments,
                install: shouldInstall,
                run: shouldRun,
                alias: alias
        ]

        Process proc = run(artifact, params)

        if (! proc) {
            return
        }

        proc.waitForProcessOutput(out, err)

        println out
        System.err.println err

        def exitValue = proc.exitValue()

        if (exitValue > 0) {
            System.exit(exitValue)
        }
	}

    public static Process run(String coords, MavenBinParams params = null) {

        MavenBin mavenBin = new MavenBin()

        Process proc = mavenBin.run(coords, params)

        return proc
    }

}
