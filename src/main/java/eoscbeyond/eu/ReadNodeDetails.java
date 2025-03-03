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
 * ID;Name;LogoURI;PID;[LegalEntityName, ROR_URI];NodeEndpoint;
 * [[Capability1, EndpointURI1, Version1],[Capability2, EndpointURI2, Version2]]
 * </pre>
 *
 * <p>
 * Example:
 * </p>
 *
 * <pre>
 * 1;Example Node;http://example.com/logo.png;PID123;[Example Entity,
 * http://example.com/ror];http://example.com/node;
 * [[Compute, http://example.com/cap, v1],
 * [Storage, http://example.com/storage, v2]]
 * </pre>
 *
 * <p>
 * This ensures that the parsing logic works correctly with structured node
 * data.
 * </p>
 *
 */
public class ReadNodeDetails {
    /** Placeholder for list of nodes. */
    private List<EoscNode> nodes = new ArrayList<>();
    /** Logger. */
    private static final Logger LOGGER =
    LogManager.getLogger(ReadNodeDetails.class);
    /** Number of elements per line in Node details file. */
    private static final int ELEMENTS_PER_LINE = 7;
    /** Number of elements in Legal Entity chunk of line. */
    private static final int ELEMENTS_PER_LEGALENTITY = 2;
    /** Number of elements in Capabilities chunk of line. */
    private static final int ELEMENTS_PER_CAPABILITY = 3;
    /** Position in line of Node ID chunk. */
    private static final int NODE_ID_CHUNK = 0;
     /** Position in line of Node name chunk. */
     private static final int NODE_NAME_CHUNK = 1;
    /** Position in line of Legal Entity logo chunk. */
    private static final int LEGALENT_LOGO_CHUNK = 2;
    /** Position in line of Node PID chunk. */
    private static final int NODE_PID_CHUNK = 3;
    /** Position in line of Legal Entity chunk. */
    private static final int LEGALENT_CHUNK = 4;
    /** Position in line of Node Endpoint chunk. */
    private static final int NODE_ENDPOINT_CHUNK = 5;
    /** Position in line of Node Capabilities chunk. */
    private static final int NODE_CAPABILITIES_CHUNK = 6;


    /**
     * Parameterised constructor for LegalEntity.
     *
     * @param filePath path to CSV file containing Node details
     */
    public ReadNodeDetails(final String filePath) throws URISyntaxException,
    IOException {
        LOGGER.info("Reading nodes from CSV file: {} ", filePath);
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
    public List<EoscNode> readNodesFromCSV(final String filePath)
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

        try (BufferedReader br = new BufferedReader(new
        StringReader(fileContents))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == ELEMENTS_PER_LINE) {
                    tempNodesList = parseNodeDetail(values);
                    nodesList.addAll(tempNodesList);
                    LOGGER.info("Node values available");
                } else {
                    LOGGER.info("Node values not available. Line length = {}",
                    values.length);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error reading file: {}", e.getMessage());
        }
        LOGGER.info("Node list size = {}", nodesList.size());
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
    public static LegalEntity readLegalEntityFromString(
        final String legalEntityValues)
            throws URISyntaxException {
        LegalEntity legalEntity = new LegalEntity();
        String cleanLegalEntityValues = legalEntityValues.replaceAll("[\\[\\]]",
        ""); // Remove brackets
        String[] parts = cleanLegalEntityValues.split(";");
        if (parts.length == ELEMENTS_PER_LEGALENTITY) {
            legalEntity.setName(parts[0]);
            legalEntity.setRorId(new URI(parts[1]));
            LOGGER.info("Legal Entity values available.");
        } else {
            LOGGER.info("Legal Entity values not available. Line length = {}",
            parts.length);
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
    public static List<EoscCapability> readCapabilitiesFromString(
        final String input)
            throws URISyntaxException {
        List<EoscCapability> capabilities = new ArrayList<>();
        String[] blocks = input.split("\\];\\[");

        for (String block : blocks) {
            block = block.replaceAll("[\\[\\]]", ""); // Remove brackets
            String[] parts = block.split(";");
            if (parts.length == ELEMENTS_PER_CAPABILITY) {
                capabilities.add(new EoscCapability(parts[0],
                new URI(parts[1]), parts[2]));
            } else {
                LOGGER.info("Capability values not available. Length = {}",
                parts.length);
            }
        }
        LOGGER.info("Number of Capabilities found = {}", capabilities.size());
        return capabilities;
    }

    /**
     * Reads given resource file as a string.
     *
     * @param fileName path to the resource file
     * @return the file's contents
     * @throws IOException if read fails for any reason
     */
    static String getResourceFileAsString(final String fileName)
    throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        try (InputStream is = classLoader.getResourceAsStream(fileName)) {
            if (is == null) {
                throw new IOException("File not found in resources: "
                + fileName);
            }
            try (InputStreamReader isr =
            new InputStreamReader(is, StandardCharsets.UTF_8);
                    BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(
                    System.lineSeparator()));
            }
        }
    }

    /**
     * Parses the contents of a line from the CVS data file.
     *
     * @param values
     * @return nodeList<EoscNode>
     * @throws NumberFormatException
     * @throws URISyntaxException
     */
    public static List<EoscNode> parseNodeDetail(final String[] values)
            throws NumberFormatException, URISyntaxException {
        List<EoscNode> nodesList = new ArrayList<>();
        try {
            // get Node ID
            String id = values[NODE_ID_CHUNK].trim();
            // get Node name
            String name = values[NODE_NAME_CHUNK].trim();
            LOGGER.info("Getting details for {}", name);
            // get Node Legal Entity logo
            URI logo = new URI(values[LEGALENT_LOGO_CHUNK].trim());
            // get Node PID
            String pid = values[NODE_PID_CHUNK].trim();
            // get Node Legal Entity ROR ID
            String legalEntityValues = values[LEGALENT_CHUNK].trim();
            // parse legal Entity details to create LegalEntity
            LegalEntity legalEntity = readLegalEntityFromString(
                legalEntityValues);
            // get Node Endpoint
            URI nodeEndpoint = new URI(values[NODE_ENDPOINT_CHUNK].trim());
            // get Node EoscCapabilities details block
            List<EoscCapability> capabilityList =
            readCapabilitiesFromString(values[NODE_CAPABILITIES_CHUNK].trim());
            // Instantiate an EoscNode using values collected above
            EoscNode eoscNode = new EoscNode(id, name, logo, pid, legalEntity,
            nodeEndpoint,
                    capabilityList);
            // add node to list of nodes
            nodesList.add(eoscNode);
        } catch (NumberFormatException e) {
            LOGGER.error("Skipping invalid entry due to number format error.");
        } catch (URISyntaxException u) {
            LOGGER.error("Skipping invalid URI format: {}", u.toString());
        }
        return nodesList;
    }
}
