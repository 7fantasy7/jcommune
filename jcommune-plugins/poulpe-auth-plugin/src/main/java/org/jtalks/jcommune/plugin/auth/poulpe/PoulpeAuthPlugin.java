/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.jtalks.jcommune.plugin.auth.poulpe;

import com.google.common.annotations.VisibleForTesting;
import org.jtalks.jcommune.model.dto.UserDto;
import org.jtalks.jcommune.model.entity.PluginProperty;
import org.jtalks.jcommune.model.plugins.SimpleAuthenticationPlugin;
import org.jtalks.jcommune.model.plugins.StatefullPlugin;
import org.jtalks.jcommune.model.plugins.exceptions.NoConnectionException;
import org.jtalks.jcommune.model.plugins.exceptions.UnexpectedErrorException;
import org.jtalks.jcommune.plugin.auth.poulpe.service.PoulpeAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.*;

import static org.jtalks.jcommune.model.entity.PluginProperty.Type.STRING;

/**
 * Provides user registration and authentication services via Poulpe.
 *
 * @author Andrey Pogorelov
 */
public class PoulpeAuthPlugin extends StatefullPlugin
        implements SimpleAuthenticationPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(PoulpeAuthPlugin.class);
    private static final String URL_PROPERTY = "Url";
    private static final String LOGIN_PROPERTY = "Login";
    private static final String PASSWORD_PROPERTY = "Password";
    private PoulpeAuthService service;
    private List<PluginProperty> pluginProperties;

    /** {@inheritDoc} */
    @Override
    public Map<String, String> registerUser(UserDto userDto) throws NoConnectionException, UnexpectedErrorException {
        return registerOrValidate(userDto, false);
    }

    /**
     * Registration or validation do not differ, whether we validate or register depends on parameter.
     * @param userDto information about the user
     * @param validateOnly 'true' if user information should be only validate and 'false' otherwise
     * @return validation errors as pairs field - error message
     * @throws UnexpectedErrorException if external service returns unexpected result
     * @throws NoConnectionException    if we can't connect for any reason to external authentication service
     */
    private Map<String, String> registerOrValidate(UserDto userDto, boolean validateOnly)
            throws UnexpectedErrorException, NoConnectionException {
        try {
            return service.registerUser(userDto, validateOnly);
        } catch (IOException | JAXBException e) {
            LOGGER.error("Parse response error", e);
            throw new UnexpectedErrorException(e);
        } catch (NoConnectionException e) {
            LOGGER.error("Can't connect to Poulpe", e);
            throw e;
        }
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, String> validateUser(UserDto userDto) throws NoConnectionException, UnexpectedErrorException {
        return registerOrValidate(userDto, true);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, String> authenticate(String login, String password)
            throws UnexpectedErrorException, NoConnectionException {
        try {
            return service.authenticate(login, password);
        } catch (IOException | JAXBException e) {
            LOGGER.error("Parse response error", e);
            throw new UnexpectedErrorException(e);
        } catch (NoConnectionException e) {
            LOGGER.error("Can't connect to Poulpe", e);
            throw e;
        }
    }

    @Override
    public boolean supportsJCommuneVersion(String version) {
        return true;
    }

    @Override
    public String getName() {
        return "Poulpe Auth Plugin";
    }

    @Override
    public List<PluginProperty> getConfiguration() {
        return pluginProperties;
    }

    @Override
    public List<PluginProperty> getDefaultConfiguration() {
        PluginProperty url = new PluginProperty(URL_PROPERTY, STRING, "http://localhost:8080");
        PluginProperty login = new PluginProperty(LOGIN_PROPERTY, STRING, "user");
        PluginProperty password = new PluginProperty(PASSWORD_PROPERTY, STRING, "1234");
        return Arrays.asList(url, login, password);
    }

    @Override
    protected Map<PluginProperty, String> applyConfiguration(List<PluginProperty> properties) {

        String url = null;
        String login = null;
        String password = null;
        for (PluginProperty property : properties) {
            if (URL_PROPERTY.equalsIgnoreCase(property.getName())) {
                url = property.getValue();
            } else if (LOGIN_PROPERTY.equalsIgnoreCase(property.getName())) {
                login = property.getValue();
            } else if (PASSWORD_PROPERTY.equalsIgnoreCase(property.getName())) {
                password = property.getValue();
            }
        }
        if (url != null && login != null && password != null) {
            service = new PoulpeAuthService(url, login, password);
            pluginProperties = properties;
        } else {
            // this should be returned as a map, but this mechanism should be implemented in the plugin API first
            throw new RuntimeException("Can't apply configuration: Url, login and password should not be null.");
        }
        return new HashMap<>();
    }

    @VisibleForTesting
    void setPluginService(PoulpeAuthService service) {
        this.service = service;
    }
}
