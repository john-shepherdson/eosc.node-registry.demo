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

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController // This class is a Spring Boot RESTful controller
@CrossOrigin(origins = "*") // Allow all origins
@RequestMapping("/nodes") // Map HTTP requests to /nodes
@Tag(name = "Node Registry", description =
"Endpoints for managing node registry") // Tag for OpenAPI documentation

/**
 * <p>
 * The {@code NodeRegistryController} class provides RESTful endpoints for
 * managing the EOSC Node Registry.
 * </p>
 * <p>
 * It allows users to retrieve a list of all registered nodes, search for a node
 * by its ID, and search for nodes that offer a specific capability.
 * </p>
 * <p>
 * <strong>Note:</strong> The controller is configured to run as a Spring Boot
 * RESTful controller.
 * </p>
 * 
 * @author John Shepherdson
 */
public class NodeRegistryController {

    /** Get the sole NodeRegistry instance. */
    private NodeRegistry nodeRegistry = NodeRegistry.getInstance();

    /**
     * Get all nodes.
     * @return ResponseEntity list of {@code EoscNode} all registered nodes
     */
    @Operation(summary = "Get all nodes", description =
    "Retrieves the list of all registered nodes.")
    @GetMapping
    public ResponseEntity<List<EoscNode>> getAllNodes() {
        return ResponseEntity.ok(nodeRegistry.getNodes());
    }

    /**
     * Search for node by ID.
     * @param id EoscNode ID
     * @return ResponseEntity {@code String} List of EoscNode summary details
     */
    @Operation(summary = "Search for node by ID", description =
    "Retrieves endpoint and capapility info for a node by its ID.")
    @GetMapping("/{id}")
    public ResponseEntity<String> getNodeById(@PathVariable
    final String id) {
        String nodeSummary = nodeRegistry.searchNodeSummaryById(id);
        if (nodeSummary != null) {
            return ResponseEntity.ok(nodeSummary);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Search for nodes by capability.
     * @param capability name of EoscCapability to search for
     * @return ResponseEntity {@code String} List of EoscNodes that have the capability
     */
    @Operation(summary = "Search for nodes by capability", description =
    "Finds nodes that offer a specific capability.")
    @GetMapping("/search")
    public ResponseEntity<List<EoscNode>> searchNodesByCapability(@RequestParam
    final String capability) {
        List<EoscNode> nodes = nodeRegistry.searchNodesByCapability(capability);
        return ResponseEntity.ok(nodes);
    }
}
