package com.rajorshi.samay.configuration;


import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@PropertySource("classpath:jetty.yml")
public class JettyConfiguration {

    /* read all the configs and create jettyCustomizer based on them
       add these customizer to JettyConfigur
       ideally create seperate customizer
       Out of the box
       Note : Customizers will be applied in the order of addition to the list
       author : ankita.d
      */


    @Value("${server.requestLog.print:false}")
    private boolean printRequestLog;

    @Value("${server.requestLog.fileName:}")
    private String filename;

    @Bean(name = "org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer")
    public JettyEmbeddedServletContainerCustomizer jettyCustomizer()
    {
        List<JettyServerCustomizer> jettyServerCustomizers = new ArrayList<>();
        //jettyServerCustomizers.add(getLogCustomizer());
        return new JettyEmbeddedServletContainerCustomizer(jettyServerCustomizers);
    }

    private JettyServerCustomizer getLogCustomizer()
    {
        return new JettyServerCustomizer() {
            @Override
            public void customize(Server server) {
                if(printRequestLog)
                {
                    NCSARequestLog requestLog;
                    if(filename!=null)
                        requestLog = new NCSARequestLog(filename);
                    else
                        requestLog = new NCSARequestLog();
                    requestLog.setAppend(true);
                    requestLog.setExtended(false);
                    requestLog.setLogTimeZone("GMT");
                    requestLog.setLogLatency(true);
                    requestLog.setRetainDays(1);
                    server.setRequestLog(requestLog);
                }
            }
        };
    }

}
