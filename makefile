all:
	javac -cp lib/lombok.jar -d bin src/**/*.java src/*.java

clean:
	rm -r bin/*
