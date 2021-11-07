import org.apache.commons.cli._

import scala.sys.exit

class ArgsParser extends App {
    validation()

    def inputFile(): String = inputFile
    def outputFile(): String = outputFile
    def typeOutFile(): String = typeOutFile
    def typeInFile(): String = typeInFile
    def listOfFields(): List[String] = listOfFields
    def numberOfRecords(): Int = numberOfRecords
    def outputExists(): Boolean = outputExists

    private[this] var inputFile: String = _
    private[this] var outputFile: String = _
    private[this] var typeOutFile: String = _
    private[this] var typeInFile: String = _  // text, csv, json, parquet, orc, avro

    private[this] var listOfFields: List[String] = _
    private[this] var numberOfRecords = 10

    private[this] var outputExists = false

    private[this] def validation(): Unit = {
        val options = new Options()

        val help = new Option("h", "help", false, "parameter for help")
        val inputFile = new Option("inf", "inputFile", true, "parameter input file")
        val outputFile = new Option("out", "outputFile", true, "path to out path")
        val typeOutFile = new Option("tout", "typeOut", true, "type out file")
        val typeInFile = new Option("tin", "typeIn", true, "type input file")
        val listOfFields = new Option("lfields", "listFields", true, "list fields for output")
        val numberOfRecords = new Option("nr", "numberOfRecords", true, "number of records visualization")

        inputFile.setRequired(true)
        outputFile.setRequired(false)
        typeOutFile.setRequired(false)
        typeInFile.setRequired(true)
        listOfFields.setRequired(false)
        numberOfRecords.setRequired(false)

        options.addOption(help)
          .addOption(inputFile)
          .addOption(outputFile)
          .addOption(typeOutFile)
          .addOption(typeInFile)
          .addOption(listOfFields)
          .addOption(numberOfRecords)


        val parser = new DefaultParser()

        def helpPrint() = {
            val formatter = new HelpFormatter()
            println("This program is designed to visualize big data")
            println("==========================================================================================")
            formatter.printHelp("Visualizer", options)
            exit(0)
        }

        try {
            parser.parse(options, args)
            val cmd = parser.parse(options, args)

            if (cmd.hasOption(help)) {
                helpPrint()
            }

            this.inputFile = cmd.getOptionValue(inputFile)
            this.typeInFile = cmd.getOptionValue(typeInFile)

            this.outputFile = if (cmd.hasOption(outputFile)) {
                outputExists = true
                cmd.getOptionValue(outputFile)
            } else {
                null
            }

            this.typeOutFile = if (cmd.hasOption(typeOutFile) && outputExists) {
                cmd.getOptionValue(typeOutFile)
            } else {
                null
            }

            this.listOfFields = if (cmd.hasOption(listOfFields)) {
                cmd.getOptionValue(listOfFields).split(",").toList
            } else {
                List("*")
            }

            this.numberOfRecords = if (cmd.hasOption(numberOfRecords)) {
                cmd.getOptionValue(numberOfRecords).toInt
            } else {
                10
            }
        }
        catch {
            case e: Exception =>
                e.printStackTrace()
                helpPrint()
        }
    }


}
