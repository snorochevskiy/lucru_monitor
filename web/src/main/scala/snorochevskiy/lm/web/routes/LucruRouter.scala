package snorochevskiy.lm.web.routes

import snorochevskiy.lm.repo.CompanyRepo
import zio.*
import zio.http.*

// https://www.youtube.com/watch?v=N8w3vU7K8ao
object LucruRouter {

  val app: HttpApp[Scope & CompanyRepo] =
    val helloEndpoint: Route[CompanyRepo, Nothing] =
      Method.GET / "health" -> handler {
        Response.text("Healthy")
      }

    val companiesEndpoint: Route[Scope & CompanyRepo, Nothing] =
      Method.GET / "companies" -> handler {
        val flow = for {
          repo      <- ZIO.service[CompanyRepo]
          companies <- repo.listCompanies()
        } yield companies
        flow.map(c => Response.text(c.toString()))
          .orElse(ZIO.succeed(Response.error(Status.InternalServerError))) // TODO: log
      }

    Routes(helloEndpoint, companiesEndpoint).toHttpApp

}
