package me.andrz.maven.bin

import groovy.util.logging.Slf4j

/**
 *
 */
@Slf4j
public class MavenBinCli {

	public static void main(String[] args) {
        process(args)
    }

    public static StdIo process(String[] args) {

        def usage = 'mvbn [options] [artifact] [args...]'
        def cli = new CliBuilder(usage: usage)

        cli.h(longOpt: 'help', 'Show help.')
        cli.i(longOpt: 'install', 'Install artifact.')
        cli.a(longOpt: 'alias', 'Alias for installed artifact.', args: 1)
        cli.c(longOpt: 'getclasspath', 'Get classpath for artifact.')

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

        Boolean getClasspath = cliOptions.getclasspath
        Boolean shouldInstall = !getClasspath && cliOptions.install
        // run if not install
        Boolean shouldRun = ! shouldInstall

        String alias = cliOptions.alias ? cliOptions.alias : null

        MavenBinParams params = [
                arguments: arguments,
                install: shouldInstall,
                getClasspath: getClasspath,
                run: shouldRun,
                alias: alias
        ]

        StdIo stdIo = run(artifact, params)

        println stdIo.out.toString()
        System.err.println stdIo.err.toString()

        def exitValue = stdIo.exitValue

        if (exitValue > 0) {
            throw new MavenBinExitValueNonZeroException(exitValue)
        }

        return stdIo
    }

    public static StdIo run(String coords, MavenBinParams params = null) {

        MavenBin mavenBin = new MavenBin()

        StdIo stdIo = mavenBin.run(coords, params)

        return stdIo
    }

}
