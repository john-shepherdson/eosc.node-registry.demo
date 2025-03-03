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
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * The {@code NodeRegistry} class serves as a registry for storing and managing
 * a collection of {@code EoscNode} instances.
 * <p>
 * It provides functionalities for adding, retrieving, and searching for nodes
 * based on their unique ID or capabilities.
 * </p>
 *
 * <p>
 * Example usage:
 * </p>
 *
 * <pre>
 * List<EoscNode> nodeList = new ArrayList<>();
 * nodeList.add(new EoscNode("123", "Example Node", logoUri, "PID123",
 *         legalEntity, endpointUri, capabilities));
 * NodeRegistry registry = new NodeRegistry(nodeList);
 * EoscNode foundNode = registry.searchNodeById("123");
 * List<EoscNode> storageNodes = registry.searchNodesByCapability
 * ("Resource Catalogue");
 * </pre>
 *
 * <p>
 * <strong>Note:</strong> The registry uses a static list of nodes, meaning
 * all instances
 * share the same data.
 * </p>
 *
 * @author John Shepherdson
 * @version 1.0
 */

@Tag(name = "Node Registry", description =
"Operations related to node management in the registry.")
public class NodeRegistry {
    /**  */
    private static volatile NodeRegistry instance = null;

    /** List of registered nodes. */
    private static List<EoscNode> nodes;
    /** Logger. */
    private static final Logger LOGGER =
    LogManager.getLogger(NodeRegistry.class);

    /**
     * Get the sole instance of NodeRegistry.
     * Create and initialise it if it does not exist already.
     * @return NodeRegistry
     */
    public static NodeRegistry getInstance() {
        if (instance == null) {
            synchronized (NodeRegistry.class) {
                if (instance == null) {
                    instance = new NodeRegistry();
                }
            }
        }
        return instance;
    }

    /** Default constructor. */
    private NodeRegistry() { }

    /**
     * Constructor - creates instance with list of nodes.
     *
     * @param xNodes the list of EOSCNodes
     */
    public NodeRegistry(final List<EoscNode> xNodes) {
        if (xNodes.isEmpty()) {
            LOGGER.error("Node registry not initialised");
        } else {
            // add nodes to singleton, for use by all other classes
            NodeRegistry.nodes = xNodes;
            LOGGER.info(
                    "Node registry initialised. Number of nodes is {}",
                    xNodes.size());
        }
    }

    /**
     * Set list of nodes stored in the registry.
     *
     * @param xNodes the list of EOSCNodes
     */
    public void setNodes(final List<EoscNode> xNodes) {
        NodeRegistry.nodes = xNodes;
    }

    /**
     * Get list of nodes stored in the registry.
     *
     * @return the list of EOSCNodes stored in the registry
     */
    @Operation(summary = "Get nodes", description =
    "Retrieves the list of nodes from the registry.")
    public List<EoscNode> getNodes() {
        return NodeRegistry.nodes;
    }

    /**
     * Search the registry for a node with a specified ID and return full
     * details.
     *
     * @param xId the ID of the node to search for
     * @return matchingNode the EOSCNode that has the specified ID,
     *         otherwise null.
     */
    @Operation(summary = "Search node by ID", description =
    "Retrieves a node from the registry by its ID.")
    public EoscNode searchNodeById(
            @Parameter(description = "ID of the node to retrieve",
            required = true, example = "1") final String xId) {
        EoscNode matchingNode = null;
        for (EoscNode node : nodes) {
            if (node.getId().equals(xId)) {
                matchingNode = node;
                break;
            }
        }
        return matchingNode;
    }

    /**
     * Search the registry for a node with a specified ID and return summary
     * details.
     *
     * @param xId the ID of the node to search for
     * @return matchingNodeSummary summary of details of the EOSCNode that has
     *         the specified ID, otherwise null.
     */
    @Operation(summary = "Search node summary by ID", description =
    "Retrieves summary information for a node from the registry by its ID.")
    public String searchNodeSummaryById(
            @Parameter(description = "ID of the node to retrieve",
            required = true, example = "1") final String xId) {
        String matchingNodeSummary = null;
        for (EoscNode node : nodes) {
            if (node.getId().equals(xId)) {
                matchingNodeSummary = node.getBasicNodeInfo();
                break;
            }
        }
        return matchingNodeSummary;
    }

    /**
     * Searches for nodes that offer a specified capability.
     *
     * @param capName The name of the Capability to look for
     * @return matchingNodes The list of EoscNodes that offer the capability,
     *         otherwise null.
     */
    @Operation(summary = "Search nodes by capability", description =
    "Finds nodes that offer a specified capability.")
    public List<EoscNode> searchNodesByCapability(
            @Parameter(description = "Name of the capability to search for",
            required = true, example = "Resource Catalogue")
            final String capName) {
        List<EoscNode> matchingNodes = new ArrayList<>();
        for (EoscNode node : nodes) {
            List<String> capabilityNames = node.getCapabilityNames();
            if (capabilityNames.contains(capName)) {
                matchingNodes.add(node);
            }
        }
        return matchingNodes;
    }
}
