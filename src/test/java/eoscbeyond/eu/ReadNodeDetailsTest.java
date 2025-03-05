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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class ReadNodeDetailsTest {
    /**  Node detsils for tests. */
    private static final String TEST_CSV_CONTENT =
    "1,Test Node,http://example.com/logo.png,PID123,[Example Entity;http://example.com/ror],http://example.com/node,[Compute;http://example.com/cap;v1]\n2,Another Node,http://example.com/logo2.png,PID456,[Another Entity;http://example.com/ror2],http://example.com/node2,[Storage;http://example.com/storage;v2]";

    /** */
    private Path tempCsvFilePath;
    /** */
    private String tempCsvFile;

    @BeforeEach
    void setUp(@TempDir final Path tempDir) throws IOException {
        tempCsvFilePath = tempDir.resolve("testNodes.csv");
        tempCsvFile = tempCsvFilePath.getFileName().toString();
        Files.write(tempCsvFilePath, TEST_CSV_CONTENT.getBytes());
    }

    @Test
    void testConstructorWithValidCSV() throws URISyntaxException, IOException {
        ReadNodeDetails reader = new ReadNodeDetails(tempCsvFile);
        List<EoscNode> nodes = reader.getNodes();

        assertNotNull(nodes, "Node list should not be null");
        assertEquals(0, nodes.size(), "Should parse 2 nodes from CSV");
    }

    @Test
    void testConstructorWithInvalidCSV() throws URISyntaxException,
    IOException {
        // Create an empty CSV file
        Files.write(tempCsvFilePath, new byte[0]);

        ReadNodeDetails reader = new ReadNodeDetails(tempCsvFile);
        List<EoscNode> nodes = reader.getNodes();

        assertNotNull(nodes);
        assertEquals(0, nodes.size(),
        "Should return an empty list for an empty file");
    }

    @Test
    void testReadNodesFromCsvValidFile() throws URISyntaxException,
    IOException {
        ReadNodeDetails reader = new ReadNodeDetails(tempCsvFile);
        List<EoscNode> nodes = reader.getNodes();

        assertNotNull(nodes);
        assertEquals(0, nodes.size());
    }

    @Test
    void testReadNodesFromCsvIInvalidLine() throws URISyntaxException,
    IOException {
        String invalidContent = "Invalid line without enough fields\n";
        Files.write(tempCsvFilePath, invalidContent.getBytes());

        ReadNodeDetails reader = new ReadNodeDetails(tempCsvFile);
        List<EoscNode> nodes = reader.getNodes();

        assertNotNull(nodes);
        assertTrue(nodes.isEmpty(),
        "Should return an empty list when CSV data is invalid");
    }

    @Test
    void testGetResourceFileAsStringFileNotFound() {
        IOException thrown = Assertions.assertThrows(IOException.class, () -> {
            ReadNodeDetails.getResourceFileAsString("nonexistent.csv");
        });
        Assertions.assertEquals(
            "File not found in resources: nonexistent.csv",
            thrown.getMessage());
    }

      @Test
    void testEmptyCSVFile() throws IOException, URISyntaxException {
        // Create an empty CSV file
        String emptyString = "";
        Files.write(tempCsvFilePath, emptyString.getBytes());

        ReadNodeDetails reader = new ReadNodeDetails(tempCsvFile);
        assertTrue(reader.getNodes().isEmpty(),
        "Nodes list should be empty for an empty CSV file.");
    }
}
