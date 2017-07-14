package cfdsl

data class CfManifest(
        var name: String = "",
        var path: String? = null,
        var memory: MEM? = null,
        var buildpack: String? = "",
        var command: String? = null,
        var disk_quota: MEM? = null,
        var domains: DOMAINS? = null,
        var domain: String? = null,
        var `health-check-type`: HealthCheckType? = HealthCheckType.process,
        var host: String? = null,
        var hosts: HOSTS? = null,
        var instances: Int? = null,
        var routes: ROUTES? = null,
        var envs: ENVS = ENVS()
) {

    fun envs(block: ENVS.() -> Unit) {
        this.envs = ENVS().apply(block)
    }

    fun domains(block: DOMAINS.() -> Unit) {
        this.domains = DOMAINS().apply(block)
    }

    fun hosts(block: HOSTS.() -> Unit) {
        this.hosts = HOSTS().apply(block)
    }

    fun routes(block: ROUTES.() -> Unit) {
        this.routes = ROUTES().apply(block)
    }

    operator fun Int.invoke(m: MemModifier): MEM = MEM(this, m)

}

data class ENVS(
        var env: MutableMap<String, String> = mutableMapOf()
)

data class MEM(
        val value: Int,
        val modifier: MemModifier
)

enum class MemModifier {
    M, MB, G, GB
}

enum class HealthCheckType {
    port, process, http
}

data class DOMAINS(
        private val domains: MutableList<String> = mutableListOf()
) {
    operator fun String.unaryPlus() {
        domains.add(this)
    }
    
    fun domains() = domains.toList()
}

data class HOSTS(
        private val hosts: MutableList<String> = mutableListOf()
) {
    operator fun String.unaryPlus() {
        hosts.add(this)
    }
    
    fun hosts() = hosts.toList()
}

data class ROUTES(
        private val routes: MutableList<String> = mutableListOf()
) {
    operator fun String.unaryPlus() {
        routes.add(this)
    }
    
    fun routes(): List<String> = routes.toList()
}