

nestedjarsearch.jar: FORCE /QOpenSys/pkgs/lib/jvm/openjdk-11/bin/java /QOpenSys/pkgs/bin/mvn
	/QOpenSys/pkgs/bin/mvn package
	rm -f ./nestedjarsearch.jar
	cp target/nestedjarsearch-*-with-dependencies.jar ./nestedjarsearch.jar

nestedjarsearch.zip: jarsearch nestedjarsearch.jar /QOpenSys/pkgs/bin/zip
	/QOpenSys/pkgs/bin/zip -0 nestedjarsearch.zip jarsearch nestedjarsearch.jar 

FORCE:

all: nestedjarsearch.zip

clean:
	rm -fr target
	rm -f nestedjarsearch.zip
	rm -f nestedjarsearch.jar

/QOpenSys/pkgs/bin/mvn:
	yum install maven

/QOpenSys/pkgs/lib/jvm/openjdk-11/bin/java:
	yum install openjdk-11

/QOpenSys/pkgs/bin/zip:
	yum install zip
