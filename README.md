# Capital Gain Calculator (java)
Demonstration command-line interface (CLI) program that calculates the tax to be paid on profits or losses from operations in the stock market.

[Click here to understand the project's assumptions.](docs/PROJECT-PREMISES.md)

# Development Setup

## Prerequisites
The build requires:
- [`Java/OpenJDK 17+`](https://openjdk.java.net/projects/jdk/17/)
- [`Apache Maven 3.6+`](https://maven.apache.org/install.html)

## Building and packaging
Open your terminal (command-line) and go to root of the project, type the command:
```shell
mvn clean package 
```
to generate the .jar package made to run the program.

## Running
After building the project, at root of the project, you can run the program following this pattern:
```
java -jar target/capital-gain-java-${app.version}.jar
```

### Examples:
The examples seen below are based on the actual version of the **capital-gain-java** program (1.0.0).
1. The program expects the user to write each line in the terminal and press 'enter'. To make the program behave in this way, simply execute a command similar to the one shown below:
```shell
java -jar target/capital-gain-java-1.0.0.jar
```
2. In this execution mode, a file can be passed to the application through `Input Redirection`:
```shell
java -jar target/capital-gain-java-1.0.0.jar < inputLines.txt
```

## Testing
At root of the project, type the command:
```shell
mvn clean test
```
to run all the automated tests.

# Tech/framework used
As this is a simple project, few frameworks were adopted. Because of that, they were used in a straightforward manner to achieve the program's objectives, with special emphasis on the Gson library, which greatly facilitated the serialization and deserialization of inputs and outputs in JSON. Here is the list of adopted frameworks and libraries:
- [`Java/OpenJDK 17+`](https://openjdk.java.net/projects/jdk/17/) - Java Development Kit 17
- [`jUnit5`](https://junit.org/junit5/) - Testing framework that supports the creation and execution of automated tests.
- [`Hamcrest`](https://hamcrest.org/JavaHamcrest/) - Framework for writing matcher objects to perform flexible and readable assertions in tests.
- [`Lombok`](https://projectlombok.org/) - Concise library that simplifies boilerplate code by providing annotations for automatic code generation during compilation.
- [`Gson`](https://github.com/google/gson) - JSON processing library that allows the conversion of Java objects to JSON and vice versa.
- [`Jacoco`](https://www.eclemma.org/jacoco/index.html) - Code coverage tool that generates reports to measure and visualize the extent of code covered by tests during Maven builds.

# Architecture
The architecture used in this challenge was based in [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html), however [more flexible and simpler](https://helpdev.com.br/2020/05/21/descomplicando-a-clean-architecture/), with only one layer (core), and prepared for an expansion to a new layer that treats the application layer, composed by packages like `entrypoints`, `configs`, etc. That structure is prepared for robust controllers if needed. 

The **core** layer, composed by `entities`, `usecases`(and `dataprovider` if needed) packages can be totally isolated and free from dependency of frameworks and details unneeded.

### Key architecture decisions:
- **Decision #01:** Start development with fundamental business classes: StockOperation, PaidTax, and OperationResult. This decision was crucial to focus on solving business rules in an organized manner from the beginning. All rules revolved around these core classes, with other classes in the entities package serving as support.

- **Decision #02:** Create the OperationType enum, adopting the enum pattern matching to translate from string to enum when convenient. This decision streamlined the deserialization process of input JSONs.

- **Decision #03:** Adopt the Strategy pattern for choosing between buy and sell operations. I made this decision after testing all sell scenarios costing less than R$ 20k. Around commit 72379bc, I realized it was time for an interesting refactoring, as the code with business rules was becoming unnecessarily complex.

- **Decision #04:** Embrace the functional paradigm in processing input lists to optimize the code and enhance project maintainability and extensibility.