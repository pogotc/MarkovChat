import scala.util.Random

class Markov( sourcePath: String ){

	val source = scala.io.Source.fromFile(sourcePath)
	val lines = source.getLines mkString "\n"
	source.close()

	val map = scala.collection.mutable.Map[String, scala.collection.mutable.Map[String, Int]]()
	val components = lines.split(" ")

	for(i <- 0 to components.length - 2){
		val word = components(i)
		val nextWord = components(i + 1)
		
		if(map.contains(word)){
			var elem = map(word)
			if(elem.contains(nextWord)){
				elem(nextWord) = elem(nextWord) + 1
			}else{
				elem(nextWord) = 1
			}
			
		}else{
			map(word) = scala.collection.mutable.Map(nextWord -> 1)
		}

	}

	def getNextChunk(base: String): String = {
		if(!map.contains(base)){
			return ""
		}else{
			val elems = map(base)
			var totalElems = 0

			elems.keys.foreach { i =>
				totalElems+= elems(i)
			}

			val random = new Random()
			val randElem = scala.math.floor(random.nextDouble * totalElems)
			
			var runningTotal = 0

			elems.keys.foreach { i => 
				runningTotal += elems(i)
				if(runningTotal > randElem){
					return i
				}
			}

			return ""
		}
	}

	def findMappedWordInString(input: String): String = {
		val words = input.split(" ")

		for(word <- words){
			if(this.map.contains(word)){
				return word
			}
		}
		return ""
	}

	def startWithFor(base: String, length: Int): String = {
		var result = ""

		//Start by trying to find a word that's in our dictionary
		var word = this.findMappedWordInString(base)
		if(word == ""){
			return ""
		}


		while(result.length < length && word.length > 0){
			result = result + word + " "
			word = this.getNextChunk(word)
		}
		result
	}
}
