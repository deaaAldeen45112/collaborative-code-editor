package org.test.editor.presentation.config;


import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "github")
public record GithubPropertiesConfig(String clientId,
                               String clientSecret,
                               String tokenUrl,
                               String userUrl,
                               String emailUrl) {
}


