package com.rajorshi.samay.configuration;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;

import java.util.List;

@AllArgsConstructor
public class JettyEmbeddedServletContainerCustomizer implements EmbeddedServletContainerCustomizer {

    List<JettyServerCustomizer> jettyServerCustomizerList;

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        if(container instanceof JettyEmbeddedServletContainerFactory && jettyServerCustomizerList!=null)
        {
            for(JettyServerCustomizer jettyServerCustomizer: jettyServerCustomizerList)
            {
                ((JettyEmbeddedServletContainerFactory) container).addServerCustomizers(jettyServerCustomizer);
            }

        }
    }
}
