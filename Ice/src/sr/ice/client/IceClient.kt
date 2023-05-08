package sr.ice.client

import Demo.FUWOSPrx
import Demo.FUWSPrx
import Demo.RUWSPrx
import com.zeroc.Ice.Communicator
import com.zeroc.Ice.Util

class IceClient {
    fun run() {
        val communicator: Communicator = Util.initialize()
        val endpoints = "tcp -h 127.0.0.2 -p 10000 -z : udp -h 127.0.0.2 -p 10000 -z"

        while (true) {
            print(">>> ")
            val input = readlnOrNull()
            if (input.isNullOrBlank()) continue

            val splitInput = input.split(" ")
            when (splitInput[0]) {
                "quit" -> {
                    communicator.destroy()
                    return
                }
                "fuws" -> {
                    val base = communicator.stringToProxy("singleton:$endpoints")
                    val fuws = FUWSPrx.checkedCast(base)
                    if (fuws == null) {
                        println("Invalid proxy")
                        continue
                    }
                    val value = fuws.incrementAndGet()
                    println("Value after incrementing: $value")
                }
                "ruws" -> {
                    if (splitInput.size < 3) {
                        println("Expected 3 arguments")
                        continue
                    }
                    val objectId = splitInput[1]
                    val base = communicator.stringToProxy("$objectId:$endpoints")
                    try {
                        val ruws = RUWSPrx.checkedCast(base)
                        if (ruws == null) {
                            println("Invalid proxy")
                            continue
                        }
                        ruws.saveALotOfData(splitInput[2])
                        println("Called `saveALotOfData` with ${splitInput[2]}")
                    } catch (ex: com.zeroc.Ice.ObjectNotExistException) {
                        println("This object does not exist")
                        continue
                    }
                }
                "fuwos" -> {
                    if (splitInput.size < 4) {
                        println("Expected 4 arguments")
                        continue
                    }
                    val objectId = splitInput[1]
                    val base = communicator.stringToProxy("$objectId:$endpoints")
                    try {
                        val fuwos = FUWOSPrx.checkedCast(base)
                        if (fuwos == null) {
                            println("Invalid proxy")
                            continue
                        }
                        val word = fuwos.concatWords(splitInput[2], splitInput[3])
                        println("Concatenated word: $word")
                    } catch (ex: com.zeroc.Ice.ObjectNotExistException) {
                        println("This object does not exist")
                        continue
                    }
                }
                else -> {
                    println("Invalid command")
                }
            }
        }
    }
}

fun main() {
    IceClient().run()
}