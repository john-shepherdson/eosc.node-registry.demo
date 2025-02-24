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
 *     System.out.println(json);
 * </pre>
 *
 * @author John Shepherdson
 * @version 1.0
 */
public class EoscCapability {
    private String capability_type;
    private URI endpoint;
    private String version;

     /**
     * Parameterized constructor.
     * @param capability_type the capability type
     * @param endpoint the endpoint URI
     * @param version the version
     */
    public EoscCapability(String capability_type, URI endpoint, String version) {
        this.capability_type = capability_type;
        this.endpoint = endpoint;
        this.version = version;
    }

    /**
     * Gets the capability type.
     * @return the capability type
     */
    public String getCapabilityType() {
        return capability_type;
    }

    /**
     * Sets the capability type.
     * @param capability_type the capability type
     */
    public void setCapabilityType(String capability_type) {
        this.capability_type = capability_type;
    }

    /**
     * Gets the endpoint URI.
     * @return the endpoint URI
     */
    public URI getEndpoint() {
        return endpoint;
    }

    /**
     * Sets the endpoint URI.
     * @param endpoint the endpoint URI
     */
    public void setEndpoint(URI endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * Gets the version.
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the version.
     * @param version the version
     */
    public void setVersion(String version) {
        this.version = version;
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