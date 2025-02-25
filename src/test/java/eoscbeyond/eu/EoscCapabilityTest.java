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

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

/**
 * Unit tests for the EoscCapability class.
 */
class EoscCapabilityTest {
    private EoscCapability capability;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() throws URISyntaxException {
        capability = new EoscCapability("Service Monitoring", new URI("https://example.com/api/service-monitoring"),
                "1.2");
    }

    /**
     * Tests the getCapabilityType() and setCapabilityType() methods.
     */
    @Test
    void testGetAndSetCapabilityType() {
        capability.setCapabilityType("Resource Catalogue");
        assertEquals("Resource Catalogue", capability.getCapabilityType());
    }

    /**
     * Tests the getEndpoint() and setEndpoint() methods.
     */
    @Test
    void testGetAndSetEndpoint() throws URISyntaxException {
        URI newEndpoint = new URI("https://example.com/api/resource-catalogue");
        capability.setEndpoint(newEndpoint);
        assertEquals(newEndpoint, capability.getEndpoint());
    }

    /**
     * Tests the getVersion() and setVersion() methods.
     */
    @Test
    void testGetAndSetVersion() {
        capability.setVersion("2.0");
        assertEquals("2.0", capability.getVersion());
    }

    /**
     * Tests the toJson() method.
     */
    @Test
    void testToJson() {
        Gson gson = new Gson();
        String expectedJson = gson.toJson(capability);
        assertEquals(expectedJson, capability.toJson());
    }
}
