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
package eoscbeyond.eu.data;

import java.net.URI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public record Configuration(
    URI noderegistryUrl
) {
    private static final URI DEFAULT_NODEREGISTRY_URL = URI.create("http://localhost:1336/");

    private static final Logger log = LogManager.getLogger(Configuration.class);

    public Configuration {
        if (noderegistryUrl != null) {
            if (noderegistryUrl.getScheme().startsWith("http")) {
                log.info("Using Node Registry {}", noderegistryUrl);
            } else {
                throw new IllegalStateException("Invalid scheme \"" + noderegistryUrl.getScheme() + "\" in Node Registry URI \"" + noderegistryUrl + "\"");
            }
        } else {
            // This makes sure a node registry URL is always configured
            log.info("Using default node registry {}", DEFAULT_NODEREGISTRY_URL);
            noderegistryUrl = DEFAULT_NODEREGISTRY_URL;
        }
    }
}
