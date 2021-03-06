package com.azureEventHub.sparkReceiver

import org.apache.spark._
import org.apache.spark.sql.SQLContext
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.eventhubs.EventHubsUtils
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.rdd.RDD	
import org.apache.log4j.Logger
import org.apache.log4j.Level
import net.liftweb.json._


/**
 * Hello world!
 *
 */
 
 case class Message1(id : String, strProp : String, longProp : Long, intprop: Int) extends Serializable
 case class Message(msg:String)
 
 case class cdr(towerID: String, tower_freq: String, tower_band: String)
 
object Receiver {


def b2s(a: Array[Byte]): String = new String(a)
  def createContext(checkpointDir: String, ehParams: Map[String, String], outputDir: String) : StreamingContext = {
    println("Creating new StreamingContext")
	
	case class EventContent(EventDetails: String)
	
    // Set max number of cores to double the partition count
    val partitionCount = ehParams("eventhubs.partition.count").toInt
    val sparkConfiguration = new SparkConf().setAppName(this.getClass().getSimpleName())
        sparkConfiguration.set("spark.streaming.receiver.writeAheadLog.enable", "true")
    sparkConfiguration.set("spark.streaming.driver.writeAheadLog.closeFileAfterWrite", "true")
    sparkConfiguration.set("spark.streaming.receiver.writeAheadLog.closeFileAfterWrite", "true")
    sparkConfiguration.set("spark.streaming.stopGracefullyOnShutdown", "true")
	  
	val sparkContext = new SparkContext(sparkConfiguration)
	
	  Logger.getLogger("org").setLevel(Level.OFF)
Logger.getLogger("akka").setLevel(Level.OFF)

	 val sqlContext = new SQLContext(sparkContext)
var dataString :RDD[String] =sparkContext.emptyRDD
  import sqlContext.implicits._
	
    val streamingContext  =  new StreamingContext(sparkContext, Seconds(5))
    streamingContext.checkpoint(checkpointDir)

    // Create a unioned stream for all partitions
    val eventHubsStream = EventHubsUtils.createUnionStream(streamingContext, ehParams)
    
     eventHubsStream.print()
   // eventHubsStream.map(msg=>Message(new String(msg))).foreachRDD(rdd=>rdd.toDF().registerTempTable("mytemptable"))
        eventHubsStream.foreachRDD{rdd=> if( rdd.isEmpty())
        {
        println("RDD IS EMPTY")
        }
        else
        {
     rdd.map(line => b2s(line)).saveAsTextFile("/data/CDRevents")
      
      
  //   val jsondata= rdd.map(line => b2s(line))
   //  val jsonSchemaRDD = sqlContext.jsonRDD(jsondata) 
    // jsonSchemaRDD.registerTempTable("testjson")
//val text = sqlContext.sql("SELECT * FROM testjson").collect()
//val rdd1 = sparkContext.parallelize(text)
//rdd1.saveAsTextFile("/data")
     
      
    //    println("*********************** &&&&&&&&&&&& COUNT ************* &&&&&&&&&&&&&&" +dataString.count())
    //    dataString.collect().foreach(println)
     //  dataString.saveAsTextFile("dataevent/data.txt")
	//	sqlContext.read.json(dataString).registerTempTable("jsoneventdata")
		//val filterData=sqlContext.sql("SELECT * from jsoneventdata")

		//filterData.show(10)
		//filterData.saveAsParquetFile("EventCheckpoint_0.1/ParquetEvent")
 

} }
   
  //  eventHubsStream.saveAsTextFiles(outputDir)
    
  //  val eventHubsWindowedStream = eventHubsStream.window(Seconds(10))
	//eventHubsStream.checkpoint(Seconds(10))
	
	//val batchEventCount = eventHubsWindowedStream.count()
	   
	   // batchEventCount.print()
 //   batchEventCount.saveAsTextFiles(outputDir)
    
	
	
// val sqlContext = new SQLContext(sparkContext)

  // import sqlContext.implicits._

  // val mapped =eventHubsWindowedStream.map{ bytes =>
   // 	val msg = (new String(bytes)).split(",")
    //	Message(msg(0),msg(1),msg(2).toLong,msg(3).toInt)
    //	}
    	

      // 	mapped.foreachRDD( m => {
      // 	println( "size is" +m.collect.size)
       //	})
	
	
   streamingContext
  }

  def main(args: Array[String]):Unit = {
  

    if (args.length < 8) {
      System.err.println("Usage: EventCount <checkpointDirectory> <policyname> <policykey>"
        + "<namespace> <name> <partitionCount> <consumerGroup> <outputDirectory>")
      System.exit(1)
    }
    val Array(checkpointDir, policy, key, namespace, name,
      partitionCount, consumerGroup, outputDir) = args
    val ehParams = Map[String, String](
      "eventhubs.policyname" -> policy,
      "eventhubs.policykey" -> key,
      "eventhubs.namespace" -> namespace,
      "eventhubs.name" -> name,
      "eventhubs.partition.count" -> partitionCount,
      "eventhubs.consumergroup" -> consumerGroup,
      "eventhubs.checkpoint.dir" -> checkpointDir,
      "eventhubs.checkpoint.interval" -> "5"
    )

    // Get StreamingContext from checkpoint directory, or create a new one
    val streamingContext  = StreamingContext.getOrCreate(checkpointDir,
      () => {
        createContext(checkpointDir, ehParams, outputDir)
      })

    streamingContext .start()
	
	val TimeoutInMinutes = -1
	 if(TimeoutInMinutes == -1) {

      streamingContext.awaitTerminationOrTimeout(TimeoutInMinutes.asInstanceOf[Long] * 60 * 1000)
    }
    else {

      streamingContext.awaitTermination()
    }
	

  }
}

