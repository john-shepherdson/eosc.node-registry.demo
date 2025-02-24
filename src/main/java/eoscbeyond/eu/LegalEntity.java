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
 * The {@code LegalEntity} class represents a legal entity with a name and a ROR (Research Organisation Registry) ID.
 * <p>
 * This class provides methods to retrieve and modify the legal entity's name and ROR ID.
 * Additionally, it includes functionality to convert the object into a JSON representation.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>
 *     URI rorUri = new URI("https://ror.org/12345");
 *     LegalEntity entity = new LegalEntity("Example Organisation", rorUri);
 *     String json = entity.toJson();
 *     logger.info(json);
 * </pre>
 *
 * @author John Shepherdson
 * @version 1.0
 */
public class LegalEntity {
    // Name of legal entity
    private String name;
    // ROR ID of Legal entity
    private URI rorId;

    /**
     * Default constructor for LegalEntity.
     */
    public LegalEntity() {
    }

    /**
     * Parameterised constructor for LegalEntity.
     * @param _name  Legal entity name
     * @param _rorId Legal entity ROR ID URI
     */
    public LegalEntity(String _name, URI _rorId) {
        name = _name;
        rorId = _rorId;
    }

     /**
     * Gets the name of the legal entity.
     * @return the name of the legal entity
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the legal entity.
     * @param _name the name of the legal entity
     */
    public void setName(String _name) {
        this.name = _name;
    }

    /**
     * Gets the ROR ID of the legal entity.
     * @return the ROR ID
     */
    public URI getRorId() {
        return this.rorId;
    }

    /**
     * Sets the ROR ID of the legal entity.
     * @param _rorId the ROR ID
     */
    public void setRorId(URI _rorId) {
        this.rorId = _rorId;
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

