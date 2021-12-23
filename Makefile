

nestedjarsearch.jar: FORCE
	mvn package
	rm -f ./nestedjarsearch.jar
	cp target/nestedjarsearch-*-with-dependencies.jar ./nestedjarsearch.jar

nestedjarsearch.zip: jarsearch nestedjarsearch.jar
	zip -0 nestedjarsearch.zip jarsearch nestedjarsearch.jar 

FORCE:

all: nestedjarsearch.zip

clean:
	rm -fr target
	rm -f nestedjarsearch.zip
	rm -f nestedjarsearch.jar

