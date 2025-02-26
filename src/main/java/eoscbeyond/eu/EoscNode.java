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
     * @param xId Node ID
     * @param xName Node name
     * @param xLogo Node logo URI
     * @param xPid Persistent identifier
     * @param xLegalxEntity Legal entity associated with the node
     * @param xNodexEndpoint Node endpoint URI
     * @param xCapabilityList List of capabilities
     */
    public EoscNode(String xId, String xName, URI xLogo, String xPid, LegalEntity xLegalxEntity, URI xNodexEndpoint, List<EoscCapability> xCapabilityList) {
        this.id = xId;
        this.name = xName;
        this.logo = xLogo;
        this.pid = xPid;
        this.legalEntity = xLegalxEntity;
        this.nodeEndpoint = xNodexEndpoint;
        this.capabilityList = xCapabilityList;
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
     * @param xId the node ID
     */
    public void setId(String xId) {
        this.id = xId;
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
    public void setLegalEntity(LegalEntity legalxEntity) {
        this.legalEntity = legalxEntity;
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
     * @param nodexEndpoint the node endpoint URI
     */
    public void setNodeEndpoint(URI nodexEndpoint) {
        this.nodeEndpoint = nodexEndpoint;
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
    public void setCapabilityList(List<EoscCapability> xCapabilityList) {
        this.capabilityList = xCapabilityList;
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
