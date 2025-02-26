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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The {@code ReadNodeDetails} class is responsible for reading and parsing node
 * details
 * from a CSV file and converting them into a list of {@code EoscNode} objects.
 * <p>
 * It provides functionality to:
 * <ul>
 * <li>Read node details from a CSV file</li>
 * <li>Convert CSV data into {@code EoscNode} objects</li>
 * <li>Parse legal entity information</li>
 * <li>Parse capabilities associated with each node</li>
 * </ul>
 * This class is useful for dynamically loading node information from external
 * data sources.
 * </p>
 * 
 * <p>
 * <strong>CSV Format Assumption:</strong>
 * </p>
 * The CSV file is expected to have the following format (semicolon-separated):
 * 
 * <pre>
 * ID;Name;LogoURI;PID;[LegalEntityName, ROR_URI];NodeEndpoint;[[Capability1, EndpointURI1, Version1],[Capability2, EndpointURI2, Version2]]
 * </pre>
 * 
 * <p>
 * Example:
 * </p>
 * 
 * <pre>
 * 1;Example Node;http://example.com/logo.png;PID123;[Example Entity, http://example.com/ror];http://example.com/node;[[Compute, http://example.com/cap, v1],[Storage, http://example.com/storage, v2]]
 * </pre>
 * 
 * <p>
 * This ensures that the parsing logic works correctly with structured node
 * data.
 * </p>
 * 
 * @author John Shepherdson
 * @version 1.0
 */
public class ReadNodeDetails {
    private List<EoscNode> nodes = new ArrayList<EoscNode>();
    private static final Logger logger = LogManager.getLogger(ReadNodeDetails.class);

    /**
     * Parameterised constructor for LegalEntity.
     * 
     * @param filePath path to CSV file containing Node details
     */
    public ReadNodeDetails(String filePath) throws URISyntaxException, IOException {
        logger.info("Reading nodes from CSV file: {} ", filePath);
        this.nodes = this.readNodesFromCSV(filePath);
    }

    /**
     * Retrieves the list of nodes.
     *
     * @return a list of {@code EoscNode} objects
     */
    public List<EoscNode> getNodes() {
        return nodes;
    }

    /**
     * Reads node data from a CSV file and converts it into a list of
     * {@code EoscNode} objects.
     *
     * @param filePath the path to the CSV file containing node details
     * @return a list of {@code EoscNode} objects parsed from the file
     */
    public List<EoscNode> readNodesFromCSV(String filePath)
            throws URISyntaxException, IOException {
        List<EoscNode> nodesList = new ArrayList<>();
        List<EoscNode> tempNodesList = new ArrayList<>();

        String fileContents = getResourceFileAsString(filePath);
        Path path = Path.of(filePath);
        if (Files.exists(path)) {
            fileContents = Files.readString(path, StandardCharsets.UTF_8);
        } else {
            fileContents = getResourceFileAsString(filePath);
        }

        if (fileContents == null) {
            throw new IOException("File not found: " + filePath);
        }

        try (BufferedReader br = new BufferedReader(new StringReader(fileContents))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 7) {
                    tempNodesList = parseNodeDetail(values);
                    nodesList.addAll(tempNodesList);
                } else {
                    logger.info("Node values not available. Line length = {}", values.length);
                }
            }
        } catch (IOException e) {
            logger.error("Error reading file: {}", e.getMessage());
        }
        return nodesList;
    }

    /**
     * Parses a string containing legal entity data and returns a
     * {@code LegalEntity} object.
     *
     * @param legalEntityValues the string containing legal entity details
     * @return a {@code LegalEntity} object
     * @throws IOException        if an error occurs while processing the input
     * @throws URISyntaxException if the URI format is invalid
     */
    public static LegalEntity readLegalEntityFromString(String legalEntityValues)
            throws IOException, URISyntaxException {
        LegalEntity legalEntity = new LegalEntity();
        legalEntityValues = legalEntityValues.replaceAll("[\\[\\]]", ""); // Remove brackets
        String[] parts = legalEntityValues.split(";");
        if (parts.length == 2) {
            legalEntity.setName(parts[0]);
            legalEntity.setRorId(new URI(parts[1]));
        } else {
            logger.info("Legal Entity values not available. Line length = {}", parts.length);
        }
        return legalEntity;
    }

    /**
     * Parses a string containing capability details and returns a list of
     * {@code EoscCapability} objects.
     *
     * @param input the string containing capability data
     * @return an {@code ArrayList} of {@code EoscCapability} objects
     * @throws IOException        if an error occurs while processing the input
     * @throws URISyntaxException if a URI format is invalid
     */
    public static List<EoscCapability> readCapabilitiesFromString(String input)
            throws URISyntaxException {
        List<EoscCapability> capabilities = new ArrayList<>();
        String[] blocks = input.split("\\];\\[");

        for (String block : blocks) {
            block = block.replaceAll("[\\[\\]]", ""); // Remove brackets
            String[] parts = block.split(";");
            if (parts.length == 3) {
                capabilities.add(new EoscCapability(parts[0], new URI(parts[1]), parts[2]));
            } else {
                logger.info("Capability values not available. Length = {}", parts.length);
            }
        }
        return capabilities;
    }

    /**
     * Reads given resource file as a string.
     *
     * @param fileName path to the resource file
     * @return the file's contents
     * @throws IOException if read fails for any reason
     */
    static String getResourceFileAsString(String fileName) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        try (InputStream is = classLoader.getResourceAsStream(fileName)) {
            if (is == null)
                return null;
            try (InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                    BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

    /**
     * Parses the contents of a line from the CVS data file
     * 
     * @param values
     * @return nodeList<EoscNode>
     * @throws NumberFormatException
     * @throws URISyntaxException
     */
    public static List<EoscNode> parseNodeDetail(String[] values)
            throws NumberFormatException, URISyntaxException, IOException {
        List<EoscNode> nodesList = new ArrayList<>();
        try {
            // get Node ID
            String id = values[0].trim();
            // get Node name
            String name = values[1].trim();
            // get Node logo
            URI logo = new URI(values[2].trim());
            // get Node PID
            String pid = values[3].trim();
            // get Node Legal Entity details block
            String legalEntityValues = values[4].trim();
            // parse legal Entity details to create LegalEntity
            LegalEntity legalEntity = readLegalEntityFromString(legalEntityValues);
            // get Node
            URI nodeEndpoint = new URI(values[5].trim());
            // get Node EoscCapabilities details block
            List<EoscCapability> capabilityList = readCapabilitiesFromString(values[6].trim());
            // Instantiate an EoscNode using values collected above
            EoscNode eoscNode = new EoscNode(id, name, logo, pid, legalEntity, nodeEndpoint,
                    capabilityList);
            // add node to list of nodes
            nodesList.add(eoscNode);
        } catch (NumberFormatException e) {
            logger.error("Skipping invalid entry due to number format error.");
        } catch (URISyntaxException u) {
            logger.error("Skipping invalid URI format: {}", u.toString());
        }
        return nodesList;
    }
}
