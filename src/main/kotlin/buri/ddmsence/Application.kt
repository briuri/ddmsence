package buri.ddmsence

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.forwardedheaders.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*
import org.slf4j.LoggerFactory
import org.thymeleaf.TemplateEngine
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

/**
 * Launches the KTOR server.
 */
fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

/**
 * Installs plugins and sets up routing.
 */
fun Application.module() {
    val environment = this.environment
   
    install(XForwardedHeaders) {

    }
    install(StatusPages) {
        setup(environment)
    }
    install(Thymeleaf) {
        setup()
    }

    routing {
        staticResources("/robots.txt", "static", index = "robots.txt")
        staticResources("/css", "static.css")
        staticResources("/docs", "static.docs")
        staticResources("/files", "static.files")
        staticResources("/images", "static.images")
        staticResources("/js", "static.js")

        get("/") {
            call.respondRedirect("/index.jsp")
        }
        get("/index.jsp") {
            val model = environment.buildModel(
                request = call.request,
                title = "Home"
            )
            call.respond(ThymeleafContent("home", model))
        }
        get("/documentation.jsp") {
            val model = environment.buildModel(
                request = call.request,
                title = "Documentation"
            )
            call.respond(ThymeleafContent("documentation", model))
        }
        get("/documentation-attributes.jsp") {
            val model = environment.buildModel(
                request = call.request,
                title = "Power Tip - Common Attribute Groups"
            )
            call.respond(ThymeleafContent("documentation-attributes", model))
        }
        get("/documentation-builders.jsp") {
            val model = environment.buildModel(
                request = call.request,
                title = "Power Tip - Using Component Builders"
            )
            call.respond(ThymeleafContent("documentation-builders", model))
        }
        get("/documentation-configuration.jsp") {
            val model = environment.buildModel(
                request = call.request,
                title = "Power Tip: Configurable Properties"
            )
            call.respond(ThymeleafContent("documentation-configuration", model))
        }
        get("/documentation-differentIsm.jsp") {
            val model = environment.buildModel(
                request = call.request,
                title = "Power Tip: Using Alternate Versions of Intelligence Community Specifications"
            )
            call.respond(ThymeleafContent("documentation-differentIsm", model))
        }
        get("/documentation-extensible.jsp") {
            val model = environment.buildModel(
                request = call.request,
                title = "Power Tip -The Extensible Layer"
            )
            call.respond(ThymeleafContent("documentation-extensible", model))
        }
        get("/documentation-multithreaded.jsp") {
            val model = environment.buildModel(
                request = call.request,
                title = "Power Tip - Thread Safety"
            )
            call.respond(ThymeleafContent("documentation-multithreaded", model))
        }
        get("/documentation-schematron.jsp") {
            val model = environment.buildModel(
                request = call.request,
                title = "Power Tip - Schematron Validation"
            )
            call.respond(ThymeleafContent("documentation-schematron", model))
        }
        get("/documentation-version.jsp") {
            val model = environment.buildModel(
                request = call.request,
                title = "Power Tip - Working With Different DDMS Versions"
            )
            call.respond(ThymeleafContent("documentation-version", model))
        }
        get("/downloads.jsp") {
            val model = environment.buildModel(
                request = call.request,
                title = "Downloads"
            )
            call.respond(ThymeleafContent("downloads", model))
        }
        get("/license.jsp") {
            val model = environment.buildModel(
                request = call.request,
                title = "License"
            )
            call.respond(ThymeleafContent("license", model))
        }
        get("/relationalTables.jsp") {
            val model = environment.buildModel(
                request = call.request,
                title = "Relational Database Model for DDMS 3.1"
            )
            call.respond(ThymeleafContent("relationalTables", model))
        }
        get("/releaseNotes-2.2.0.jsp") {
            val model = environment.buildModel(
                request = call.request,
                title = "What's New in DDMS 5.0 / DDMSence 2.2.0"
            )
            call.respond(ThymeleafContent("releaseNotes-2.2.0", model))
        }
        get("/schematron.jsp") {
            val model = environment.buildModel(
                request = call.request,
                title = "Schematron Implementation for DDMS"
            )
            call.respond(ThymeleafContent("schematron", model))
        }
        get("/tutorials-01.jsp") {
            val model = environment.buildModel(
                request = call.request,
                title = "Tutorial #1 (Essentials)"
            )
            call.respond(ThymeleafContent("tutorials-01", model))
        }
        get("/tutorials-02.jsp") {
            val model = environment.buildModel(
                request = call.request,
                title = "Tutorial #2 (Escort)"
            )
            call.respond(ThymeleafContent("tutorials-02", model))
        }
        get("/tutorials-03.jsp") {
            val model = environment.buildModel(
                request = call.request,
                title = "Tutorial #3 (Escape)"
            )
            call.respond(ThymeleafContent("tutorials-03", model))
        }
        get("/upgrade-2.0.0.jsp") {
            val model = environment.buildModel(
                request = call.request,
                title = "Upgrade Guide: Version 1.x to 2.x"
            )
            call.respond(ThymeleafContent("upgrade-2.0.0", model))
        }
    }
}

/**
 * Configuration for Status Pages (404s)
 */
private fun StatusPagesConfig.setup(environment: ApplicationEnvironment) {
    val logger = LoggerFactory.getLogger(this::class.java)

    // 404
    status(HttpStatusCode.NotFound) { call, status ->
        val model = environment.buildModel(
            request = call.request,
            title = "404 Not Found"
        )
        call.respond(status, ThymeleafContent("404", model))
    }

    // 500
    exception<Throwable> { call, cause ->
        logger.error(cause.message)
        val model = environment.buildModel(
            request = call.request,
            title = "500 Internal Server Error"
        )
        model["message"] = cause.message ?: ""
        model["stackTrace"] = cause.stackTraceToString()
        call.respond(HttpStatusCode.InternalServerError, ThymeleafContent("500", model))
    }
}

/**
 * Configuration for Thymeleaf
 */
private fun TemplateEngine.setup() {
    setTemplateResolver(ClassLoaderTemplateResolver().apply {
        prefix = "views/"
        suffix = ".html"
        characterEncoding = "utf-8"
    })
}

/**
 * Builds a model that controllers can add data to.
 */
fun ApplicationEnvironment.buildModel(request: ApplicationRequest, title: String): MutableMap<String, Any> {
    val baseUrl = this.config.property("ddmsence.baseUrl").getString()
    return mutableMapOf(
        // Environment properties.
        "baseUrl" to baseUrl,

        // Head metadata
        "path" to request.path(),
        "title" to title,
    )
}