import org.apache.spark.sql.SparkSession

object Main extends ArgsParser {
  val spark = SparkSession
    .builder()
    .appName("Java Spark Visualizer")
    .config("spark.master", "local")
    .getOrCreate()


  val usersDF = typeInFile() match {
    case csv @ "csv" => spark.read.format(csv)
      .option("sep", ";")
      .option("inferSchema", "true")
      .option("header", "true").load(inputFile)
    case text @ "text" => spark.read.format(text).load(inputFile)
    case json @ "json" => spark.read.format(json).json(inputFile)
    case avro @ "avro" => spark.read.format(avro).load(inputFile)
    case parquet @ "parquet" => spark.read.format(parquet).load(inputFile)
  }

  usersDF.createOrReplaceTempView("dataFrame")
  usersDF.printSchema()

  val dataFrame = spark.sql(s"SELECT ${listOfFields().mkString(", ")} FROM dataFrame")

  dataFrame.show(numberOfRecords())

  if (outputExists()) {
    dataFrame.coalesce(1).write.mode("overwrite").format(typeOutFile()).save(outputFile())
  }
}
