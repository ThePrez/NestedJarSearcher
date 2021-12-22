

nestedjarsearch.jar: FORCE /QOpenSys/pkgs/lib/jvm/openjdk-11/bin/java /QOpenSys/pkgs/bin/mvn
	JAVA_HOME=/QOpenSys/pkgs/lib/jvm/openjdk-11 /QOpenSys/pkgs/bin/mvn package
	cp target/nestedjarsearch-*-with-dependencies.jar nestedjarsearch.jar

FORCE:

all: nestedjarsearch.jar

uninstall: clean
	rm -r ${INSTALL_ROOT}/QOpenSys/pkgs/lib/sc ${INSTALL_ROOT}/QOpenSys/pkgs/bin/sc jarsearch.jar

clean:
	rm -r target

/QOpenSys/pkgs/bin/mvn:
	yum install maven

/QOpenSys/pkgs/lib/jvm/openjdk-11/bin/java:
	yum install openjdk-11
