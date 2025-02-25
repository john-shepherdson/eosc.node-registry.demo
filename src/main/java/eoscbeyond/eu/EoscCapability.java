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

import com.google.gson.Gson;


/**
 * The {@code EoscCapability} class represents a capability that an EOSC (European Open Science Cloud) node can offer.
 * <p>
 * This class includes attributes such as the capability type, its endpoint URI, and the version of the capability.
 * It also provides methods to retrieve and modify these attributes, as well as functionality to convert the object
 * into a JSON representation.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>
 *     URI endpointUri = new URI("https://example.com/capability");
 *     EoscCapability capability = new EoscCapability("Resource Catalogue", endpointUri, "1.0");
 *     String json = capability.toJson();
 *     logger.info(json);
 * </pre>
 *
 * @author John Shepherdson
 * @version 1.0
 */
public class EoscCapability {
    private String capabilityType;
    private URI endpoint;
    private String version;

     /**
     * Parameterized constructor.
     * @param _capabilityType the capability type
     * @param _endpoint the endpoint URI
     * @param _version the version
     */
    public EoscCapability(String _capabilityType, URI _endpoint, String _version) {
        this.capabilityType = _capabilityType;
        this.endpoint = _endpoint;
        this.version = _version;
    }

    /**
     * Gets the capability type.
     * @return the capability type
     */
    public String getCapabilityType() {
        return this.capabilityType;
    }

    /**
     * Sets the capability type.
     * @param _capabilityType the capability type
     */
    public void setCapabilityType(String _capabilityType) {
        this.capabilityType = _capabilityType;
    }

    /**
     * Gets the endpoint URI.
     * @return the endpoint URI
     */
    public URI getEndpoint() {
        return this.endpoint;
    }

    /**
     * Sets the endpoint URI.
     * @param _endpoint the endpoint URI
     */
    public void setEndpoint(URI _endpoint) {
        this.endpoint = _endpoint;
    }

    /**
     * Gets the version.
     * @return the version
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Sets the version.
     * @param _version the version
     */
    public void setVersion(String _version) {
        this.version = _version;
    }

    /**
     * Converts this object to a JSON representation.
     * @return JSON string representation of this object
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}