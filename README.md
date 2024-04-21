<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://bitbucket.org/intelligence-opensent/opensentop/src/master/">
    <img src="backend/src/main/resources/images/gold.png" alt="Logo" width="400" height="140">
  </a>

<h3 align="center">Aurum Bank</h3>

  <p align="center">
    Java, React, and MongoDB Project
    <br />
    <a href="https://github.com/walimorris/aurum"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/walimorris/aurum">Report Bug</a>
    ·
    <a href="https://github.com/walimorris/aurum">Request Feature</a>
  </p>
</div>

<!-- ABOUT THE ARCHETYPE -->
## About This Project
Aurum is the Latin word for gold and the source of its chemical symbol "Au". With that, aurum, is a banking 
application to experiment using MongoDB in banking and financial transactions. Fortunately, MongoDB is ACID
compliant, which provides "all or nothing" transactions and an important feature in any financial transaction. 
Banks, like Wells Fargo, are already using MongoDB for its powerful Document Model, Atlas Data platform and powerful
AI applications. In this project, we'll explore some of those features and more. 

<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these simple example steps.

### Prerequisites
* Node v21.6.2 or higher
  ```sh
  node -v
  ```

* NPM Package Manager
  ```sh
  npm -version
  ```
  
* Java 17
  ```sh
  java --version
  ```

* Maven 3.9 or above
  ```sh
  mvn --version
  ```
### Configuration
1. Have your MongoDB connection string handy. Open up `application.properties` and fill in the required information.
   ```
   spring.application.name=<your project name>
   server.port=<application port - 8080>
   spring.data.mongodb.uri=<your mongodb connection string>
   spring.data.mongodb.database=<your applications mongodb database>
   ```
2. You want to do the same thing in `secrets.properties`
   ```
   secrets.mongodbUri=<your mongodb connection string>
   ```

### Installation
1. Install dependencies (including NPM) default profile - frontend is automated with Eirik Sletteberg's plugin.
   ```sh
   mvn clean install
   ```

2. Run webpack in development mode - you can find your built bundle in `backend/src/main/resources/static/built`
   ```sh
   npm run watch

3. Run `main()` in `AurumApplication.java`

<!-- USAGE EXAMPLES -->
## Usage

This project uses Eirik Sletteberg's [Frontend-Maven-Plugin](https://github.com/eirslett/frontend-maven-plugin) which allows the generated to use a single plugin for both frontend and backend builds in a single repo.
This plugin is capable of various configurations, but the configuration used in this project is minimum only using Webpack and few configurations to install Node and NPM. 
The meat of this usage is from the creation of the project's bundle which is integrated using a `<script>` in root of React application (typical React fashion) exposed in the `index.html` file in the Springboot resources folder.
