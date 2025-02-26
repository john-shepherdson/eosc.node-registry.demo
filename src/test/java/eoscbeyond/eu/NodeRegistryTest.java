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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@code NodeRegistry} class.
 */
class NodeRegistryTest {
    private EoscNode node1;
    private EoscNode node2;
    private List<EoscNode> nodeList;

    
    /** 
     * @throws URISyntaxException
     */
    @BeforeEach
    void setUp() throws URISyntaxException {
        // Creating sample capabilities
        List<EoscCapability> capabilities1 = new ArrayList<>();
        capabilities1.add(new EoscCapability("Resource Catalogue", new URI("http://example.com/cap1"), "v1"));

        List<EoscCapability> capabilities2 = new ArrayList<>();
        capabilities2.add(new EoscCapability("Identity Management", new URI("http://example.com/cap2"), "v2"));

        // Creating sample legal entity
        LegalEntity legalEntity = new LegalEntity("Example Entity;", new URI("http://example.com/ror"));

        // Creating sample nodes
        node1 = new EoscNode("1", "Node One,", new URI("http://example.com/logo1"), "PID1", legalEntity, new URI("http://example.com/node1"), capabilities1);
        node2 = new EoscNode("2", "Node Two", new URI("http://example.com/logo2"), "PID2", legalEntity, new URI("http://example.com/node2"), capabilities2);

        // Initialising registry
        this.nodeList = new ArrayList<EoscNode>();
        this.nodeList.add(node1);
        this.nodeList.add(node2);
        new NodeRegistry(nodeList);
    }

    /**
     * Tests whether the NodeRegistry correctly initialises with nodes.
     */
    @Test
    void testNodeRegistryInitialisation() {
        assertNotNull(NodeRegistry.getNodes());
        assertEquals(2, NodeRegistry.getNodes().size());
    }

    /**
     * Tests searching for a node by ID.
     */
    @Test
    void testSearchNodeById() {
        assertEquals(node1, NodeRegistry.searchNodeById("1"));
        assertEquals(node2, NodeRegistry.searchNodeById("2"));
        assertNull(NodeRegistry.searchNodeById("999")); // Non-existent ID
    }

     /**
     * Tests searching for a node by ID.
     */
    @Test
    void testSearchNodeSummaryById() {
        assertTrue(NodeRegistry.searchNodeSummaryById("1").contains("http://example.com/node1"));
        assertTrue(NodeRegistry.searchNodeSummaryById("2").contains("http://example.com/node2"));
        assertNull(NodeRegistry.searchNodeSummaryById("999")); // Non-existent ID
    }

    /**
     * Tests searching for nodes by capability.
     */
    @Test
    void testSearchNodesByCapability() {
        List<EoscNode> storageNodes = NodeRegistry.searchNodesByCapability("Resource Catalogue");
        assertEquals(1, storageNodes.size());
        assertEquals(node1, storageNodes.get(0));

        List<EoscNode> computeNodes = NodeRegistry.searchNodesByCapability("Identity Management");
        assertEquals(1, computeNodes.size());
        assertEquals(node2, computeNodes.get(0));

        List<EoscNode> nonExistentNodes = NodeRegistry.searchNodesByCapability("Networking");
        assertTrue(nonExistentNodes.isEmpty()); // No nodes with this capability
    }

    /**
     * Tests setting and getting nodes.
     */
    @Test
    void testSetAndGetNodes() throws URISyntaxException {
        List<EoscNode> newNodes = new ArrayList<>();
        EoscNode node3 = new EoscNode("3", "Node Three", new URI("http://example.com/logo3"), "PID3", new LegalEntity("Another Entity", new URI("http://example.com/ror3")), new URI("http://example.com/node3"), new ArrayList<>());
        newNodes.add(node3);

        NodeRegistry.setNodes(newNodes);
        assertEquals(1, NodeRegistry.getNodes().size());
        assertEquals(node3, NodeRegistry.getNodes().get(0));
    }
}
