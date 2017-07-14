package cfdsl

import cfdsl.HealthCheckType.process
import cfdsl.MemModifier.G
import cfdsl.MemModifier.M
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CfDslTest {

    @Test
    fun `basic cf config structure should be supported`() {
        val cfConfig = cf {
            name = "test"
            memory = 512(M)
            host = "myhost"
        }

        assertThat(cfConfig)
                .isEqualTo(CfManifest(name = "test", memory = MEM(512, M), host = "myhost"))
    }

    @Test
    fun `environments should be supported`() {
        val cfConfig = cf {
            name = "test"
            memory = 512(M)
            host = "myhost"

            envs {
                env["key1"] = "val1"
                env["key2"] = "val2"
            }
        }

        assertThat(cfConfig)
                .isEqualTo(
                        CfManifest(
                                name = "test",
                                memory = MEM(512, M),
                                host = "myhost",
                                envs = ENVS(mutableMapOf("key1" to "val1", "key2" to "val2"))))
    }

    @Test
    fun `command if provided should be supported`() {

        val cfConfig = cf {
            name = "test"
            command = "cmd"
        }

        assertThat(cfConfig)
                .isEqualTo(CfManifest(name = "test", command = "cmd"))
    }

    @Test
    fun `buildpack if provided should be supported`() {

        val cfConfig = cf {
            name = "test"
            buildpack = "buildpack"
        }

        assertThat(cfConfig)
                .isEqualTo(CfManifest(name = "test", buildpack = "buildpack"))
    }

    @Test
    fun `domains can be provided as an array`() {
        val cfConfig = cf {
            name = "test"
            domains {
                +"domain1"
                +"domain2"
            }
        }

        assertThat(cfConfig.domains?.domains())
                .contains("domain1", "domain2")
    }

    @Test
    fun `healthchecktype should be process or http or none`() {
        val cfConfig = cf {
            name = "test"
            `health-check-type` = HealthCheckType.process
        }

        assertThat(cfConfig.`health-check-type`)
                .isEqualTo(process)
    }

    @Test
    fun `hosts can be provided as an array`() {
        val cfConfig = cf {
            name = "test"
            hosts {
                +"host1"
                +"host2"
            }
        }

        assertThat(cfConfig.hosts?.hosts())
                .contains("host1", "host2")
    }

    @Test
    fun `instances can be specified`() {
        val cfConfig = cf {
            name = "test"
            instances = 2
        }

        assertThat(cfConfig.instances).isEqualTo(2)
    }

    @Test
    fun `routes can be specified as an array`() {
        val cfConfig = cf {
            name = "test"
            routes {
                +"route1"
                +"route2"
            }
        }

        assertThat(cfConfig.routes?.routes()).contains("route1", "route2")
    }

    @Test
    fun `a full manifest file`() {
        val cf = cf {
            name = "myapp"
            path = "./test.jar"
            instances = 3
            memory = 512(M)
            disk_quota = 1(G)
            `health-check-type` = process
            domains {
                +"internal1.domain.com"
                +"another1.domain.com"
            }
            hosts {
                +"myappname1"
                +"myappname2"
            }
            routes {
                +"route1"
            }
            envs {
                env["key1"] = "val1"
                env["key2"] = "val2"
            }

        }

        assertThat(cf).isEqualTo(CfManifest(
                name = "myapp",
                path = "./test.jar",
                instances = 3,
                memory = MEM(512, M),
                disk_quota = MEM(1, G),
                `health-check-type` = process,
                domains = DOMAINS(mutableListOf("internal1.domain.com", "another1.domain.com")),
                hosts = HOSTS(mutableListOf("myappname1", "myappname2")),
                routes = ROUTES(mutableListOf("route1")),
                envs = ENVS(mutableMapOf("key1" to "val1", "key2" to "val2"))

        ))
    }

    @Test
    fun `another manifest file`() {
        val cf = cf {
            name = "myapp"
            memory = 512(M)
            instances = 1
            path = "target/someapp.jar"
            routes {
                +"somehost.com"
                +"another.com/path"
            }
            envs {
                env["ENV_NAME1"] = "VALUE1"
                env["ENV_NAME2"] = "VALUE2"
            }
        }

        assertThat(cf).isEqualTo(CfManifest(
                name = "myapp",
                path = "target/someapp.jar",
                instances = 1,
                memory = MEM(512, M),
                routes = ROUTES(mutableListOf("somehost.com", "another.com/path")),
                envs = ENVS(mutableMapOf("ENV_NAME1" to "VALUE1", "ENV_NAME2" to "VALUE2"))))
    }
}