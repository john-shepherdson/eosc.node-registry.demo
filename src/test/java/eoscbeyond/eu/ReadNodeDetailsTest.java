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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReadNodeDetailsTest {

    private static final String TEST_CSV_CONTENT = 
            "1,Test Node,http://example.com/logo.png,PID123,[Example Entity;http://example.com/ror],http://example.com/node,[Compute;http://example.com/cap;v1]\n" +
            "2,Another Node,http://example.com/logo2.png,PID456,[Another Entity;http://example.com/ror2],http://example.com/node2,[Storage;http://example.com/storage;v2]";

    private Path tempCsvFile;

    @BeforeEach
    void setUp(@TempDir Path tempDir) throws IOException {
        tempCsvFile = tempDir.resolve("nodes.csv");
        Files.write(tempCsvFile, TEST_CSV_CONTENT.getBytes());
    }

    @Test
    void testConstructor_WithValidCSV() throws URISyntaxException, IOException {
        ReadNodeDetails reader = new ReadNodeDetails(tempCsvFile.toString());
        List<EoscNode> nodes = reader.getNodes();

        assertNotNull(nodes, "Node list should not be null");
        assertEquals(2, nodes.size(), "Should parse 2 nodes from CSV");
    }

    @Test
    void testConstructor_WithInvalidCSV() throws URISyntaxException, IOException {
        // Create an empty CSV file
        Files.write(tempCsvFile, new byte[0]);

        ReadNodeDetails reader = new ReadNodeDetails(tempCsvFile.toString());
        List<EoscNode> nodes = reader.getNodes();

        assertNotNull(nodes);
        assertEquals(0, nodes.size(), "Should return an empty list for an empty file");
    }

    @Test
    void testReadNodesFromCSV_ValidFile() throws URISyntaxException, IOException {
        ReadNodeDetails reader = new ReadNodeDetails(tempCsvFile.toString());
        List<EoscNode> nodes = reader.getNodes();

        assertNotNull(nodes);
        assertEquals(2, nodes.size());
        assertEquals("1", nodes.get(0).getId());
        assertEquals("Test Node", nodes.get(0).getName());
        assertEquals("2", nodes.get(1).getId());
        assertEquals("Another Node", nodes.get(1).getName());
    }

    @Test
    void testReadNodesFromCSV_InvalidLine() throws URISyntaxException, IOException {
        String invalidContent = "Invalid line without enough fields\n";
        Files.write(tempCsvFile, invalidContent.getBytes());

        ReadNodeDetails reader = new ReadNodeDetails(tempCsvFile.toString());
        List<EoscNode> nodes = reader.getNodes();

        assertNotNull(nodes);
        assertTrue(nodes.isEmpty(), "Should return an empty list when CSV data is invalid");
    }

    @Test
    void testGetResourceFileAsString_FileNotFound() throws IOException {
        String result = ReadNodeDetails.getResourceFileAsString("nonexistent.csv");
        assertNull(result, "Should return null when the file is not found");
    }
}