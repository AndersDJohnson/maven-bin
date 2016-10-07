package me.andrz.maven.bin.aether;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import org.apache.maven.plugin.dependency.utils.resolvers.ArtifactsResolver
import org.apache.maven.plugin.dependency.utils.resolvers.DefaultArtifactsResolver;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException
import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.collection.CollectRequest
import org.eclipse.aether.graph.DependencyNode
import org.eclipse.aether.internal.impl.SimpleLocalRepositoryManager
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException
import org.eclipse.aether.util.graph.visitor.PreorderNodeListGenerator;

/**
 * Created by anders on 8/28/16.
 */
public class MavenClasspath {

    public static String getClasspathFromMavenProject(File pom, File local) {

    }

//    public static String getClasspathFromMavenProject(File pom, File local) {
//
//        ArtifactsResolver artifactsResolver =
//                new DefaultArtifactsResolver(null, this.getLocal(), this.remoteRepos, true );
//        def resolvedArtifacts = artifactsResolver.resolve( artifacts, getLog() );
//    }

//    public static String getClasspathFromMavenProject(File pom, File local) {
//        MavenProject project = loadProject(pom)
//        Artifact artifact = projectToArtifact(project)
//
//        RepositorySystem repoSystem = newRepositorySystem();
//
//        RepositorySystemSession session = newSession( repoSystem );
//
//        Dependency dependency =
//                new Dependency(artifact, "compile" );
//        RemoteRepository central = new RemoteRepository.Builder( "central", "default", "http://repo1.maven.org/maven2/" ).build();
//
//        CollectRequest collectRequest = new CollectRequest();
//        collectRequest.setRoot( dependency );
//        collectRequest.addRepository( central );
//        DependencyNode node = repoSystem.collectDependencies( session, collectRequest ).getRoot();
//
//        DependencyRequest dependencyRequest = new DependencyRequest();
//        dependencyRequest.setRoot( node );
//
//        repoSystem.resolveDependencies( session, dependencyRequest  );
//
//        PreorderNodeListGenerator nlg = new PreorderNodeListGenerator();
//        node.accept( nlg );
//        System.out.println( nlg.getClassPath() );
//    }


//    public static String getClasspathFromMavenProject(File pom, File local) {
//        MavenProject project = loadProject(pom)
//        Artifact artifact = projectToArtifact(project)
//        Collection<RemoteRepository> remotes = Arrays.asList(
//                new RemoteRepository(
//                        "maven-central",
//                        "default",
//                        "http://repo1.maven.org/maven2/"
//                )
//        );
//        Collection<Artifact> deps = new Aether(remotes, local).resolve(
//                artifact,
//                "runtime"
//        );
//    }


//    public static String getClasspathFromMavenProject(File projectPom, File localRepoFolder) {
////        Booter booter = new Booter()
//        MavenProject project = loadProject(projectPom)
////        def artifact = project.getArtifact()
//        Artifact artifact = projectToArtifact(project)
////        Resolver resolver = new Resolver()
////        def resolves = resolver.resolves(artifact)
////        println resolves
//
//        Resolver resolver = new Resolver()
//        def resolves = resolver.resolves(artifact)
//        println resolves
//
////        def localRepoManager = new SimpleLocalRepositoryManager(booter.getLocalRepoPath())
////        localRepoManager.
////        def repository = localRepoManager.getRepository()
//////        def file = new File(localRepoManager.getPathForArtifact(artifact, true))
//////        println file
//////        new RemoteRepository(lo)
////        Resolver resolver = new Resolver()
////        def resolves = resolver.resolves([repository], artifact)
////        println resolves
//    }

    public static Artifact projectToArtifact(MavenProject project) {
        Model model = project.model
        return new DefaultArtifact(model.groupId ?: model.parent?.groupId,
                model.artifactId, null, model.version, model.packaging)
    }


//    public static String getClasspathFromMavenProject(File projectPom, File localRepoFolder) {
////        pbConfig=new DefaultProjectBuilderConfiguration().setLocalRepository(localRepo);
//        pbConfig=new DefaultProjectBuilderConfiguration()
//        project = projectBuilder.build( pomFile, pbConfig );
//        aether = new Aether(project, local);
//    }



//    public static String getClasspathFromMavenProject(File projectPom, File localRepoFolder) throws DependencyResolutionException, IOException, XmlPullParserException
//    {
//        MavenProject proj = loadProject(projectPom);
//
//        List<RemoteRepository> repos = Arrays.asList(
////                null
////                new MavenArtifactRepository(
////                        "maven-central", "http://repo1.maven.org/maven2/", new DefaultRepositoryLayout(),
////                        new ArtifactRepositoryPolicy(), new ArtifactRepositoryPolicy()
////                )
//        );
////        proj.setRemoteArtifactRepositories(
////                repos
////        );
//
//        String classpath = "";
////        Aether aether = new Aether(proj, localRepoFolder);
//
//        List<Dependency> dependencies = proj.getDependencies();
//        Iterator<Dependency> it = dependencies.iterator();
//
//        while (it.hasNext()) {
//            org.apache.maven.model.Dependency depend = it.next();
//
//            Resolver resolver = new Resolver();
//            Artifact artifact = new DefaultArtifact(
//                    depend.getGroupId(), depend.getArtifactId(),
//                    depend.getClassifier(), depend.getType(), depend.getVersion());
////
//            List<Artifact> deps = resolver.resolves(repos, artifact);
////            final Collection<Artifact> deps = aether.resolve(
////                    JavaScopes.RUNTIME
////            );
//
//            Iterator<Artifact> artIt = deps.iterator();
//            while (artIt.hasNext()) {
//                Artifact art = artIt.next();
//                classpath = classpath + " " + art.getFile().getAbsolutePath();
//            }
//        }
//
//        return classpath;
//    }

    public static MavenProject loadProject(File pomFile) throws IOException, XmlPullParserException
    {
        MavenProject ret = null;
        MavenXpp3Reader mavenReader = new MavenXpp3Reader();

        if (pomFile != null && pomFile.exists())
        {
            FileReader reader = null;

            try
            {
                reader = new FileReader(pomFile);
                Model model = mavenReader.read(reader);
                model.setPomFile(pomFile);

                ret = new MavenProject(model);
            }
            finally
            {
                reader.close();
            }
        }

        return ret;
    }
}
