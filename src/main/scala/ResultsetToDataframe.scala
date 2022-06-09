import org.apache.spark.sql.types.{BooleanType, DateType, DoubleType, FloatType, IntegerType, StringType, StructField, StructType, TimestampType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.json.{JSONArray, JSONObject}
import java.sql.ResultSet



class ResultsetToDataframe(var resultSet: ResultSet) {

  var spark: SparkSession = _
  var resultSetList: Seq[Row] = Seq(Row())
  var colCount = 0
  var colNames: List[String] = List()
  var colType: List[String] = List()
  var schema: StructType = _

  def initialize(resultSet: ResultSet,spark: SparkSession) = {
    this.spark = spark
    if(!resultSet.isClosed) {
      colCount = resultSet.getMetaData.getColumnCount
      for(i <- 1 to  resultSet.getMetaData.getColumnCount) colType = colType :+ resultSet.getMetaData.getColumnTypeName(i)
      for (i <- 1 to resultSet.getMetaData.getColumnCount) colNames = colNames :+ resultSet.getMetaData.getColumnName(i)
      schema = StructType(createDFSchema(resultSet, colNames))
      resultSetList =  Iterator.continually((resultSet.next(), resultSet)).takeWhile(_._1).map(r => {getRowFromResultSet(r._2)}).toSeq
    }
  }

  def this(resultSet: ResultSet,spark: SparkSession) = {
    this(resultSet)
    initialize(resultSet,spark)
  }





  //Function to print ResultSet
  def printlnRS(resultSet: ResultSet) = {
    //    resultSet.first()
    var strRS = ""
    val rows = resultSetList
    strRS+=colNames.mkString(" | ")+"\n"
    for (i <- 0 until rows.length) strRS+= rows(i).mkString(" | ")+"\n"
    println(strRS)
  }


  //Function to convert ResultSet to Dataframe
  def ResultSetToDataframe(resultSet: ResultSet): DataFrame = {
    //    resultSet.first()
    println(resultSetList)
    val tempDf = spark.createDataFrame(spark.sparkContext.parallelize(resultSetList), schema)
    //    val requiredDf = tempDf.select((0 until lengthOfRow).map(i => col("value")(i).alias(colNames(i))): _*)
    tempDf
  }

  def createDFSchema(resultSet: ResultSet, colNames: Seq[String]) = {
    var DfSchema: Array[StructField] = Array()

    for(i <- 0 until  colCount) {
      colType(i) match {
        case "FLOAT" =>
          DfSchema = DfSchema :+ StructField(colNames(i), FloatType, true)
        case "DOUBLE" =>
          DfSchema = DfSchema :+ StructField(colNames(i), DoubleType, true)
        case "DECIMAL" =>
          DfSchema = DfSchema :+ StructField(colNames(i), DoubleType, true)
        case "BIGINT" =>
          DfSchema = DfSchema :+ StructField(colNames(i), IntegerType, true)
        case "VARCHAR" =>
          DfSchema = DfSchema :+ StructField(colNames(i), StringType, true)
        case "CHAR" =>
          DfSchema = DfSchema :+ StructField(colNames(i), StringType, true)
        case "BOOLEAN" =>
          DfSchema = DfSchema :+ StructField(colNames(i), BooleanType, true)
        case "TIMESTAMP" =>
          DfSchema = DfSchema :+ StructField(colNames(i), TimestampType, true)
        case "DATE" =>
          DfSchema = DfSchema :+ StructField(colNames(i), DateType, true)

      }

    }
    DfSchema
  }

  def getRowFromResultSet(resultSet: ResultSet) = {
    var rowSeq: Seq[Any] = Seq()
    for (i <- 1 to  colCount) {
      colType(i-1) match {
        case "DECIMAL" =>
          rowSeq = rowSeq :+ resultSet.getDouble(i)
        case "FLOAT" =>
          rowSeq = rowSeq :+ resultSet.getFloat(i)
        case "DOUBLE" =>
          rowSeq = rowSeq :+ resultSet.getDouble(i)
        case "BIGINT" =>
          rowSeq = rowSeq :+ resultSet.getInt(i)
        case "VARCHAR" =>
          rowSeq = rowSeq :+ resultSet.getString(i)
        case "CHAR" =>
          rowSeq = rowSeq :+ resultSet.getString(i)
        case "BOOLEAN" =>
          rowSeq = rowSeq :+ resultSet.getBoolean(i)
        case "TIMESTAMP" =>
          rowSeq = rowSeq :+ resultSet.getTimestamp(i)
        case "DATE" =>
          rowSeq = rowSeq :+ resultSet.getDate(i)
      }
    }
    Row.fromSeq(rowSeq)
  }

  def ResultSetToJSON(resultSet: ResultSet) = {
    //    resultSet.first()
    val json = new JSONArray()
    for(j <- 0 until resultSetList.length) {
      val obj = new JSONObject()
      for (i <- 0 until  colCount) {
        obj.put(colNames(i), resultSetList(j)(i))
      }
      json.put(obj)
    }
    json
  }
}


