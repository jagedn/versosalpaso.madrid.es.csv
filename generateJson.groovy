import groovy.json.JsonOutput

Map toMap(String[] fields){
    [
            id:fields[0],
            latitud:fields[1],
            longitud:fields[2],
            autor:fields[3],
            barrio:fields[4],
            verso:fields[5],
            direccion:fields[6],
            openstreetmap:"https://www.openstreetmap.org/#map=17/${fields[1]}/${fields[2]}"
    ]
}

url = "https://jagedn.github.io/versosalpaso.madrid.es.csv/versos"

file = new File("versosalpaso.csv")

dataDir = new File("public")
dataDir.mkdirs()

byId = new File(dataDir, "versos")
byId.mkdirs()

all = []
file.readLines().eachWithIndex { line, idx ->
    if (idx == 0)
        return
    def fields = line.replaceAll('﻿', '').split('\\|')
    def map = toMap(fields)
    all.add map
}

all.sort{it.id as int}
all.each {
    new File(byId, "${it.id}.json").text = JsonOutput.prettyPrint(JsonOutput.toJson(it))
}

new File(dataDir,"ids.json").text = JsonOutput.prettyPrint(JsonOutput.toJson(all.collect{
    [ id:"${it.id}", url: "$url/${it.id}.json"]
}))


authors = all.inject([:],{map, entry->
    def list = map[entry.autor] ?: []
    list.add entry
    map[entry.autor] = list
    map
})
new File(dataDir,"auths.json").text = JsonOutput.prettyPrint(JsonOutput.toJson(authors.collect{
    def list = it.value.collect{[ id:"${it.id}", url: "$url/${it.id}.json", verso:it.verso]}
    [ id:"${it.key}", versos: list]
}))

barrios = all.inject([:],{map, entry->
    def list = map[entry.barrio] ?: []
    list.add entry
    map[entry.barrio] = list
    map
})
new File(dataDir,"barrios.json").text = JsonOutput.prettyPrint(JsonOutput.toJson(barrios.collect{
    def list = it.value.collect{[ id:"${it.id}", url: "$url/${it.id}.json", verso:it.verso]}
    [ id:"${it.key}", versos: list]
}))

println "Total versos "+all.size()
println "Total autores "+authors.size()
println "Total barrios "+barrios.size()