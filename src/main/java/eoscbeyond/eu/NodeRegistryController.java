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

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/nodes")
@Tag(name = "Node Registry", description = "Endpoints for managing node registry")
public class NodeRegistryController {

    private final NodeRegistry nodeRegistry;

    // Constructor Injection
    public NodeRegistryController() {
        this.nodeRegistry = new NodeRegistry(new ArrayList<>()); // Initializing with an empty list
    }

    @Operation(summary = "Get all nodes", description = "Retrieves the list of all registered nodes.")
    @GetMapping
    public ResponseEntity<ArrayList<EoscNode>> getAllNodes() {
        return ResponseEntity.ok(NodeRegistry.getNodes());
    }

    @Operation(summary = "Set nodes", description = "Sets a new list of nodes in the registry.")
    @PostMapping("/set")
    public ResponseEntity<String> setNodes(@RequestBody ArrayList<EoscNode> nodes) {
        nodeRegistry.setNodes(nodes);
        return ResponseEntity.ok("Node list updated successfully");
    }

    @Operation(summary = "Search node by ID", description = "Retrieves a specific node by its ID.")
    @GetMapping("/{id}")
    public ResponseEntity<EoscNode> getNodeById(@PathVariable String id) {
        EoscNode node = nodeRegistry.searchNodeById(id);
        if (node != null) {
            return ResponseEntity.ok(node);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Search nodes by capability", description = "Finds nodes that offer a specific capability.")
    @GetMapping("/search")
    public ResponseEntity<ArrayList<EoscNode>> searchNodesByCapability(@RequestParam String capability) {
        ArrayList<EoscNode> nodes = nodeRegistry.searchNodesByCapability(capability);
        return ResponseEntity.ok(nodes);
    }
}

