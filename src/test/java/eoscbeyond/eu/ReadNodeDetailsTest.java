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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReadNodeDetailsTest {
    private File testCsvFile;
    private static final Logger logger = LogManager.getLogger(ReadNodeDetailsTest.class);

    /** 
     * @throws IOException
     */
    @BeforeEach
    void setUp() throws IOException {
        // Create a temporary test file
        testCsvFile = File.createTempFile("test_nodes", ".csv");
        logger.info("Test CSV File Created: " + testCsvFile.getAbsolutePath());
        testCsvFile.deleteOnExit(); // Ensure the file is deleted after test runs

        // Ensure the file is writable
        if (!testCsvFile.exists()) {
            throw new FileNotFoundException("Test CSV file was not created.");
        }

        // Create a sample CSV file for testing
        try (FileWriter writer = new FileWriter(testCsvFile)){
        writer.write("1,Test Node,http://example.com/logo.png,PID123,[Example Entity;http://example.com/ror],http://example.com/node,[Compute;http://example.com/cap;v1];[Storage;http://example.com/storage;v2]\n");
        writer.close();
        logger.info("Test CSV File Written Successfully!");
        }
    }

    
    /** 
     * @throws URISyntaxException
     */
    @Test
    void testReadNodesFromCSV_ValidFile() throws URISyntaxException, IOException {
        ReadNodeDetails reader = new ReadNodeDetails(testCsvFile.getAbsolutePath());
        List<EoscNode> nodes = reader.getNodes();
    
        assertNotNull(nodes, "Node list should not be null");
        assertFalse(nodes.isEmpty(), "Expected at least one node, but got an empty list.");
        assertEquals(1, nodes.size(), "Expected exactly one node in the list");

        // Check the first node's details
        EoscNode node = nodes.get(0);
        assertEquals("1", node.getId());
        assertEquals("Test Node", node.getName());
        assertEquals(URI.create("http://example.com/logo.png"), node.getLogo());
        assertEquals("PID123", node.getPid());
        assertEquals(URI.create("http://example.com/node"), node.getNodeEndpoint());
        
        // Validate Legal Entity
        LegalEntity entity = node.getLegalEntity();
        assertNotNull(entity);
        assertEquals("Example Entity", entity.getName());
        assertEquals(URI.create("http://example.com/ror"), entity.getRorId());

        // Validate Capabilities
        List<EoscCapability> capabilities = node.getCapabilityList();
        assertNotNull(capabilities);
        assertEquals(2, capabilities.size());

        assertEquals("Compute", capabilities.get(0).getCapabilityType());
        assertEquals(URI.create("http://example.com/cap"), capabilities.get(0).getEndpoint());
        assertEquals("v1", capabilities.get(0).getVersion());
    }

    
    /** 
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    void testReadNodesFromCSV_InvalidFormat() throws IOException, URISyntaxException {
        // Create an invalid CSV file (missing fields)
        FileWriter writer = new FileWriter(testCsvFile.getAbsolutePath());
        writer.write("2,Invalid Node,http://invalid.com/logo.png,PID999\n"); // Missing required fields
        writer.close();

        ReadNodeDetails reader = new ReadNodeDetails(testCsvFile.getAbsolutePath());
        List<EoscNode> nodes = reader.getNodes();

        assertTrue(nodes.isEmpty(), "Nodes list should be empty for invalid CSV format.");
    }

    @Test
    void testReadLegalEntityFromString_ValidData() throws URISyntaxException, IOException {
        LegalEntity entity = ReadNodeDetails.readLegalEntityFromString("[Test Entity;http://test.com/ror]");

        assertNotNull(entity);
        assertEquals("Test Entity", entity.getName());
        assertEquals(new URI("http://test.com/ror"), entity.getRorId());
    }

    @Test
    void testReadLegalEntityFromString_InvalidData() throws URISyntaxException, IOException {
        LegalEntity entity = ReadNodeDetails.readLegalEntityFromString("[Invalid Entity]");
        assertNull(entity.getRorId(), "ROR ID should be null for invalid data.");
    }

    @Test
    void testReadCapabilitiesFromString_ValidData() throws URISyntaxException, IOException {
        List<EoscCapability> capabilities = ReadNodeDetails.readCapabilitiesFromString("[AI;http://ai.com;v1];[BigData;http://bigdata.com;v2]");

        assertNotNull(capabilities);
        assertEquals(2, capabilities.size());
        assertEquals("AI", capabilities.get(0).getCapabilityType());
        assertEquals(new URI("http://ai.com"), capabilities.get(0).getEndpoint());
        assertEquals("v1", capabilities.get(0).getVersion());
    }

    @Test
    void testReadCapabilitiesFromString_InvalidData() throws URISyntaxException, IOException {
        List<EoscCapability> capabilities = ReadNodeDetails.readCapabilitiesFromString("[Invalid Capability]");
        assertTrue(capabilities.isEmpty(), "Capabilities list should be empty for invalid data.");
    }


    @Test
    void testEmptyCSVFile() throws IOException, URISyntaxException {
        // Create an empty CSV file
        FileWriter writer = new FileWriter(testCsvFile.getAbsolutePath());
        writer.write("");
        writer.close();

        ReadNodeDetails reader = new ReadNodeDetails(testCsvFile.getAbsolutePath());
        assertTrue(reader.getNodes().isEmpty(), "Nodes list should be empty for an empty CSV file.");
    }
}