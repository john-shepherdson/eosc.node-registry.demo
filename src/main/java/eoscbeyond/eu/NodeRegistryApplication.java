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
import java.util.ArrayList;
import java.util.List;

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
public class NodeRegistryApplication extends SpringBootServletInitializer  {

    
    /** 
     * @param args
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        
        SpringApplication.run(NodeRegistryApplication.class, args);
        String filePath = "src/main/resources/nodes.csv";
    
        List<EoscNode> nodeList = new ArrayList<EoscNode>();
        
        // Initialise the Node Registry - Read node details from CVS file
        ReadNodeDetails readNodeDetails = new ReadNodeDetails(filePath);
        nodeList = readNodeDetails.getNodes(); 
        new NodeRegistry(nodeList);
    }
    
    /** 
     * @param application
     * @return SpringApplicationBuilder
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(NodeRegistry.class);
    }

    
    /** 
     * @return ObjectMapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

}
