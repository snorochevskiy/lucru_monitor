package snorochevskiy.lm.crawler.http

import zio.*
import zio.http.*
import zio.json.*

case class JobCategoryRespEntity(
  id: Long,
  name: String
)

case class JobLocationRespEntity(
  id: Long,
  name: String, // e.g. "Krak\u00f3w"
  slug: String, // e.g. "krakow"
)

/*
{
      "id":3647,
      "date":"2023-12-20T01:10:03",
      "date_gmt":"2023-12-20T00:10:03",
      "guid":{
         "rendered":"https:\/\/careers.softwaremind.com\/jobs\/front-end-software-engineer-javascript\/"
      },
      "modified":"2023-12-20T01:10:03",
      "modified_gmt":"2023-12-20T00:10:03",
      "slug":"front-end-software-engineer-javascript",
      "status":"publish",
      "type":"jobs",
      "link":"https:\/\/careers.softwaremind.com\/jobs\/front-end-software-engineer-javascript\/",
      "title":{
         "rendered":"Front-end Software Engineer (JavaScript)"
      },
      "content":{
         "rendered":"",
         "protected":false
      },
      "author":0,
      "menu_order":0,
      "template":"",
      "job-categories":[
         104,
         129,
         107,
         153
      ],
      "location":[
         191,
         39
      ],
      "type-of-employment":[
         47,
         46
      ],
      "acf":[

      ],
      "aioseo_notices":[

      ],
      "custom_fields":{
         "project":"<p>We are currently in search of a skilled JavaScript Developer specializing in Progressive Web Applications (PWAs) to join the team of our client, a prominent supplier of broadcast reception equipment.<\/p>\n",
         "position":"<ul>\n<li>Design, develop, and optimize Progressive Web Applications.<\/li>\n<li>Work with cross-functional teams to create high-performance web applications for embedded products such as set-top boxes, home routers, networking devices, and gateways.<\/li>\n<li>Need to have a strong foundation in front-end development, with a proven track record in building PWAs apps.<\/li>\n<li>Collaborate with UI\/UX designers to implement PWA features and ensure a seamless user experience across devices.<\/li>\n<li>Work closely with backend developers to integrate PWA functionality with server-side logic.<\/li>\n<li>Implement service workers, offline functionality, and other PWA features to enhance application performance and reliability.<\/li>\n<li>Optimize PWAs for various devices and browsers, ensuring a consistent and smooth experience.<\/li>\n<li>Conduct code reviews to ensure adherence to PWA standards and overall code quality.<\/li>\n<li>Troubleshoot and debug PWA-related issues, providing effective and timely solutions.<\/li>\n<\/ul>\n",
         "expectations":"<ul>\n<li>Proven experience as a JavaScript Developer with a focus on Progressive Web Applications or web GUI for embedded devices\/routers.<\/li>\n<li>Strong proficiency in JavaScript, HTML, and CSS.<\/li>\n<li>Experience with popular JavaScript frameworks\/libraries (e.g., React, Angular, or Vue.js).<\/li>\n<li>Knowledge of responsive design principles and cross-browser compatibility.<\/li>\n<li>Familiarity with version control systems (e.g., Git) and build tools (e.g., Webpack).<\/li>\n<li>Strong problem-solving skills and attention to detail.<\/li>\n<li>Good communication and collaboration skills.<\/li>\n<\/ul>\n",
         "additional_skills":"<ul>\n<li>In-depth understanding of PWA principles, service workers, and offline functionality.<\/li>\n<li>Familiarity with PWA testing frameworks and tools.<\/li>\n<li>Understanding of security best practices in PWA development.<\/li>\n<li>Knowledge of performance optimization techniques for PWAs.<\/li>\n<li>Experience with implementing push notifications in PWAs.<\/li>\n<\/ul>\n",
         "link":"https:\/\/jobs.smartrecruiters.com\/SoftwareMind\/743999951743333--emb-front-end-software-engineer-javascript-?oga=true",
         "nr_ref":"REF877J",
         "posting_uuid":"d4a273a0-bc12-4add-a978-b85b5bcebe1e"
      },
      "_links":{
         "self":[
            {
               "href":"https:\/\/careers.softwaremind.com\/wp-json\/wp\/v2\/jobs\/3647"
            }
         ],
         "collection":[
            {
               "href":"https:\/\/careers.softwaremind.com\/wp-json\/wp\/v2\/jobs"
            }
         ],
         "about":[
            {
               "href":"https:\/\/careers.softwaremind.com\/wp-json\/wp\/v2\/types\/jobs"
            }
         ],
         "version-history":[
            {
               "count":1,
               "href":"https:\/\/careers.softwaremind.com\/wp-json\/wp\/v2\/jobs\/3647\/revisions"
            }
         ],
         "predecessor-version":[
            {
               "id":3649,
               "href":"https:\/\/careers.softwaremind.com\/wp-json\/wp\/v2\/jobs\/3647\/revisions\/3649"
            }
         ],
         "wp:attachment":[
            {
               "href":"https:\/\/careers.softwaremind.com\/wp-json\/wp\/v2\/media?parent=3647"
            }
         ],
         "wp:term":[
            {
               "taxonomy":"job-categories",
               "embeddable":true,
               "href":"https:\/\/careers.softwaremind.com\/wp-json\/wp\/v2\/job-categories?post=3647"
            },
            {
               "taxonomy":"location",
               "embeddable":true,
               "href":"https:\/\/careers.softwaremind.com\/wp-json\/wp\/v2\/location?post=3647"
            },
            {
               "taxonomy":"type-of-employment",
               "embeddable":true,
               "href":"https:\/\/careers.softwaremind.com\/wp-json\/wp\/v2\/type-of-employment?post=3647"
            }
         ],
         "curies":[
            {
               "name":"wp",
               "href":"https:\/\/api.w.org\/{rel}",
               "templated":true
            }
         ]
      }
   },
 */
