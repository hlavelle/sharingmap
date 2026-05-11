package com.sharingmap.search

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "semantic.search")
class SemanticSearchProperties {
    var enabled: Boolean = false
    var provider: String = "yandex"
    var embeddingDim: Int = 256
    var reindexOnStartup: Boolean = false
    var maxDistance: Double = 0.67
}

@Component
@ConfigurationProperties(prefix = "yandex.ai")
class YandexAiProperties {
    var folderId: String = ""
    var apiKey: String = ""
    var iamToken: String = ""
    var docModelUri: String = ""
    var queryModelUri: String = ""
}
