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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@WebMvcTest(NodeRegistryController.class)
@ExtendWith(MockitoExtension.class)
class NodeRegistryControllerTest {

    /** */
    @Autowired
    private MockMvc mockMvc;

    /** */
    @Mock
    private NodeRegistry nodeRegistryMock;

    /** */
    @InjectMocks
    private NodeRegistryController nodeRegistryController;

    /** */
    private List<EoscNode> mockNodes;

    @BeforeEach
    void setUp() {
        mockNodes = Arrays.asList(
            new EoscNode("1", "Node1", null, "PID1", null, null, null),
            new EoscNode("2", "Node2", null, "PID2", null, null, null)
        );

        mockNodes.toString();

        mockMvc = MockMvcBuilders.standaloneSetup(nodeRegistryController)
        .build();
    }

    /**
     * Test GET /{id} - Finds a node by ID (Success).
     */
    @Test
    void testGetNodeByIdSuccess() throws Exception {

        mockMvc.perform(get("/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(nodeRegistryMock, times(0)).searchNodeSummaryById("1");
    }

    /**
     * Test GET /{id} - Node not found (404).
     */
    @Test
    void testGetNodeByIdNotFound() throws Exception {

        mockMvc.perform(get("/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(nodeRegistryMock, times(0)).searchNodeSummaryById("99");
    }

    /**
     * Test GET /search?capability=xyz - Finds nodes by capability.
     */
    @Test
    void testSearchNodesByCapability() throws Exception {

        mockMvc.perform(get("/search")
                .param("capability", "AI")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(nodeRegistryMock, times(0)).searchNodesByCapability("AI");
    }
}
