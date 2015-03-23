package me.andrz.maven.executable.aether

import org.apache.maven.project.MavenProject
import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.RepositorySystemSession
import org.eclipse.aether.artifact.Artifact
import org.eclipse.aether.collection.CollectRequest
import org.eclipse.aether.graph.Dependency
import org.eclipse.aether.graph.DependencyFilter
import org.eclipse.aether.resolution.ArtifactRequest
import org.eclipse.aether.resolution.ArtifactResult
import org.eclipse.aether.resolution.DependencyRequest
import org.eclipse.aether.resolution.DependencyResult
import org.eclipse.aether.util.artifact.JavaScopes
import org.eclipse.aether.util.filter.DependencyFilterUtils

/**
 *
 */
class Resolver {

    public static Artifact resolve(MavenProject project, Artifact artifact) {

        RepositorySystem system = Booter.newRepositorySystem();
        RepositorySystemSession session = Booter.newRepositorySystemSession(system);

        ArtifactRequest artifactRequest = new ArtifactRequest();
        artifactRequest.setArtifact(artifact);
        artifactRequest.setRepositories(project.getRemoteProjectRepositories());

        ArtifactResult artifactResult = system.resolveArtifact(session, artifactRequest);

        artifact = artifactResult.getArtifact();

        return artifact;
    }

    public static List<Artifact> resolves(MavenProject project, Artifact artifact) {
        RepositorySystem system = Booter.newRepositorySystem();
        RepositorySystemSession session = Booter.newRepositorySystemSession(system);

        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(new Dependency(artifact, ""));
        collectRequest.setRepositories(Booter.newRepositories(system, session));

//        CollectResult collectResult = system.collectDependencies(session, collectRequest);
        DependencyFilter classpathFlter = DependencyFilterUtils.classpathFilter( JavaScopes.COMPILE );
        DependencyRequest dependencyRequest = new DependencyRequest( collectRequest, classpathFlter );

//        List<Dependency> dependencies = collectRequest.getDependencies();
        DependencyResult dependencyResult = system.resolveDependencies( session, dependencyRequest )

        List<ArtifactResult> artifactResults = dependencyResult.getArtifactResults()

        List<Artifact> artifacts = []

        for (ArtifactResult artifactResult : artifactResults) {
            artifacts.add(artifactResult.artifact)
        }

        return artifacts
    }

}
