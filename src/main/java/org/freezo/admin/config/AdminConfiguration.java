package org.freezo.admin.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "freezo", name = "enableAdmin", havingValue = "true")
public class AdminConfiguration
{
}
