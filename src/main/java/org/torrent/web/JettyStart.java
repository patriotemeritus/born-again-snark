package org.torrent.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.embedded.HelloServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlets.MultiPartFilter;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * User: Vlad Vinichenko (akerigan@gmail.com)
 * Date: 15.09.2009
 * Time: 22:45:49
 */
public class JettyStart {

    private static Log log = LogFactory.getLog(JettyStart.class);

    public static void main(String[] args) throws Exception {

        PropertyConfigurator.configure("./app/conf/log4j.properties");

        Server server = new Server(8080);

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        server.setHandler(contexts);

        ServletContextHandler root = new ServletContextHandler(contexts, "/", ServletContextHandler.SESSIONS);

        root.addFilter(new FilterHolder(new MultiPartFilter()), "/*", 0);        

        File velocityProperties = new File("./app/conf/velocity.properties");
        VelocityEngine velocityEngine;
        if (velocityProperties.exists()) {
            Properties properties = new Properties();
            properties.load(new FileInputStream(velocityProperties));
            velocityEngine = new VelocityEngine(properties);
        } else {
            velocityEngine = new VelocityEngine();
        }

        root.addServlet(new ServletHolder(new AddTorrentServlet(velocityEngine)), "/");

        server.start();
        log.info(server.dump());
        server.join();
    }
}
