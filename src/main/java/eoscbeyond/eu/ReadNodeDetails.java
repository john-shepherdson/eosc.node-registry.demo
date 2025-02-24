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
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

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
    private ArrayList<EoscNode> nodes = new ArrayList<EoscNode>();

    /**
     * Parameterised constructor for LegalEntity.
     * 
     * @param filePath path to CSV file containing Node details
     */
    public ReadNodeDetails(String filePath) {
        System.out.println("Reading CSV file: " + filePath);
        this.nodes = this.readNodesFromCSV(filePath);
    }

    /**
     * Retrieves the list of nodes.
     *
     * @return a list of {@code EoscNode} objects
     */
    public ArrayList<EoscNode> getNodes() {
        return nodes;
    }

    /**
     * Reads node data from a CSV file and converts it into a list of
     * {@code EoscNode} objects.
     *
     * @param filePath the path to the CSV file containing node details
     * @return a list of {@code EoscNode} objects parsed from the file
     */
    public ArrayList<EoscNode> readNodesFromCSV(String filePath) {
        ArrayList<EoscNode> nodes = new ArrayList<>();
        
         try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 7) {
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
                        ArrayList<EoscCapability> capabilityList = readCapabilitiesFromString(values[6].trim());
                        // Instantiate an EoscNode using values collected above
                        EoscNode eoscNode = new EoscNode(id, name, logo, pid, legalEntity, nodeEndpoint,
                                capabilityList);
                        // add node to list of nodes
                        nodes.add(eoscNode);
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping invalid entry due to number format error.");
                    } catch (URISyntaxException u) {
                        System.err.println("Skipping invalid URI format: " + u);
                    }
                } else {
                    System.out.println("Node values not available. Line length = " + values.length);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return nodes;
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
            System.out.println("Legal Entity values not available. Line length = " + parts.length);
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
    public static ArrayList<EoscCapability> readCapabilitiesFromString(String input)
            throws IOException, URISyntaxException {
        ArrayList<EoscCapability> capabilities = new ArrayList<>();
        String[] blocks = input.split("\\];\\[");

        for (String block : blocks) {
            block = block.replaceAll("[\\[\\]]", ""); // Remove brackets
            String[] parts = block.split(";");
            if (parts.length == 3) {
                capabilities.add(new EoscCapability(parts[0], new URI(parts[1]), parts[2]));
            } else {
                System.out.println("Capability values not available. Length = " + parts.length);
            }
        }
        return capabilities;
    }
}
