/*
 * Copyright © 2025 EOSC Beyond (${email})
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eoscbeyond.eu;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import eoscbeyond.eu.data.Configuration;

@EnableConfigurationProperties(Configuration.class)
@SpringBootApplication

/**
 * <p
 * The {@code NodeRegistryApplication} class is the main entry point for the
 * EOSC Node Registry application.
 * </p>
 * <p>
 * It reads node details from a CSV file, creates a list of {@code EoscNode}
 * instances, and initialises the {@code NodeRegistry} with the list of nodes.
 * </p>
 * <p>
 * <strong>Note:</strong> The application is configured to run as a Spring Boot
 * application.
 * </p>
 * 
 * @author John Shepherdson
 */
public class NodeRegistryApplication extends SpringBootServletInitializer  {
    /** Logger. */
    private static final Logger LOGGER =
    LogManager.getLogger(ReadNodeDetails.class);

    /**
     * Main entry point for the EOSC Node Registry application.
     * 
     * @param args Arguments
     * @throws URISyntaxException If the URI is invalid
     * @throws IOException If an I/O error occurs
     */
    public static void main(final String[] args) throws URISyntaxException,
    IOException {
        SpringApplication.run(NodeRegistryApplication.class, args);
        String filePath = "nodes.csv";

        // Initialise the Node Registry - Read node details from CVS file
        ReadNodeDetails readNodeDetails = new ReadNodeDetails(filePath);
        List<EoscNode> nodeList = readNodeDetails.getNodes();

        // Create and initialise the sole NodeRegistry instance.
        NodeRegistry nodeRegistry = NodeRegistry.getInstance();
        nodeRegistry.setNodes(nodeList);
        LOGGER.info("NodeRegistry instance initialised.");
    }

    /**
     * Configure the application.
     * @param application SpringApplicationBuilder
     * @return SpringApplicationBuilder
     */
    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder
    application) {
        return application.sources(NodeRegistry.class);
    }

    /**
     * Create an ObjectMapper bean.
     * @return ObjectMapper bean
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
