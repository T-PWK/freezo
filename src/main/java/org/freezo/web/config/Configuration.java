package org.freezo.web.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@org.springframework.context.annotation.Configuration
@ConditionalOnProperty(prefix = "freezo", name = "enableWeb", havingValue = "true")
public class Configuration
{

}
