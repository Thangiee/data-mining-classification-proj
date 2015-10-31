Thang Le
1000787155

Language used: Scala

REQUIRE java-8-jdk


Option 1 ----- run the pre-compiled jar

1. execute "java -jar classifier.jar"


Option 2 ----- compiling and executing it

1. Download and install Scala 2.11.7 binaries: http://www.scala-lang.org/index.html

2. Download and install SBT (Simple Build tool): http://www.scala-sbt.org/download.html

3. Open a terminal/command line and navigate to the root of the project folder.
   You know you're in the correct directory if you also see this README file.

4. and execute "sbt run" (it many take a while on the first run).

5. Next, it will prompt you to select a main class to run. Select "project1.Main".


------- How to use the program -------

After running it, the program will prompt you to:
    1) Choose a a data set
    2) Choose the number of k-folds
    3) Choose the classifying algorithm
    4) Choose the parameters for the respective algorithm