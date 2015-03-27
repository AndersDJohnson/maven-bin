package me.andrz.maven.executable.aether

import org.apache.maven.model.Repository
import org.apache.maven.project.MavenProject
import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.RepositorySystemSession
import org.eclipse.aether.artifact.Artifact
import org.eclipse.aether.collection.CollectRequest
import org.eclipse.aether.graph.Dependency
import org.eclipse.aether.graph.DependencyFilter
import org.eclipse.aether.repository.ProxySelector
import org.eclipse.aether.repository.RemoteRepository
import org.eclipse.aether.resolution.ArtifactResult
import org.eclipse.aether.resolution.DependencyRequest
import org.eclipse.aether.resolution.DependencyResult
import org.eclipse.aether.util.artifact.JavaScopes
import org.eclipse.aether.util.filter.DependencyFilterUtils

/**
 *
 */
class Resolver {

    Booter booter

    public List<Artifact> resolvesWithProject(MavenProject project, Artifact artifact, String scope = null) {
        return resolves(null, artifact, scope)
    }

    public List<Artifact> resolves(Artifact artifact, String scope = null) {
        return resolves(null, artifact, scope)
    }

    public List<Artifact> resolves(List<RemoteRepository> repositories, Artifact artifact, String scope = '') {

        if (! booter) {
            booter = new Booter()
        }

        RepositorySystem system = booter.newRepositorySystem();
        RepositorySystemSession session = booter.newRepositorySystemSession(system);

        if (! repositories) {
            repositories = booter.newRepositories(system, session)
        }

        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(new Dependency(artifact, scope));
        collectRequest.setRepositories(repositories);

        DependencyFilter classpathFilter = DependencyFilterUtils.classpathFilter(
                JavaScopes.COMPILE,
                JavaScopes.PROVIDED,
                JavaScopes.SYSTEM,
                JavaScopes.RUNTIME,
        );
        DependencyRequest dependencyRequest = new DependencyRequest(collectRequest, classpathFilter);

        DependencyResult dependencyResult = system.resolveDependencies(session, dependencyRequest)

        List<ArtifactResult> artifactResults = dependencyResult.getArtifactResults()

        List<Artifact> artifacts = []

        for (ArtifactResult artifactResult : artifactResults) {
            artifacts.add(artifactResult.artifact)
        }

        return artifacts
    }

}
