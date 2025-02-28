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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*") // Allow all origins
@RequestMapping("/nodes")
@Tag(name = "Node Registry", description = "Endpoints for managing node registry")
public class NodeRegistryController {

    /**
     * @return ResponseEntity<List<EoscNode>>
     */
    @Operation(summary = "Get all nodes", description = "Retrieves the list of all registered nodes.")
    @GetMapping
    public ResponseEntity<List<EoscNode>> getAllNodes() {
        return ResponseEntity.ok(NodeRegistry.getNodes());
    }

    /**
     * @param nodes List of EoscNodes
     * @return ResponseEntity<String>
     */
    @Operation(summary = "Set nodes", description = "Sets a new list of nodes in the registry.")
    @PostMapping("/set")
    public ResponseEntity<String> setNodes(@RequestBody List<EoscNode> nodes) {
        NodeRegistry.setNodes(nodes);
        return ResponseEntity.ok("Node list updated successfully");
    }

    /**
     * @param id EoscNode ID
     * @return ResponseEntity<String> List of EoscNode summary details
     */
    @Operation(summary = "Search for node by ID", description = "Retrieves endpoint and capapility info for a node by its ID.")
    @GetMapping("/{id}")
    public ResponseEntity<String> getNodeSummaryById(@PathVariable String id) {
        String nodeSummary = NodeRegistry.searchNodeSummaryById(id);
        if (nodeSummary != null) {
            return ResponseEntity.ok(nodeSummary);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * @param capability name of EoscCapability to search for
     * @return ResponseEntity<String> List of EoscNodes that have the capability
     */
    @Operation(summary = "Search for nodes by capability", description = "Finds nodes that offer a specific capability.")
    @GetMapping("/search")
    public ResponseEntity<List<EoscNode>> searchNodesByCapability(@RequestParam String capability) {
        List<EoscNode> nodes = NodeRegistry.searchNodesByCapability(capability);
        return ResponseEntity.ok(nodes);
    }
}