case class JobRespEntity(
  id: Long,
  date_gmt: String,
  slug: String,
  status: String,
  `job-categories`: List[Long],
  location: List[Long],
  `type-of-employment`: List[Long]
)

class SiradmaCrawler {

  given jobCategoryDecoder: JsonDecoder[JobCategoryRespEntity] = DeriveJsonDecoder.gen[JobCategoryRespEntity]
  given jobLocationDecoder: JsonDecoder[JobLocationRespEntity] = DeriveJsonDecoder.gen[JobLocationRespEntity]
  given jobDecoder: JsonDecoder[JobRespEntity] = DeriveJsonDecoder.gen[JobRespEntity]

  val BaseUrl = "https://careers.softwaremind.com"

  // Categories: 	/wp-json/wp/v2/job-categories
  // Locations: /wp-json/wp/v2/location
  // Jobs: https://careers.softwaremind.com/wp-json/wp/v2/jobs?per_page=6&page=1

  // All have content type: application/json; charset=UTF-8
  // Pagination headers: 'x-wp-total: 21' and 'x-wp-totalpages: 4'

  // TODO: implement fetching all pages! See headers
  def fetchJobCategories(): ZIO[Client & Scope, Serializable, List[JobCategoryRespEntity]] =
    for {
      res <- ZClient.request(Request.get(s"$BaseUrl/wp-json/wp/v2/job-categories"))
      data <- res.body.asString
      jobCategories <- ZIO.fromEither(data.fromJson[List[JobCategoryRespEntity]])
    } yield jobCategories

  def fetchJobLocations() =
    for {
      res <- ZClient.request(Request.get(s"$BaseUrl/wp-json/wp/v2/location"))
      data <- res.body.asString
      jobLocations <- ZIO.fromEither(data.fromJson[List[JobLocationRespEntity]])
    } yield jobLocations

  def fetchJobs() =
    for {
      res <- ZClient.request(Request.get(s"$BaseUrl/wp-json/wp/v2/jobs?per_page=6&page=1"))
      data <- res.body.asString
      jobs <- ZIO.fromEither(data.fromJson[List[JobRespEntity]])
    } yield jobs

  //  def fetchVacanciesLinks() = {
  //    val url = URL.decode(s"$BaseUrl/wp-json/wp/v2/jobs?per_page=6&page=1").toOption.get
  //
  //    val program2 = for {
  //      res <- ZClient.request(Request.get(url))
  //      data <- res.body.asString
  //      _ <- Console.printLine(data)
  //    } yield ()
  //
  //  }

}

object SiradmaCrawler {
  val layer = ZLayer.succeed(new SiradmaCrawler)
}
