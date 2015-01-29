package fr.vidal.oss.testing.tomcat8;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.junit.rules.ExternalResource;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URISyntaxException;

import static fr.vidal.oss.testing.tomcat8.Preconditions.checkState;
import static fr.vidal.oss.testing.tomcat8.Throwables.propagate;
import static java.lang.String.format;
import static org.apache.commons.io.FileUtils.copyDirectory;
import static org.apache.commons.io.FileUtils.deleteDirectory;

public abstract class ServletContainerRule extends ExternalResource {

    private Tomcat server;

    public abstract Iterable<WebAppParameter> webappParameters();
    public abstract String contextPath();

    public URI uri() {
        checkState(server != null);
        try {
            return new URI(format("http://localhost:%d/%s/", port(), contextPath()));
        } catch (URISyntaxException e) {
            throw propagate(e);
        }
    }

    public int port() {
        return server.getConnector().getLocalPort();
    }

    protected void before() throws Exception {

        int port = randomAvailablePort();

        server = new Tomcat();
        server.setPort(port);
        server.setBaseDir(webappBaseDir(port));
        Context webappContext = server.addWebapp("/", webAppSourceFolder().getAbsolutePath());

        for (WebAppParameter parameter : webappParameters()) {
            webappContext.addParameter(parameter.name(), parameter.value());
        }

        server.start();
    }

    protected void after() {
        if (server != null) {
            try {
                server.stop();
                server.destroy();
                deleteDirectory(new File(targetBaseDir(port()), "webapps"));
            } catch (Exception e) {
                throw propagate(e);
            }
        }
    }

    private static int randomAvailablePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

    /**
     * Test execution on windows fails because we can't always delete resource files.
     * Lucene indexes aren't always properly closed on windows under certain conditions.
     * Thus, we decided to use a simple workaround : use random folder at each execution,
     * the next "maven clean" will do the cleanup job
     *
     * @return path of webapp output directory copy
     */
    private static String webappBaseDir(int port) {
        try {
            File baseDir = targetBaseDir(port);
            copyDirectory(webAppSourceFolder(), new File(baseDir, "webapps"));
            return baseDir.getPath();
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static File targetBaseDir(int port) {
        return new File(format("target/tomcat.%d", port));
    }

    private static File webAppSourceFolder() throws URISyntaxException {
        File outputTestDirectory = new File(ServletContainerRule.class.getResource("/").toURI());
        return new File(outputTestDirectory, "../../src/main/webapp");
    }

}
