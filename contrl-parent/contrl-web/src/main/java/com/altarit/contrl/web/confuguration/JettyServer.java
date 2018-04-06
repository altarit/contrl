package com.altarit.contrl.web.confuguration;

import com.altarit.contrl.client.utils.AppEnv;
//import com.altarit.contrl.web.controllers.ws.JettySocketHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.IOException;

public class JettyServer {

    public static final Logger log = LoggerFactory.getLogger(JettyServer.class);

    private static final int DEFAULT_PORT = 8070;
    private static final String CONTEXT_PATH = "/";
    private static final String CONFIG_LOCATION = "com.altarit.contrl.web.confuguration";
    private static final String MAPPING_URL = "/*";
    private static final String DEFAULT_PROFILE = "dev";

    public static void main(String[] args) throws Exception {
        AppEnv.instance().put("args", args);
        new JettyServer().startJetty(getPortFromArgs(args));
    }

    private static int getPortFromArgs(String[] args) {
        if (args.length > 0) {
            try {
                return Integer.valueOf(args[0]);
            } catch (NumberFormatException ignore) {
            }
        }
        return DEFAULT_PORT;
    }

    private void startJetty(int port) throws Exception {
        Server server = new Server(port);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] {/*new JettySocketHandler(),*/ getServletContextHandler(getContext())});

        //server.setHandler(getServletContextHandler(getContext()));
        server.setHandler(handlers);
        server.start();
        server.join();
    }

    private static ServletContextHandler getServletContextHandler(WebApplicationContext context) throws IOException {
        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setErrorHandler(null);
        contextHandler.setContextPath(CONTEXT_PATH);
        contextHandler.addServlet(new ServletHolder(new DispatcherServlet(context)), MAPPING_URL);
        contextHandler.addEventListener(new ContextLoaderListener(context));
        contextHandler.setResourceBase(new ClassPathResource("webapp").getURI().toString());

        //HandlerList handlers = new HandlerList();
        // first element  is webSocket handler, second element is first handler
        //handlers.addHandler(new Handler[] {new JettySocketHandler(), resource_handler});
        //contextHandler.setHandler(new JettySocketHandler());

        return contextHandler;
    }

    private static WebApplicationContext getContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(CONFIG_LOCATION);
        context.getEnvironment().setDefaultProfiles(DEFAULT_PROFILE);
        return context;
    }

}
