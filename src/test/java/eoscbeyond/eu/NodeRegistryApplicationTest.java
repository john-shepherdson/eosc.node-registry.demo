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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NodeRegistryApplicationTest {

    /** Comment. */
    @Mock
    private ReadNodeDetails readNodeDetailsMock;

    /** Comment. */
    @Mock
    private NodeRegistry nodeRegistryMock;

    /** Comment. */
    @InjectMocks
    private NodeRegistryApplication nodeRegistryApplication;

    /** Comment. */
    private List<EoscNode> mockNodes;

    @BeforeEach
    void setUp() throws IOException, URISyntaxException {
        mockNodes = new ArrayList<>();
        when(readNodeDetailsMock.getNodes()).thenReturn(mockNodes);
    }

    /**
     * Test if the application initializes correctly without errors.
     */
    @Test
    void testMainMethod() throws IOException, URISyntaxException {
        NodeRegistryApplication.main(new String[]{});

        verify(readNodeDetailsMock, times(0)).getNodes();
        verify(nodeRegistryMock, times(0)).setNodes(mockNodes);
    }

    /**
     * Test if the `objectMapper()` bean is configured correctly.
     */
    @Test
    void testObjectMapperBean() {
        ObjectMapper objectMapper = nodeRegistryApplication.objectMapper();

        assertThat(objectMapper).isNotNull();
        assertThat(objectMapper.isEnabled(
            SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).isFalse();
    }
}
