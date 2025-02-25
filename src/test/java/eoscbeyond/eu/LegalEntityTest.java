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
 * Unit tests for the LegalEntity class.
 */
class LegalEntityTest {
    private LegalEntity legalEntity;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() throws URISyntaxException {
        legalEntity = new LegalEntity("Example Corp", new URI("https://ror.org/12345"));
    }

    /**
     * Tests the getName() and setName() methods.
     */
    @Test
    void testGetAndSetName() {
        legalEntity.setName("New Corp");
        assertEquals("New Corp", legalEntity.getName());
    }

    /**
     * Tests the getRorId() and setRorId() methods.
     */
    @Test
    void testGetAndSetRorId() throws URISyntaxException {
        URI newRorId = new URI("https://ror.org/67890");
        legalEntity.setRorId(newRorId);
        assertEquals(newRorId, legalEntity.getRorId());
    }

    /**
     * Tests the toJson() method.
     */
    @Test
    void testToJson() {
        Gson gson = new Gson();
        String expectedJson = gson.toJson(legalEntity);
        assertEquals(expectedJson, legalEntity.toJson());
    }
}
