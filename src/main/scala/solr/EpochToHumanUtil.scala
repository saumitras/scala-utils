package solr

import java.text.SimpleDateFormat
import java.util.Date

object EpochToHumanUtil extends App {

  case class Col(name:String, colType:Char, mps:String, start:Long, end:Long, startDate:String, endDate:String)
  val colListFilePath = "/home/sam/scripts/collist"

//  val cols = scala.io.Source.fromFile(colListFilePath).getLines().toList.take(100)

  val cols = List(
    "E---siemens-siemens-podv10___1516190400___1516204799"
  )

  val END_EPOCH = 1519797094 //     delete all collections older than this

  val sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss")
  println("End epoch is " + sdf.format(new Date(END_EPOCH.toLong * 1000)))

  val parsedColls = cols.map { c =>  //E---siemens-siemens-podv10___1509863296___1509949696

    val colRegex = """(\w+)---(.*)___(.*)___(.*)""".r

     c match {
      case colRegex(colType, mps, start, end) =>

        Some(Col(c, colType.head, mps, start.toLong, end.toLong,
          sdf.format(new Date(start.toLong * 1000)),
          sdf.format(new Date(end.toLong * 1000))
        ))

      case _ =>
        println("Col name didnt match standard naming convention. " + c)
        None
    }

  }

  val colBefore = parsedColls.filter(_.isDefined).map(_.get).filter(_.end < END_EPOCH).sortBy(_.end)

  //println(colBefore.map(c => c.name).mkString("\n"))
  println(colBefore.map(c => (c.name, c.startDate, c.endDate)).mkString("\n"))

}
