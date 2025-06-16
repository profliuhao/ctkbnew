package org.haoai.medixhub.ctkb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "org.haoai.medixhub.ctkb.repository")
public class RepositoryConfig {
}
