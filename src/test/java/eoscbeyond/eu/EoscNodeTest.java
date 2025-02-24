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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

/**
 * Unit tests for the EoscNode class.
 */
public class EoscNodeTest {
    private EoscNode node;
    private URI nodeEndpoint;
    private LegalEntity legalEntity;
    private ArrayList<EoscCapability> capabilities;
    private ArrayList<String> capabilityNames;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    public void setUp() throws URISyntaxException {
        legalEntity = new LegalEntity("Test Entity",new URI("https://example.com/legal"));
        capabilities = new ArrayList<>();
        capabilities.add(new EoscCapability("Service Monitoring",new URI("https://example.com/api/service-monitoring"),"1.2"));
        capabilities.add(new EoscCapability("Resource Catalogue",new URI("https://example.com/api/resource-catalogue"),"3.0"));
        node = new EoscNode("1", "Test Node",new URI("https://example.com/logo"),"PID12345",legalEntity,new URI("https://example.com/node-endpoint"),capabilities);
        nodeEndpoint = new URI("https://example.com/node-endpoint");
        capabilityNames = new ArrayList<>();
        capabilityNames.add("Service Monitoring");
        capabilityNames.add("Resource Catalogue");
    }

    /**
     * Tests the getId() and setId() methods.
     */
    @Test
    public void testGetAndSetId() {
        node.setId ("2");
        assertEquals("2", node.getId());
    }

    /**
     * Tests the getName() and setName() methods.
     */
    @Test
    public void testGetAndSetName() {
        node.setName("Updated Node");
        assertEquals("Updated Node", node.getName());
    }

    /**
     * Tests the getLogo() and setLogo() methods.
     */
    @Test
    public void testGetAndSetLogo() throws URISyntaxException {
        URI newLogo = new URI("https://example.com/new-logo");
        node.setLogo(newLogo);
        assertEquals(newLogo, node.getLogo());
    }

    /**
     * Tests the getPid() and setPid() methods.
     */
    @Test
    public void testGetAndSetPid() {
        node.setPid("PID67890");
        assertEquals("PID67890", node.getPid());
    }

    /**
     * Tests the getLegalEntity() and setLegalEntity() methods.
     */
    @Test
    public void testGetAndSetLegalEntity() {
        LegalEntity newEntity = new LegalEntity("New Entity", URI.create("https://example.com/new-legal"));
        node.setLegalEntity(newEntity);
        assertEquals(newEntity, node.getLegalEntity());
    }

    /**
     * Tests the getNodeEndpoint() and setNodeEndpoint() methods.
     */
    @Test
    public void testGetAndSetNodeEndpoint() throws URISyntaxException {
        URI newEndpoint = new URI("https://example.com/new-endpoint");
        node.setNodeEndpoint(newEndpoint);
        assertEquals(newEndpoint, node.getNodeEndpoint());
    }

    /**
     * Tests the getCapabilityList() and setCapabilityList() methods.
     */
    @Test
    public void testGetAndSetCapabilityList() {
        ArrayList<EoscCapability> newCapabilities = new ArrayList<>();
        newCapabilities.add(new EoscCapability("Identity Management",URI.create("https://example.com/api/identity-management"), "2.1"));
        node.setCapabilityList(newCapabilities);
        assertEquals(newCapabilities, node.getCapabilityList());
    }

    /**
     * Tests the toJson() method.
     */
    @Test
    public void testToJson() {
        assertNotNull(node.toJson());
        assertTrue(node.toJson().contains("Test Node"));
    }

    /**
     * Tests the getCapabilityNames() method.
     */
    @Test
    public void testGetCapabilityNames() {
        ArrayList<String> capabilityNames = node.getCapabilityNames();
        assertEquals(2,capabilityNames.size());
        assertEquals("Service Monitoring",capabilityNames.get(0));
        assertEquals("Resource Catalogue",capabilityNames.get(1));
    }

    @Test
    void testGetBasicNodeInfo() {
        Gson gson = new Gson();

        // Expected JSON output
        ArrayList<String> expectedList = new ArrayList<>(capabilityNames);
        expectedList.addFirst(nodeEndpoint.toString()); // Should append the endpoint
        String expectedJson = gson.toJson(expectedList);

        // Actual output from method
        String actualJson = node.getBasicNodeInfo();

        // Assert expected and actual match
        assertEquals(expectedJson, actualJson, "getBasicNodeInfo() did not return the expected JSON string.");
    }
}
