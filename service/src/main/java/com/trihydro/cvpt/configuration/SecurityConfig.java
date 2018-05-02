/********************************************************************
 *  Copyright 2016 Trihydro 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ********************************************************************/

 package com.trihydro.cvpt.configuration;


import com.auth0.spring.security.api.Auth0SecurityConfig;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends Auth0SecurityConfig 
{
    /**
     * Provides Auth0 API access
     */
    @Bean
    public Auth0Client auth0Client() 
    {
        return new Auth0Client(clientId, issuer);
    }

    /**
     *  API Authorization Configuration.
     *
     *  Here we choose not to use the `auth0.securedRoute` property configuration
     *  and instead ensure any unlisted endpoint in our config is secured by default
     */
    @Override
    protected void authorizeRequests(final HttpSecurity http) throws Exception 
    {
        http.authorizeRequests()
            .antMatchers("/cvpt/users/authorize", "/cvpt/users/verify", "/cvpt/users/newUserpasswordTicket").permitAll()
            .antMatchers(HttpMethod.PATCH, "/cvpt/users/password").hasAnyAuthority("ROLE_ADMIN", "ROLE_TRAINING", "ROLE_EQUIPMENT", "ROLE_VEHICLE", "ROLE_READ")
            .antMatchers(HttpMethod.GET, "/cvpt/roles").hasAuthority("ROLE_ADMIN")
            .antMatchers("/cvpt/users").hasAuthority("ROLE_ADMIN")
            .antMatchers("/cvpt/users/**").hasAuthority("ROLE_ADMIN")
            .antMatchers(HttpMethod.GET, "/cvpt/participants").hasAnyAuthority("ROLE_ADMIN", "ROLE_TRAINING", "ROLE_EQUIPMENT", "ROLE_VEHICLE", "ROLE_READ")
            .antMatchers(HttpMethod.POST, "/cvpt/participants").hasAnyAuthority("ROLE_ADMIN", "ROLE_TRAINING")
            .antMatchers(HttpMethod.GET, "/cvpt/participants/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_TRAINING", "ROLE_EQUIPMENT", "ROLE_VEHICLE", "ROLE_READ")
            .antMatchers("/cvpt/participants/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_TRAINING")
            .anyRequest().authenticated();
    }

    /*
     * Only required for sample purposes..
     */
    String getAuthorityStrategy() {
       return super.authorityStrategy;
    }


}