# versosalpaso.madrid.es.csv
CSV con los versos de las aceras de Madrid , extraidos de la web https://versosalpaso.madrid.es/


Como no he encontrado un dataset con los versos en formato "amigable" he scrappeado la web con este groovy

```
String html = "https://versosalpaso.madrid.es/".toURL().text
def m = html =~ /var marker_([0-9]+) = L.marker\(\[([0-9\,\.-]+)\].+?nombre:"(.+?)".+?barrio:"(.+?)".+?direccion:"(.+?)".+?verso:"(.+?)"/
println "id|latitud|longitud|autor|barrio|verso|direccion"
def lines = m.collect{ token ->
  String line =  [
	token[1],
	token[2].split(',')[0],token[2].split(',')[1],
	token[3],
	token[4],
	token[6],
	token[5]
	].join('|')
  line
}
lines.sort().each{
	println it
}
```
y subido el resultado al repositorio
