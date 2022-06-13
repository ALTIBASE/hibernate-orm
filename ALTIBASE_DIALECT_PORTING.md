# How to port AltibaseDialect
This document describes how to port AltibaseDialect to Hibernate to work with Altibase.

## Hibernate full build
If you use the hibernate full build command as below, hibernate core library file containing AltibaseDialect.class will be created and detailed explanation can be found [here](README.md).

    git clone https://github.com/ALTIBASE/hibernate-orm.git
    cd hibernate-orm
    git checkout proper branch(ex:5.1)
    ./gradlew clean build

## AltibaseDialect manual porting
You can compile only AltibaseDialect-related classes and add them to existing libraries without having to build hibernate entirely.

### Hibernate library download
The Hibernate official distribution site provides hibernate core library for each version. The library provided by the Hibernate official distribution site does not include AltibaseDialect.class and should be repackaged and included.

## AltibaseDialect.java compile
The AltibaseDialect.java file for Hibernate version can be downloaded from the link below. Starting with Hibernate 4.2, you need to compile with AltibaseLimitHandler.java.

| Hibernate Ver  |AltibaseDialect.java | AltibaseLimitHandler.java | SequenceInformationExtractorAltibaseDatabaseImpl.java | Required JDK ver |
|---|---|---|---|---|
|3.6|https://github.com/ALTIBASE/hibernate-orm/blob/3.6/hibernate-core/src/main/java/org/hibernate/dialect/AltibaseDialect.java |   |   | 1.5 |
|4.2|https://github.com/ALTIBASE/hibernate-orm/blob/4.2/hibernate-core/src/main/java/org/hibernate/dialect/AltibaseDialect.java | https://github.com/ALTIBASE/hibernate-orm/blob/4.2/hibernate-core/src/main/java/org/hibernate/dialect/pagination/AltibaseLimitHandler.java  |   | 1.6  |
|4.3|https://github.com/ALTIBASE/hibernate-orm/blob/4.3/hibernate-core/src/main/java/org/hibernate/dialect/AltibaseDialect.java | https://github.com/ALTIBASE/hibernate-orm/blob/4.3/hibernate-core/src/main/java/org/hibernate/dialect/pagination/AltibaseLimitHandler.java   |   |  1.6 |
|5.0|https://github.com/ALTIBASE/hibernate-orm/blob/5.0/hibernate-core/src/main/java/org/hibernate/dialect/AltibaseDialect.java | https://github.com/ALTIBASE/hibernate-orm/blob/5.0/hibernate-core/src/main/java/org/hibernate/dialect/pagination/AltibaseLimitHandler.java   |   | 1.6 |
|5.1|https://github.com/ALTIBASE/hibernate-orm/blob/5.1/hibernate-core/src/main/java/org/hibernate/dialect/AltibaseDialect.java | https://github.com/ALTIBASE/hibernate-orm/blob/5.1/hibernate-core/src/main/java/org/hibernate/dialect/pagination/AltibaseLimitHandler.java   |   | 1.6 |
|5.2|https://github.com/ALTIBASE/hibernate-orm/blob/5.2/hibernate-core/src/main/java/org/hibernate/dialect/AltibaseDialect.java | https://github.com/ALTIBASE/hibernate-orm/blob/5.2/hibernate-core/src/main/java/org/hibernate/dialect/pagination/AltibaseLimitHandler.java   |   | 1.8|
|5.3|https://github.com/ALTIBASE/hibernate-orm/blob/5.3/hibernate-core/src/main/java/org/hibernate/dialect/AltibaseDialect.java | https://github.com/ALTIBASE/hibernate-orm/blob/5.3/hibernate-core/src/main/java/org/hibernate/dialect/pagination/AltibaseLimitHandler.java   |   | 1.8|
|5.4(master)|https://github.com/ALTIBASE/hibernate-orm/blob/master/hibernate-core/src/main/java/org/hibernate/dialect/AltibaseDialect.java | https://github.com/ALTIBASE/hibernate-orm/blob/master/hibernate-core/src/main/java/org/hibernate/dialect/pagination/AltibaseLimitHandler.java  | https://github.com/ALTIBASE/hibernate-orm/blob/master/hibernate-core/src/main/java/org/hibernate/tool/schema/extract/internal/SequenceInformationExtractorAltibaseDatabaseImpl.java | 1.8|

1. hibernate jar uncompress

    Move the hibernate-core-x.x.x.Final.jar file from the downloaded Hibernate library file to the directory where the java file is located and uncompress it.
    
        mv hibernate-core-x.x.x.Final.jar to-the-java-file-directory
        cd to-the-java-file-directory
        jar xvf hibernate-core-x.x.x.Final.jar
    
2. Compile AltibaseLimitHandler.java, AltibaseDialect.java and SequenceInformationExtractorAltibaseDatabaseImpl.java files.

        javac -d . -cp . SequenceInformationExtractorAltibaseDatabaseImpl.java
        javac -d . -cp . AltibaseLimitHandler.java
        javac -d . -cp . AltibaseDialect.java
    
3. When compilation is completed, the following class files will be created under the current directory.

        ./org/hibernate/dialect/AltibaseDialect.class
        ./org/hibernate/dialect/pagination/AltibaseLimitHandler.class
        ./org/hibernate/tool/schema/extract/internal/SequenceInformationExtractorAltibaseDatabaseImpl.class

4. Delete the *.java files or move them to another directory so that the Altibase Dialet java source is not included in the jar file.

### Port the AltibaseDialect class to a Hibernate jar file
Make a new jar file using the newly compiled AltibaseDialect classes.

    jar -cvfm hibernate-core-x.x.x.Final.jar META-INF/MANIFEST.MF .
