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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The {@code EoscNode} class represents a node in the European Open Science Cloud (EOSC).
 * <p>
 * Each EOSC node has a unique identifier, a name, a logo URI, a persistent identifier (PID), 
 * an associated legal entity, an endpoint URI, and a list of capabilities that describe 
 * the services or functions the node provides.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>
 *     String nodeId = "1234";
 *     String nodeName = "Example Node";
 *     URI logoUri = new URI("https://example.com/logo.png");
 *     URI endpointUri = new URI("https://example.com/endpointapi");
 *     LegalEntity legalEntity = new LegalEntity("Legal Entity Name", new URI("https://ror.org/example"));
 *     ArrayList<EoscCapability> capabilities = new ArrayList<>();
 *     capabilities.add(new EoscCapability("Resource Catalogue", new URI("https://capability.example.com"), "1.0"));
 *
 *     EoscNode node = new EoscNode("123", "Example Node", logoUri, "PID123", legalEntity, endpointUri, capabilities);
 *     String json = node.toJson();
 *     logger.info(json);
 * </pre>
 *
 * @author John Shepherdson
 * @version 1.0
 */
@Schema(description = "Represents a node in the EOSC ecosystem.")
public class EoscNode {
    private String id;
    private String name;
    private URI logo;
    private String pid;
    private LegalEntity legalEntity;
    private URI nodeEndpoint;
    private List<EoscCapability> capabilityList;

    /**
     * Parameterized constructor for EoscNode.
     * @param _id Node ID
     * @param _name Node name
     * @param _logo Node logo URI
     * @param _pid Persistent identifier
     * @param _legal_entity Legal entity associated with the node
     * @param _node_endpoint Node endpoint URI
     * @param _capabilityList List of capabilities
     */
    public EoscNode(String _id, String _name, URI _logo, String _pid, LegalEntity _legal_entity, URI _node_endpoint, List<EoscCapability> _capabilityList) {
        this.id = _id;
        this.name = _name;
        this.logo = _logo;
        this.pid = _pid;
        this.legalEntity = _legal_entity;
        this.nodeEndpoint = _node_endpoint;
        this.capabilityList = _capabilityList;
    }

    /**
     * Gets the node ID.
     * @return the node ID
     */
    @Operation(summary = "Get node ID", description = "Retrieves the unique identifier of the node.")
    @Parameter(description = "The ID of the node")
    public String getId() {
        return this.id;
    }

    /**
     * Sets the node ID.
     * @param _id the node ID
     */
    public void setId(String _id) {
        this.id = _id;
    }

    /**
     * Gets the node name.
     * @return the node name
     */
    @Operation(summary = "Get node name", description = "Retrieves the name of the node.")
    public String getName() {
        return this.name;
    }

    /**
     * Sets the node name.
     * @param name the node name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the node logo URI.
     * @return the logo URI
     */
    @Operation(summary = "Get node logo URI", description = "Retrieves the URI of the node's logo.")
    public URI getLogo() {
        return logo;
    }

    /**
     * Sets the node logo URI.
     * @param logo the logo URI
     */
    public void setLogo(URI logo) {
        this.logo = logo;
    }

    /**
     * Gets the persistent identifier.
     * @return the persistent identifier
     */
    @Operation(summary = "Get persistent identifier", description = "Retrieves the persistent identifier of the node.")
    public String getPid() {
        return pid;
    }

    /**
     * Sets the persistent identifier.
     * @param pid the persistent identifier
     */
     public void setPid(String pid) {
        this.pid = pid;
    }

    /**
     * Gets the legal entity.
     * @return the legal entity
     */
    @Operation(summary = "Get legal entity", description = "Retrieves the legal entity associated with the node.")
    public LegalEntity getLegalEntity() {
        return legalEntity;
    }

    /**
     * Sets the legal entity.
     * @param legalEntity the legal entity
     */
    public void setLegalEntity(LegalEntity legal_entity) {
        this.legalEntity = legal_entity;
    }

    /**
     * Gets the node endpoint URI.
     * @return the node endpoint URI
     */
    @Operation(summary = "Get node endpoint URI", description = "Retrieves the endpoint URI of the node.")
    public URI getNodeEndpoint() {
        return nodeEndpoint;
    }

    /**
     * Sets the node endpoint URI.
     * @param node_endpoint the node endpoint URI
     */
    public void setNodeEndpoint(URI node_endpoint) {
        this.nodeEndpoint = node_endpoint;
    }

    /**
     * Gets the list of capabilities.
     * @return the list of capabilities
     */
    @Operation(summary = "Get list of capabilities", description = "Retrieves the list of capabilities offered by the node.")
    public List<EoscCapability> getCapabilityList() {
        return this.capabilityList;
    }

    /**
     * Sets the list of capabilities.
     * @param capabilityList the list of capabilities
     */
    public void setCapabilityList(ArrayList<EoscCapability> _capabilityList) {
        this.capabilityList = _capabilityList;
    }

    /**
     * Converts this object to a JSON representation.
     * @return JSON string representation of this object
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /**
    * Gets the list of capability names.
    * @return the list of capability names as an array of Strings
    */
    public List<String> getCapabilityNames() {
        ArrayList<String> capabilityNames = new ArrayList<String>();
        String capabilityType = null;

        for (int i = 0; i < this.capabilityList.size(); i++){
            capabilityType = this.capabilityList.get(i).getCapabilityType();
            capabilityNames.add(capabilityType.trim());
        }
        return capabilityNames;
    }

    /**
    * Gets the basic node info.
    * @return a JSON string representation of the endpoint and list of capabilities
    */
    @Operation(summary = "Get basic node info", description = "Retrieves basic information about the node, including its endpoint and capability names.")
    public String getBasicNodeInfo(){
        Gson gson = new Gson();
        String endpoint = this.getNodeEndpoint().toString();
        List<String> basicInfo = new ArrayList<>();
        String caps = gson.toJson(this.getCapabilityList());
        basicInfo.add("node endpoint:");
        basicInfo.add(endpoint);
        basicInfo.add("capabilities:");
        basicInfo.add(caps);
        return gson.toJson(basicInfo);
    }
}
