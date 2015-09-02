package me.andrz.maven.bin.aether

import groovy.util.logging.Slf4j
import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.RepositorySystemSession
import org.eclipse.aether.artifact.Artifact
import org.eclipse.aether.collection.CollectRequest
import org.eclipse.aether.collection.CollectResult
import org.eclipse.aether.graph.Dependency
import org.eclipse.aether.graph.DependencyFilter
import org.eclipse.aether.graph.DependencyNode
import org.eclipse.aether.repository.RemoteRepository
import org.eclipse.aether.resolution.ArtifactRequest
import org.eclipse.aether.resolution.ArtifactResolutionException
import org.eclipse.aether.resolution.ArtifactResult
import org.eclipse.aether.resolution.DependencyRequest
import org.eclipse.aether.resolution.DependencyResult
import org.eclipse.aether.util.artifact.JavaScopes
import org.eclipse.aether.util.filter.DependencyFilterUtils

/**
 *
 */
@Slf4j
class Resolver {

    Booter booter

    public List<Artifact> resolves(List<RemoteRepository> repositories, Artifact artifact, String scope = '') {

        if (! booter) {
            booter = new Booter()
        }

        RepositorySystem system = booter.newRepositorySystem();
        RepositorySystemSession session = booter.newRepositorySystemSession(system);

        if (! repositories) {
            repositories = booter.newRepositories(session)
        }

        // First, check artifact exists by resolving - this has better exceptions than
        //  the dependency resolution's NullPointerException when artifact doesn't exist.
        ArtifactRequest artifactRequest = new ArtifactRequest(artifact, repositories, null)
        ArtifactResult artifactResult
        try {
            //noinspection GroovyUnusedAssignment
            artifactResult = system.resolveArtifact(session, artifactRequest)
        }
        catch (ArtifactResolutionException e) {
            throw new MavenBinArtifactResolutionException("Exception resolving artifact with request: " + artifactRequest, e)
        }

        CollectRequest collectRequest = new CollectRequest();
        Dependency rootDependency = new Dependency(artifact, scope);
        collectRequest.setRoot(rootDependency);
        collectRequest.setRepositories(repositories);

        CollectResult collectResult = system.collectDependencies(session, collectRequest)
        DependencyNode dependencyNode = collectResult.getRoot();

        DependencyFilter classpathFilter = DependencyFilterUtils.classpathFilter(
                JavaScopes.COMPILE,
                JavaScopes.PROVIDED,
                JavaScopes.SYSTEM,
                JavaScopes.RUNTIME,
        );
        DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, classpathFilter);
        dependencyRequest.setRoot(dependencyNode);

        DependencyResult dependencyResult = system.resolveDependencies(session, dependencyRequest)

        List<ArtifactResult> dependencyArtifactResults = dependencyResult.getArtifactResults()

        List<Artifact> artifacts = []

        for (ArtifactResult dependencyArtifactResult : dependencyArtifactResults) {
            artifacts.add(dependencyArtifactResult.artifact)
        }

        return artifacts
    }

}
